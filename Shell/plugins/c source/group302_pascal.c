/**
 * Pascal's Triangle plugin by group 302 (Eric Hotinger + Trevor Senior)
 *
 * Usage: <pascal> <# rows>
 *
 */
#include <stdio.h>
#include "../esh-sys-utils.h"

static long factorial(int n)
{
  int a;
  
  long ret = 1;

  for(a = 1; a <= n; a++)
    ret = ret * a;

  return ret;
}

static bool init_plugin(struct esh_shell *shell)
{
    printf("Plugin 'pascal' initialized...\n");
    return true;
}


static bool pascal_builtin(struct esh_command *cmd)
{
    if(strcmp(cmd->argv[0], "pascal") == 0)
    {
    int a, b, c;

    b = atoi(cmd->argv[1]);

    if(atoi(cmd->argv[1]) < 22 && atoi(cmd->argv[1]) >= 0)
      {
        for(a = 0; a < b; a++)
        {
          for(c = 0; c <= (b - a - 2); c++)
            printf(" ");

          for(c = 0; c <= a; c++)
            printf("%ld ", factorial(a) / (factorial(c) * factorial(a - c)));

          printf("\n");
        }
      }

    else
      printf("Sorry, we can't handle this number.\n");  

    return true;
    }

return false;
}

struct esh_plugin esh_module = {
  .rank = 1,
  .init = init_plugin,
  .process_builtin = pascal_builtin
};
