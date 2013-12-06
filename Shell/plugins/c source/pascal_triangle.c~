/**
 * Factorial plugin by group 302 (Eric Hotinger + Trevor Senior)
 *
 * Usage: <factorial> <#>
 *
 * Notes: prints out "Sorry, we can't handle this number." if
 * the <#> is >= 32 or < 0.
 */
#include <stdio.h>
#include "../esh-sys-utils.h"

static bool init_plugin(struct esh_shell *shell)
{
    printf("Plugin 'factorial' initialized...\n");
    return true;
}


static bool factorial_builtin(struct esh_command *cmd)
{
    if(strcmp(cmd->argv[0], "factorial") == 0)
  	{
  	int i, n, s = 1;
          n = atoi(cmd->argv[1]);

          for(i = 1; i <= n; i++)
            s = s*i;

  	if(atoi(cmd->argv[1]) < 32 && atoi(cmd->argv[1]) >= 0)
   	  printf("The factorial is %d.\n", s);

    else
      printf("Sorry, we can't handle this number.\n");

  	return true;
  	}

return false;
}

struct esh_plugin esh_module = {
  .rank = 1,
  .init = init_plugin,
  .process_builtin = factorial_builtin
};