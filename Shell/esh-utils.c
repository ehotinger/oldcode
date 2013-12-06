/*
 * esh-utils.c
 * A set of utility routines to manage esh objects.
 *
 * Developed by Godmar Back for CS 3214 Fall 2009
 * Virginia Tech.
 */
#include <stdio.h>
#include <sys/types.h>
#include <dirent.h>
#include <dlfcn.h>
#include <limits.h>

#include "esh.h"

static const char rcsid [] = "$Id: esh-utils.c,v 1.5 2011/03/29 15:46:28 cs3214 Exp $";

/* List of loaded plugins */
struct list esh_plugin_list;

/* Create new command structure and initialize first command word,
 * and/or input or output redirect file. */
struct esh_command * 
esh_command_create(char ** argv, 
                   char *iored_input, 
                   char *iored_output, 
                   bool append_to_output)
{
    struct esh_command *cmd = malloc(sizeof *cmd);

    cmd->iored_input = iored_input;
    cmd->iored_output = iored_output;
    cmd->argv = argv;
    cmd->append_to_output = append_to_output;

    return cmd;
}

/* Create a new pipeline containing only one command */
struct esh_pipeline *
esh_pipeline_create(struct esh_command *cmd)
{
    struct esh_pipeline *pipe = malloc(sizeof *pipe);

    pipe->bg_job = false;
    cmd->pipeline = pipe;
    list_init(&pipe->commands);
    list_push_back(&pipe->commands, &cmd->elem);
    return pipe;
}

/* Complete a pipe's setup by copying I/O redirection information */
void
esh_pipeline_finish(struct esh_pipeline *pipe)
{
    if (list_size(&pipe->commands) == 0)
        return;

    struct esh_command *first;
    first = list_entry(list_front(&pipe->commands), struct esh_command, elem);
    pipe->iored_input = first->iored_input;

    struct esh_command *last;
    last = list_entry(list_back(&pipe->commands), struct esh_command, elem);
    pipe->iored_output = last->iored_output;
    pipe->append_to_output = last->append_to_output;
}

/* Create an empty command line */
struct esh_command_line *
esh_command_line_create_empty(void)
{
    struct esh_command_line *cmdline = malloc(sizeof *cmdline);

    list_init(&cmdline->pipes);
    return cmdline;
}

/* Create a command line with a single pipeline */
struct esh_command_line *
esh_command_line_create(struct esh_pipeline *pipe)
{
    struct esh_command_line *cmdline = esh_command_line_create_empty();

    list_push_back(&cmdline->pipes, &pipe->elem);
    return cmdline;
}

/* Print esh_command structure to stdout */
void
esh_command_print(struct esh_command *cmd)
{
    char **p = cmd->argv;

    printf("  Command:");
    while (*p)
        printf(" %s", *p++);

    printf("\n");

    if (cmd->iored_output)
        printf("  stdout %ss to %s\n", 
                cmd->append_to_output ? "append" : "write",
                cmd->iored_output);

    if (cmd->iored_input)
        printf("  stdin reads from %s\n", cmd->iored_input);
}
  
/* Print esh_pipeline structure to stdout */
void
esh_pipeline_print(struct esh_pipeline *pipe)
{
    int i = 1;
    struct list_elem * e = list_begin (&pipe->commands); 

    printf(" Pipeline\n");
    for (; e != list_end (&pipe->commands); e = list_next (e)) {
        struct esh_command *cmd = list_entry(e, struct esh_command, elem);

        printf(" %d. ", i++);
        esh_command_print(cmd);
    }

    if (pipe->bg_job)
        printf("  - is a background job\n");
}

/* Print esh_command_line structure to stdout */
void 
esh_command_line_print(struct esh_command_line *cmdline)
{
    struct list_elem * e = list_begin (&cmdline->pipes); 

    printf("Command line\n");
    for (; e != list_end (&cmdline->pipes); e = list_next (e)) {
        struct esh_pipeline *pipe = list_entry(e, struct esh_pipeline, elem);

        printf(" ------------- \n");
        esh_pipeline_print(pipe);
    }
    printf("==========================================\n");
}

/* Deallocation functions. */
void 
esh_command_line_free(struct esh_command_line *cmdline)
{
    struct list_elem * e = list_begin (&cmdline->pipes); 

    for (; e != list_end (&cmdline->pipes); ) {
        struct esh_pipeline *pipe = list_entry(e, struct esh_pipeline, elem);
        e = list_remove(e);
        esh_pipeline_free(pipe);
    }
    free(cmdline);
}

void 
esh_pipeline_free(struct esh_pipeline *pipe)
{
    struct list_elem * e = list_begin (&pipe->commands); 

    for (; e != list_end (&pipe->commands); ) {
        struct esh_command *cmd = list_entry(e, struct esh_command, elem);
        e = list_remove(e);
        esh_command_free(cmd);
    }
    free(pipe);
}

void 
esh_command_free(struct esh_command * cmd)
{
    char ** p = cmd->argv;
    while (*p) {
        free(*p++);
    }
    if (cmd->iored_input)
        free(cmd->iored_input);
    if (cmd->iored_output)
        free(cmd->iored_output);
    free(cmd->argv);
    free(cmd);
}

#define PSH_MODULE_NAME "esh_module"

/* Load a plugin referred to by modname */
static struct esh_plugin *
load_plugin(char *modname)
{
    printf("Loading %s ...", modname);
    fflush(stdout);

    void *handle = dlopen(modname, RTLD_LAZY);
    if (handle == NULL) {
        fprintf(stderr, "Could not open %s: %s\n", modname, dlerror());
        return NULL;
    }

    struct esh_plugin * p = dlsym(handle, PSH_MODULE_NAME);
    if (p == NULL) {
        fprintf(stderr, "%s does not define %s\n", modname, PSH_MODULE_NAME);
        dlclose(handle);
        return NULL;
    }

    printf("done.\n");
    return p;
}

static bool sort_by_rank (const struct list_elem *a,
                          const struct list_elem *b,
                          void *aux __attribute__((unused)))
{
    struct esh_plugin * pa =  list_entry(a, struct esh_plugin, elem);
    struct esh_plugin * pb =  list_entry(b, struct esh_plugin, elem);
    return pa->rank < pb->rank;
}

/* Load plugins from directory dirname */
void 
esh_plugin_load_from_directory(char *dirname)
{
    DIR * dir = opendir(dirname);
    if (dir == NULL) {
        perror("opendir");
        return;
    }

    struct dirent * dentry;
    while ((dentry = readdir(dir)) != NULL) {
        if (!strstr(dentry->d_name, ".so"))
            continue;

        char modname[PATH_MAX + 1];
        snprintf(modname, sizeof modname, "%s/%s", dirname, dentry->d_name);

        struct esh_plugin * plugin = load_plugin(modname);
        if (plugin)
            list_push_back(&esh_plugin_list, &plugin->elem);
    }
    closedir(dir);
}

/* Initialize loaded plugins */
void 
esh_plugin_initialize(struct esh_shell *shell)
{
    /* Sort plugins and call init() method. */
    list_sort(&esh_plugin_list, sort_by_rank, NULL);

    struct list_elem * e = list_begin(&esh_plugin_list);
    for (; e != list_end(&esh_plugin_list); e = list_next(e)) {
        struct esh_plugin *plugin = list_entry(e, struct esh_plugin, elem);
        if (plugin->init)
            plugin->init(shell);
    }
}