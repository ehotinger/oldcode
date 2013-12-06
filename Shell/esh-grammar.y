/*
 * esh - the 'pluggable' shell.
 *
 * Developed by Godmar Back for CS 3214 Fall 2009
 * Virginia Tech.
 *
 * This is based on an assignment I did in 1993 as an undergraduate
 * student at Technische Universitaet Berlin.
 *
 * Known bugs: leaks memory when parse errors occur.
 */
%{
#include <stdio.h>
#include <stdlib.h>
#define YYDEBUG 1
int yydebug;
void yyerror(const char *msg);
int yylex(void);

/*
 * Error messages, csh-style
 */
#define MISRED  "Missing name for redirect."
#define INVNUL  "Invalid null command."
#define AMBINP  "Ambiguous input redirect."
#define AMBOUT  "Ambiguous output redirect."

#include "esh.h"

#define obstack_chunk_alloc malloc
#define obstack_chunk_free free

struct cmd_helper {
    struct obstack words;   /* an obstack of char * to collect argv */
    char *iored_input;
    char *iored_output;
    bool append_to_output;
};

/* Initialize cmd_helper and, optionally, set first argv */
static void
init_cmd(struct cmd_helper *cmd, char *firstcmd, 
         char *iored_input, char *iored_output, bool append_to_output)
{
    obstack_init(&cmd->words);
    if (firstcmd)
        obstack_ptr_grow(&cmd->words, firstcmd);

    cmd->iored_output = iored_output;
    cmd->iored_input = iored_input;
    cmd->append_to_output = append_to_output;
}

/* print error message */
static void p_error(char *msg);

/* Convert cmd_helper to esh_command.
 * Ensures NULL-terminated argv[] array
 */
static struct esh_command * 
make_esh_command(struct cmd_helper *cmd)
{
    obstack_ptr_grow(&cmd->words, NULL);

    int sz = obstack_object_size(&cmd->words);
    char **argv = malloc(sz);
    memcpy(argv, obstack_finish(&cmd->words), sz);
    obstack_free(&cmd->words, NULL);

    if (*argv == NULL) {
        free(argv);
        return NULL; 
    }

    return esh_command_create(argv,
                              cmd->iored_input,
                              cmd->iored_output,
                              cmd->append_to_output);
}

/* Called by parser when command line is complete */
static void cmdline_complete(struct esh_command_line *);

/* work-around for bug in flex 2.31 and later */
static void yyunput (int c,char *buf_ptr  ) __attribute__((unused));

%}

/* LALR stack types */
%union {
  struct cmd_helper command;
  struct esh_pipeline * pipe;
  struct esh_command_line * cmdline;
  char *word;
}

/* Nonterminals */
%type <command> input output
%type <command> command
%type <pipe> pipeline
%type <cmdline> cmd_list

/* Terminals */
%token <word> WORD
%token GREATER_GREATER 

%%
cmd_line: cmd_list { cmdline_complete($1); }

cmd_list:   /* Null Command */ { $$ = esh_command_line_create_empty(); }
|       pipeline { 
            esh_pipeline_finish($1);
            $$ = esh_command_line_create($1);
        } 
|       cmd_list ';'
|       cmd_list '&' {
            $$ = $1;
            struct esh_pipeline * last;
            last = list_entry(list_back(&$1->pipes), 
                              struct esh_pipeline, elem);
            last->bg_job = true;
        }
|       cmd_list ';' pipeline   { 
            esh_pipeline_finish($3);
            $$ = $1;
            list_push_back(&$$->pipes, &$3->elem);
        }
|       cmd_list '&' pipeline   { 
            esh_pipeline_finish($3);
            $$ = $1;

            struct esh_pipeline * last;
            last = list_entry(list_back(&$1->pipes), 
                              struct esh_pipeline, elem);
            last->bg_job = true;

            list_push_back(&$$->pipes, &$3->elem);
        }

pipeline: command {
            struct esh_command * pcmd = make_esh_command(&$1);
            if (pcmd == NULL) { p_error(INVNUL); YYABORT; }
            $$ = esh_pipeline_create(pcmd);
        }
|       pipeline '|' command {
            /* Error: 'ls >x | wc' */
            struct esh_command * last;
            last = list_entry(list_back(&$1->commands), 
                              struct esh_command, elem);
            if (last->iored_output) { p_error(AMBOUT); YYABORT; }

            /* Error: 'ls | <x wc' */
            if ($3.iored_input) { p_error(AMBINP); YYABORT; }

            struct esh_command * pcmd = make_esh_command(&$3);
            if (pcmd == NULL) { p_error(INVNUL); YYABORT; }

            list_push_back(&$1->commands, &pcmd->elem);
            pcmd->pipeline = $1;
            $$ = $1;
        }
|       '|' error      { p_error(INVNUL); YYABORT; }
|       pipeline '|' error { p_error(INVNUL); YYABORT; }

command:   WORD { 
            init_cmd(&$$, $1, NULL, NULL, false);
        }
|       input   
|       output
|       command WORD {
            $$ = $1;
            obstack_ptr_grow(&$$.words, $2);
        }
|       command input {
            obstack_free(&$2.words, NULL);
            /* Error: ambiguous redirect 'a <b <c' */
            if($1.iored_input)   { p_error(AMBINP); YYABORT; }
            $$ = $1; 
            $$.iored_input = $2.iored_input;
        }
|       command output {
            obstack_free(&$2.words, NULL);
            /* Error: ambiguous redirect 'a >b >c' */
            if ($1.iored_output) { p_error(AMBOUT); YYABORT; }
            $$ = $1; 
            $$.iored_output = $2.iored_output;
            $$.append_to_output = $2.append_to_output;
        }

input:  '<' WORD { 
            init_cmd(&$$, NULL, $2, NULL, false);
        }
|       '<' error     { p_error(MISRED); YYABORT; }

output: '>' WORD { 
            init_cmd(&$$, NULL, NULL, $2, false);
        }
|       GREATER_GREATER WORD { 
            init_cmd(&$$, NULL, NULL, $2, true);
        }
        /* Error: missing redirect */
|       '>' error     { p_error(MISRED); YYABORT; }
|       GREATER_GREATER error { p_error(MISRED); YYABORT; }

%%
static char * inputline;    /* currently processed input line */
#define YY_INPUT(buf,result,max_size) \
    { \
        result = *inputline ? (buf[0] = *inputline++, 1) : YY_NULL; \
    }

#define YY_NO_UNPUT
#define YY_NO_INPUT
#include "lex.yy.c"

static void
p_error(char *msg) 
{ 
    /* print error */
    fprintf(stderr, "%s\n", msg); 
}

extern int yyparse (void);

/* do not use default error handling since errors are handled above. */
void 
yyerror(const char *msg) { }

static struct esh_command_line * commandline;
static void cmdline_complete(struct esh_command_line *cline)
{
    commandline = cline;
}

/* 
 * parse a commandline.
 */
struct esh_command_line *
esh_parse_command_line(char * line)
{
    inputline = line;
    commandline = NULL;

    int error = yyparse();

    return error ? NULL : commandline;
}