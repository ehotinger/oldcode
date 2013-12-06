/*
 * An example plug-in, which implements the 'cd' command.
 */
#include <stdbool.h>
#include <stdio.h>
#include <pwd.h>
#include <unistd.h>
#include <sys/types.h>
#include "../esh.h"
#include <signal.h>
#include "../esh-sys-utils.h"

static bool 
init_plugin(struct esh_shell *shell)
{
    printf("Plugin 'cd' initialized...\n");
    return true;
}

/* Implement chdir built-in.
 * Returns true if handled, false otherwise. */
static bool
chdir_builtin(struct esh_command *cmd)
{
    if (strcmp(cmd->argv[0], "cd"))
        return false;

    char *dir = cmd->argv[1];
    // if no argument is given, default to home directory
    if (dir == NULL) {
        struct passwd *pw = getpwuid(getuid());
        if (pw == NULL) {
            esh_sys_error("Could not obtain home directory.\n"
                          "getpwuid(%d) failed: ", getuid());
        } else {
            dir = pw->pw_dir;
        }
    }

    if (chdir(dir) != 0)
        esh_sys_error("chdir: ");

    return true;
}

struct esh_plugin esh_module = {
  .rank = 1,
  .init = init_plugin,
  .process_builtin = chdir_builtin
};
