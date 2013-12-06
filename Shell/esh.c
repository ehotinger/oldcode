/**
 * esh - the 'pluggable' shell.
 *
 * Developed by Godmar Back for CS 3214 Fall 2009
 * Virginia Tech.
 */
#include "esh-sys-utils.h"

/**
 * The shell object plugins use.
 * Some methods are set to defaults.
 */
struct esh_shell shell =
{
    .build_prompt = buildPromptWithPlugins,
    .readline = readline,                           /* GNU readline(3) */
    .parse_command_line = esh_parse_command_line    /* Default parser */
};

// ----------------------------------------
//           STATIC METHODS BEGIN
// ----------------------------------------

/**
 * Prints out a helpful usage message and then exits the program.
 *
 * Credit to whoever originally wrote this when the src code of the shell
 * was provided to us; we've apparently deleted the comments and have now lost track
 * of the original comment for this function.
 */
static void usage(char *progname)
{
    printf("Usage: %s -h\n"
           " -h            print this help\n"
           " -p  plugindir directory from which to load plug-ins\n",
           progname);

    exit(EXIT_SUCCESS);
}

// ----------------------------------------
//           STATIC METHODS END
// ----------------------------------------


/* MAIN */
int main(int ac, char *av[])
{
    jid = 0; // global jid; initially set it to 0

    int opt;

    list_init(&esh_plugin_list);
    list_init(&currentJobs);

    /* Process command-line arguments. See getopt(3) */
    while ((opt = getopt(ac, av, "hp:")) > 0) {
        switch (opt) {
        case 'h':
            usage(av[0]);
            break;

        case 'p':
            esh_plugin_load_from_directory(optarg);
            break;
        }
    }

    // BEGIN PLUGIN INITIALIZATION
    esh_plugin_load_from_directory("plugins");
    esh_plugin_initialize(&shell);

    setpgrp(); // In the Linux DLL 4.4.1 library, setpgrp simply calls setpgid(0,0).

    struct termios *sysTTY = esh_sys_tty_init();

    give_terminal_to(getpgrp(), sysTTY);

    /* Read/eval loop. */
    while(true)
    {
        /* Do not output a prompt unless shell's stdin is a terminal */
        char * prompt = isatty(0) ? shell.build_prompt() : NULL;
        char * cmdline = shell.readline(prompt);
        free (prompt);

        if (cmdline == NULL)                      /* User typed EOF */
            break;

        struct esh_command_line * commandLine = shell.parse_command_line(cmdline);
        free (cmdline);
        if (commandLine == NULL)                  /* Error in command line */
            continue;

        if (list_empty(&commandLine->pipes)){     /* User hit enter */
            esh_command_line_free(commandLine);
            continue;
        }

        struct esh_pipeline *pipeline = list_entry(list_begin(&commandLine->pipes), struct esh_pipeline, elem);

        struct esh_command *commands = list_entry(list_begin(&pipeline->commands), struct esh_command, elem);

        // SPECIFIC BUILT-IN COMMAND TYPE (SEE esh-sys-utils.h)
    	int commandType = getCommandType(commands->argv[0]);

    	struct list_elem * listElem = list_begin(&esh_plugin_list);

        // PLUGINS
        pluginProcessor(listElem, commands, commandType);

        // OTHER COMMANDS (BUILT-IN & EXTRANEOUS)
        handleCommands(pipeline, commandType, commands, commandLine, sysTTY,
                       listElem);

        esh_command_line_free(commandLine);
    }

    return 0;
}
