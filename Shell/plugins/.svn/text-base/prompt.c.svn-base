#include <stdbool.h>
#include <stdio.h>
#include "../esh.h"

static bool 
init_plugin(struct esh_shell *shell)
{
    printf("Plugin 'prompt' initialized...\n");
    return true;
}

static char * 
prompt(void)
{
    // the prompt must be dynamically allocated
    return strdup("custom prompt> ");
}

struct esh_plugin esh_module = {
    .rank = 10,
    .init = init_plugin,
    .make_prompt = prompt
};
