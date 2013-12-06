/**
 * Circle area plugin by group 302 (Eric Hotinger + Trevor Senior)
 *
 * Usage: <circlearea> <radius>
 *
 * Notes: prints out "Incorrect input" if fails.
 */
#include <stdio.h>
#include "../esh-sys-utils.h"
#define PI 3.14159

static bool init_plugin(struct esh_shell *shell)
{
    printf("Plugin 'circlearea' initialized...\n");
    return true;
}


static bool circle_area_builtin(struct esh_command *cmd)
{
    if(strcmp(cmd->argv[0], "circlearea") == 0)
  	{
  	int n, s = 0;
        n = atoi(cmd->argv[1]);
	s = PI * n * n;	

  	if(atoi(cmd->argv[1]) < 10000 && atoi(cmd->argv[1]) >= 0)
   	  printf("The circle's area is %d.\n", s);

	else
	  printf("Sorry, we can't handle this number.\n");

  	return true;
  	}

return true;
}

struct esh_plugin esh_module = {
  .rank = 1,
  .init = init_plugin,
  .process_builtin = circle_area_builtin
};
