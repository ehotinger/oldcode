/**
 * esh - the 'extensible' shell.
 *
 * Utility functions for system calls.
 *
 * Developed by Godmar Back for CS 3214 Fall 2009
 * Virginia Tech.
 */
#include <stdbool.h>
#include <signal.h>

#include <stdio.h>

#include <fcntl.h>
#include <termios.h>
#include <sys/wait.h>
#include <readline/readline.h>

#include <errno.h>
#include <string.h>
#include <stdarg.h>
#include <unistd.h>
#include <stdlib.h>
#include <assert.h>

#include "esh.h"

// -------------------------------------------
//            COMMAND TYPES
// -------------------------------------------
#define PLUGIN -1
#define DEFAULT 0
#define EXIT 1
#define JOBS 2
#define FG 3
#define BG 4
#define KILL 5
#define STOP 6

// -------------------------------------------
//            FORKING
// -------------------------------------------
#define IS_CHILD == 0
#define IS_PARENT > 0

// -------------------------------------------
//            LOOP ITERATION
// -------------------------------------------
#define INIT_LISTELEM struct list_elem *listElem = list_begin
#define LISTELEM_BEGIN ; listElem != list_end
#define LISTELEM_END ; listElem = list_next(listElem)

// -------------------------------------------
//            ERRORS
// -------------------------------------------
#define FOREGROUND_ERROR "[ERROR] Foreground Error "
#define FORK_ERROR "[ERROR] Fork Error "
#define PROCESSGRP_ERROR "[ERROR] Process Group Error "
#define DUP2_ERROR "[ERROR] dup2 call Error "
#define PID_ERROR "[ERROR] pid Error "

// -------------------------------------------
//            FLAGS AND ACCESS PERMISSIONS 
// -------------------------------------------
#define OFLAGS O_WRONLY | O_CREAT

// ACCESS PERMISSIONS FOR OPENED FILES
// For flag meanings see:
//
// http://www.gnu.org/software/libc/manual/html_node/Permission-Bits.html
#define ACCESS_PERMISH S_IWUSR | S_IWGRP | S_IRGRP | S_IRUSR


/* Print message to stderr, followed by information about current error. 
 * Use like 'printf' */
void esh_sys_error(char *fmt, ...);
void esh_sys_fatal_error(char *fmt, ...);

/* Get a file descriptor that refers to controlling terminal */
int esh_sys_tty_getfd(void);

/* Initialize tty support.  
 * Return pointer to static structure that saves initial state.
 * Restore this state via esh_sys_tty_restore() whenever the shell
 * takes back control of the terminal.
 */
struct termios * esh_sys_tty_init(void);

/* Save current terminal settings.
 * This function is used when a job is suspended.*/
void esh_sys_tty_save(struct termios *saved_tty_state);

/* Restore terminal to saved settings.
 * This function is used when resuming a suspended job. */
void esh_sys_tty_restore(struct termios *saved_tty_state);

/* Return true if this signal is blocked */
bool esh_signal_is_blocked(int sig);

/* Block a signal. Returns true it was blocked before */
bool esh_signal_block(int sig);

/* Unblock a signal. Returns true it was blocked before */
bool esh_signal_unblock(int sig);

/* Signal handler prototype */
typedef void (*sa_sigaction_t)(int, siginfo_t *, void *);

/* Install signal handler for signal 'sig' */
void esh_signal_sethandler(int sig, sa_sigaction_t handler);

void give_terminal_to(pid_t pgrp, struct termios *pg_tty_state);

void sigchld_handler(int sig, siginfo_t *info, void *_ctxt);

void wait_for_job(struct termios *sysTTY);

void child_status_change(pid_t pid, int status);

int getCommandType(char *command_name);

void printJobFromPipe(struct esh_pipeline *pipe);

void printJobFromList(struct list jobList);

void runJobsCommand(void);

void runForegroundCommand(struct esh_pipeline *myPipe,
                          struct termios *sysTTY);

void runBackgroundCommand(struct esh_pipeline *myPipe);

void runKillCommand(struct esh_pipeline *myPipe, int SIGNAL);

int isFgBgKillStop(int commandType);

struct esh_pipeline* getPipeline(struct esh_command *commands);

void handleBuiltinCommands(struct esh_pipeline *myPipe,
                           int commandType,
                           struct esh_command *commands,
                           struct termios *sysTTY);

void handleChild(struct esh_pipeline *pipeline,
                 struct esh_command *command,
                 struct termios *sysTTY,
                 bool isPiped,
                 int *oldPipe, int *newPipe,
                 struct list_elem *e);

void handleParent(struct esh_pipeline *pipeline,
                  int pid,
                  bool isPiped,
                  int *oldPipe, int *newPipe,
                  struct list_elem *e);

void incrementJobId(void);

void pipeAndForkCommands(struct esh_pipeline *pipeline,
                         struct termios *sysTTY);
void handleOtherCommands(struct esh_pipeline *pipeline,
                         struct termios *sysTTY,
                         struct list_elem * listElem,
                         struct esh_command_line * commandLine);
bool jobsExist(void);

void handleCommands(struct esh_pipeline *myPipe,
                    int commandType,
                    struct esh_command *commands,
                    struct esh_command_line * commandLine,
                    struct termios *sysTTY,
                    struct list_elem* listElem);
void pluginProcessor(struct list_elem * listElem,
                 struct esh_command *commands,
                 int commandType);

char *buildPromptWithPlugins(void);

void closePipe(int *pipe);

void closePipes(int *pipe1, int *pipe2);

void copyPipeContents(int *pipe1, int *pipe2);

void closeDuplicate(int *pipe, int a, int b, int c, int d);

void updatePipelineProcessGroup(struct esh_command *command,
                                struct esh_pipeline *myPipe);

void updatePipelineStatus(struct termios *sysTTY,
                          struct esh_pipeline *myPipe);

void handleIoredInput(struct esh_command *command);

void handleIoredOutput(struct esh_command *command);

bool stringContains(char *string, char *value);

int getJobArgumentId(struct esh_command *commands);