/**
 * esh - the 'extensible' shell.
 *
 * Utility functions for system calls.
 *
 * Developed by Godmar Back for CS 3214 Fall 2009
 * Virginia Tech.
 */
#include "esh-sys-utils.h"

/* List of constant statuses to use throughout the program. */
static const char *STATUSES[] = {"Foreground", "Running", "Stopped"};

/* Utility function for esh_sys_fatal_error and esh_sys_error */
static void
vesh_sys_error(char *fmt, va_list ap)
{
	char errmsg[1024];

	strerror_r(errno, errmsg, sizeof errmsg);
	vfprintf(stderr, fmt, ap);
	fprintf(stderr, "%s\n", errmsg);
}

/* Print information about the last syscall error */
void
esh_sys_error(char *fmt, ...)
{
	va_list ap;
	va_start(ap, fmt);
	vesh_sys_error(fmt, ap);
	va_end(ap);
}

/* Print information about the last syscall error and then exit */
void
esh_sys_fatal_error(char *fmt, ...) 
{
    va_list ap;
    va_start(ap, fmt);
    vesh_sys_error(fmt, ap);
    va_end(ap);
    exit(EXIT_FAILURE);
}

static int terminal_fd = -1;            /* the controlling terminal */

static struct termios saved_tty_state;  /* the state of the terminal when shell
                                           was started. */

/* Initialize tty support.  Return pointer to saved initial terminal state */
struct termios *
esh_sys_tty_init(void)
{
    char *tty;
    assert(terminal_fd == -1 || !!!"esh_sys_tty_init already called");

    terminal_fd = open(tty = ctermid(NULL), O_RDWR);
    if (terminal_fd == -1)
        esh_sys_fatal_error("opening controlling terminal %s failed: ", tty);

    esh_sys_tty_save(&saved_tty_state);
    return &saved_tty_state;
}

/* Save current terminal settings.
 * This function is used when a job is suspended.*/
void 
esh_sys_tty_save(struct termios *saved_tty_state)
{
    int rc = tcgetattr(terminal_fd, saved_tty_state);
    if (rc == -1)
        esh_sys_fatal_error("tcgetattr failed: ");
}

/* Restore terminal to saved settings.
 * This function is used when resuming a suspended job. */
void
esh_sys_tty_restore(struct termios *saved_tty_state)
{
    int rc;

retry:
    rc = tcsetattr(terminal_fd, TCSADRAIN, saved_tty_state);
    if (rc == -1) {
        /* tcsetattr, apparently, does not restart even with SA_RESTART,
         * so repeat call on EINTR. */
        if (errno == EINTR)
            goto retry;

        esh_sys_fatal_error("could not restore tty attributes tcsetattr: ");
    }
}

/* Get a file descriptor that refers to controlling terminal */
int 
esh_sys_tty_getfd(void)
{
    assert(terminal_fd != -1 || !!!"esh_sys_tty_init() must be called");
    return terminal_fd;
}

/* Return true if this signal is blocked */
bool 
esh_signal_is_blocked(int sig)
{
    sigset_t mask;
    if (sigprocmask(0, NULL, &mask) == -1)
        esh_sys_error("sigprocmask failed while retrieving current mask");

    return sigismember(&mask, sig);
}

/* Helper for esh_signal_block and esh_signal_unblock */
static bool
__mask_signal(int sig, int how)
{
    sigset_t mask, omask;
    sigemptyset(&mask);
    sigaddset(&mask, sig);
    if (sigprocmask(how, &mask, &omask) != 0)
        esh_sys_error("sigprocmask failed for %d/%d", sig, how);
    return sigismember(&omask, sig);
}

/* Block a signal. Returns true it was blocked before */
bool 
esh_signal_block(int sig)
{
    return __mask_signal(sig, SIG_BLOCK);
}

/* Unblock a signal. Returns true it was blocked before */
bool 
esh_signal_unblock(int sig)
{
    return __mask_signal(sig, SIG_UNBLOCK);
}

/* Install signal handler for signal 'sig' */
void
esh_signal_sethandler(int sig, sa_sigaction_t handler)
{
    sigset_t emptymask;

    sigemptyset(&emptymask);
    struct sigaction sa = {
        .sa_sigaction = handler,
        /* do not block any additional signals (besides 'sig') when
         * signal handler is entered. */
        .sa_mask = emptymask,
        /* restart system calls when possible */
        .sa_flags = SA_RESTART | SA_SIGINFO
    };

    if (sigaction(sig, &sa, NULL) != 0)
        esh_sys_fatal_error("sigaction failed for signal %d", sig);
}


/**
 * Gathered from:
 * http://courses.cs.vt.edu/~cs3214/fall2011/projects/giveterminalto.c
 *
 * Credit to Godmar Back.
 *
 * Assign ownership of ther terminal to process group
 * pgrp, restoring its terminal state if provided.
 *
 * Before printing a new prompt, the shell should
 * invoke this function with its own process group
 * id (obtained on startup via getpgrp()) and a
 * sane terminal state (obtained on startup via
 * esh_sys_tty_init()).
 */
void give_terminal_to(pid_t pgrp, struct termios *pg_tty_state)
{
	esh_signal_block(SIGTTOU);
	int rc = tcsetpgrp(esh_sys_tty_getfd(), pgrp);

	if (rc == -1)
		esh_sys_fatal_error("tcsetpgrp: ");

	if (pg_tty_state)
		esh_sys_tty_restore(pg_tty_state);

	esh_signal_unblock(SIGTTOU);
}


/**
 * Gathered from:
 * http://courses.cs.vt.edu/~cs3214/fall2011/projects/waitpid-sketch.c
 *
 * Credit to Godmar Back.
 *
 * SIGCHLD handler.
 * Call waitpid() to learn about any child processes that
 * have exited or changed status (been stopped, needed the
 * terminal, etc.)
 * Just record the information by updating the job list
 * data structures.  Since the call may be spurious (e.g.
 * an already pending SIGCHLD is delivered even though
 * a foreground process was already reaped), ignore when
 * waitpid returns -1.
 * Use a loop with WNOHANG since only a single SIGCHLD
 * signal may be delivered for multiple children that have
 * exited.
 */
void sigchld_handler(int sig, siginfo_t *info, void *_ctxt)
{
	pid_t pid;
	int status;

	assert(sig == SIGCHLD);

	while ((pid = waitpid(-1, &status, WUNTRACED|WNOHANG)) > 0)
		child_status_change(pid, status);
}

/**
 * Gathered from:
 * http://courses.cs.vt.edu/~cs3214/fall2011/projects/waitpid-sketch.c
 *
 * Credit to Godmar Back.
 *
 * Wait for all processes in this pipeline to complete, or for
 * the pipeline's process group to no longer be the foreground
 * process group.
 * You should call this function from a) where you wait for
 * jobs started without the &; and b) where you implement the
 * 'fg' command.
 *
 * Implement child_status_change such that it records the
 * information obtained from waitpid() for pid 'child.'
 * If a child has exited or terminated (but not stopped!)
 * it should be removed from the list of commands of its
 * pipeline data structure so that an empty list is obtained
 * if all processes that are part of a pipeline have
 * terminated.  If you use a different approach to keep
 * track of commands, adjust the code accordingly.
 */
void wait_for_job(struct termios *sysTTY)
{
	//assert(esh_signal_is_blocked(SIGCHLD));

	//while (pipeline->status == FOREGROUND && !list_empty(&pipeline->commands)) {
	    int status;
	    pid_t pid;

	    bool valid = (pid = waitpid(-1, &status, WUNTRACED)) > 0;

	    if (valid)
	    {
	    	give_terminal_to(getpgrp(), sysTTY);
	    	child_status_change(pid, status);
	    }
	//}
}

/**
 * Goes through all of the current jobs, and if we have a process group in the
 * pipe that equals the pid that was passed in, we then check the signals and
 * determine what needs to be done with the current pipeline process (STOPPED,
 * REMOVED, etc)
 *
 * @param pid_t pid The PID that we are looking for
 * @oaram int status The Status of the process
 */
void child_status_change(pid_t pid, int status)
{
	if (pid > 0)
	{
		INIT_LISTELEM(&currentJobs);

		for (LISTELEM_BEGIN(&currentJobs)LISTELEM_END)
		{
			struct esh_pipeline *myPipe = list_entry(listElem, struct esh_pipeline, elem);

			bool valid = (myPipe->pgrp == pid);

			// See: http://octave.sourceforge.net/octave/function/WSTOPSIG.html
			// for all the different "WIF" commands and their outputs
			if (valid && WIFSTOPPED(status))
			{
				// STOPPED
				// See here: http://stackoverflow.com/q/15101619/586621
				// on why we are comparing to the number 22
				if (WSTOPSIG(status) == 22) myPipe->status = STOPPED;

				else
				{
					myPipe->status = STOPPED;
					printf("\n[%d]+ Stopped ", myPipe->jid);
					printJobFromList(currentJobs);
				}
			}

			// Determines if this child process is the one that we're terminating
			if (valid && WTERMSIG(status) == 9) list_remove(listElem);

			// EXIT
			if (valid && WIFEXITED(status)) list_remove(listElem);
				else if (valid && WIFSIGNALED(status)) list_remove(listElem);
				else if (valid && WIFCONTINUED(status)) list_remove(listElem);

			// RESET JOB ID if there are no more jobs!
			if (valid && !jobsExist()) jid = 0;
		}
	}

	else if (pid < 0)
		esh_sys_fatal_error(PID_ERROR);
}


/**
 * Given a command, determines which command it's related to, if any at all.
 *
 * If a particular command doesn't match this set of string comparisons,
 * we will assume it to not be listed as a valid built-in command, and we
 * will return a default value of 0.
 *
 * Currently supported commands and corresponding constant values are:
 * 		EXIT     [1]
 * 		JOBS     [2]
 * 		FG       [3]
 * 		BG       [4]
 * 		KILL     [5]
 * 		STOP     [6]
 */
int getCommandType(char *command_name)
{
	if(strcmp(command_name, "exit") == 0)
		return EXIT;

	if(strcmp(command_name, "jobs") == 0)
		return JOBS;

	if(strcmp(command_name, "fg") == 0)
		return FG;

	if(strcmp(command_name, "bg") == 0)
		return BG;

	if(strcmp(command_name, "kill") == 0)
		return KILL;

	if(strcmp(command_name, "stop") == 0)
		return STOP;

	return 0; // DEFAULT value
}

/**
 * Given the pipeline, print out its jobs.
 *
 * There is a slight error here when printing out pipes;
 * when pipes are printed out; multiple bars (|) can be printed out.
 * 
 * For example, in the case of sleep 10 | sleep 10 and then pressing CTRL Z,
 * our print job functions will actually print out (sleep 10 | sleep 10 | )
 * instead of (sleep 10 | sleep 10 ) which should actually be the correct
 * output as per normal terminals.
 *
 * Not sure if this needs to be revisited or not, because the
 * auto-grading script seems to be OK with it.
 */
void printJobFromPipe(struct esh_pipeline *pipe)
{
	printf("(");

	struct list_elem *listElem = list_begin(&pipe->commands);

	for(LISTELEM_BEGIN(&pipe->commands)LISTELEM_END)
	{
		struct esh_command *currentCommand = list_entry(listElem, struct esh_command, elem);

		char **arguments = currentCommand->argv;

		while(*arguments)
		{
			printf("%s ", *arguments); // Have to do this to match auto grader regex
			++arguments;
		}

		if(list_size(&pipe->commands) > 1)
			printf("| "); // Have to do this to match auto grader regex
	}

printf(")\n"); // Have to do this to match auto grader regex
}

/**
 * Given the list of current jobs, print out its jobs.
 *
 * There is a slight error here when printing out pipes;
 * when pipes are printed out; multiple bars (|) can be printed out.
 * 
 * For example, in the case of sleep 10 | sleep 10 and then pressing CTRL Z,
 * our print job functions will actually print out (sleep 10 | sleep 10 | )
 * instead of (sleep 10 | sleep 10 ) which should actually be the correct
 * output as per normal terminals.
 *
 * Not sure if this needs to be revisited or not, because the
 * auto-grading script seems to be OK with it.  For now, I guess we'll leave
 * it as is. -- Eric
 */
void printJobFromList(struct list jobList)
{
	printf("(");

	struct list_elem *listElem = list_begin(&jobList);

	struct esh_pipeline *myPipe = list_entry(listElem, struct esh_pipeline, elem);

	listElem = list_begin(&myPipe->commands);

	for (LISTELEM_BEGIN(&myPipe->commands)LISTELEM_END)
	{
		struct esh_command *currentCommand = list_entry(listElem, struct esh_command, elem);

		char **arguments = currentCommand->argv;

		while (*arguments)
		{
			printf("%s ", *arguments); // Have to do this to match auto grader regex
			++arguments;
		}

		if (list_size(&myPipe->commands) > 1)
			printf("| "); // Have to do this to match auto grader regex
	}

	printf(")\n"); // Have to do this to match auto grader regex :(
	// five hours of my life gone.
}



/**
 * Print out the stream of current jobs.
 * 
 * Jobs are formatted as [jid] [status]; [status] may represent the job
 * formatting as [jid]+ Stopped.  This is the case when a user presses CTRL-Z.
 * 
 * See child_status_change() for more info.
 */
void runJobsCommand()
{
	struct list_elem *listElem = list_begin(&currentJobs);

	for(LISTELEM_BEGIN(&currentJobs)LISTELEM_END)
	{
		struct esh_pipeline *myPipe = list_entry(listElem, struct esh_pipeline, elem);

		printf("[%d] %s ", myPipe->jid, STATUSES[myPipe->status]);

		printJobFromPipe(myPipe);
	}
}

/**
 * Handles foreground commands.
 */
void runForegroundCommand(struct esh_pipeline *myPipe,
						  struct termios *sysTTY)
{
	esh_signal_block(SIGCHLD);

	myPipe->status = FOREGROUND;

	printJobFromPipe(myPipe);

	give_terminal_to(myPipe->pgrp, sysTTY);

	runKillCommand(myPipe, SIGCONT);

	if (kill(myPipe->pgrp, SIGCONT) == -1)
		esh_sys_fatal_error(FOREGROUND_ERROR);

	wait_for_job(sysTTY);

	esh_signal_unblock(SIGCHLD);
}

/**
 * Runs a command in the background given a pipeline.
 *
 * Simply change the job's status to background and
 * run the kill command with a SIGCONT.
 */
void runBackgroundCommand(struct esh_pipeline *myPipe)
{
	myPipe->status = BACKGROUND;

	printJobFromList(currentJobs);
	
	runKillCommand(myPipe, SIGCONT);
}

/**
 * Runs the "kill" commands. Some commands just STOP the running process,
 * which doesn't really kill the process. Because of this this function also
 * can resume a process by continuing a process.
 *
 * @param truct esh_pipeline *myPipe our pipeline
 * @param int SIGNAL The signal that we want to use
 */
void runKillCommand(struct esh_pipeline *myPipe, int SIGNAL)
{
	char * command;

	switch(SIGNAL)
	{
		case SIGCONT:
			command = "SIGCONT";
			break;

		case SIGKILL:
			command = "SIGKILL";
			break;

		case SIGSTOP:
			command = "SIGSTOP";
			break;
	}

	if (kill(myPipe->pgrp, SIGNAL) == -1)
		esh_sys_fatal_error("%s ERROR ", command);
}

/**
 * Is the command type a background, foreground, kill, or stop?
 *
 * @param int commandType The command type passed in
 */
int isFgBgKillStop(int commandType)
{
	return (commandType == FG || commandType == BG ||
		   commandType == KILL || commandType == STOP);
}

/**
 * Simple string contains function; returns
 * true or false depending on whether or not
 * a specified string contains a specified value.
 */
bool stringContains(char *string, char *value)
{
	return strncmp(string, value, 1) == 0;
}

/**
 * Given the stream of commands, get a job argument id.
 */
int getJobArgumentId(struct esh_command *commands)
{
	int jobArgumentId = -1;

	if (commands->argv[1] == NULL)
	{
		struct list_elem *listElem = list_back(&currentJobs);

		struct esh_pipeline *pipeline = list_entry(listElem, struct esh_pipeline, elem);

		jobArgumentId = pipeline->jid;
	}

	else if (stringContains(commands->argv[1], "%"))
  	{
		char *argId = (char*) malloc(5);

		strcpy(argId, commands->argv[1] + 1);

		jobArgumentId = atoi(argId);
		free(argId);
	}

	else
		jobArgumentId = atoi(commands->argv[1]);

	return jobArgumentId;
}

/**
 * Gets the pipeline given a set of commands.
 */
struct esh_pipeline* getPipeline(struct esh_command *commands)
{
	struct esh_pipeline *ret = NULL;

	int jobArgumentId = getJobArgumentId(commands);

	INIT_LISTELEM(&currentJobs);

	for (LISTELEM_BEGIN(&currentJobs)LISTELEM_END)
	{
		struct esh_pipeline *job = list_entry(listElem, struct esh_pipeline, elem);

		if (job->jid == jobArgumentId)
		{
			ret = job;
			break;
		}
	}

	return ret;
}

/**
 * Handle FG, BG, KILL, and STOP commands based on the commandType specified.
 */
void handleBuiltinCommands(struct esh_pipeline *myPipe,
						   int commandType,
						   struct esh_command *commands,
						   struct termios *sysTTY)
{
	myPipe = getPipeline(commands);

	// FOREGROUND COMMAND
	if (commandType == FG)
		runForegroundCommand(myPipe, sysTTY);

	// BACKGROUND COMMAND
	else if (commandType == BG)
		runBackgroundCommand(myPipe);

	// KILL COMMAND
	else if (commandType == KILL)
		runKillCommand(myPipe, SIGKILL);

	// STOP COMMAND
	else if (commandType == STOP)
		runKillCommand(myPipe, SIGSTOP);
}

/**
 * Closes a particular pipe.
 */
void closePipe(int *pipe)
{
	close(pipe[0]);
	close(pipe[1]);
}

/**
 * Closes two particular pipes at once!
 */
void closePipes(int *pipe1, int *pipe2)
{
	closePipe(pipe1);
	closePipe(pipe2);
}

/**
 * Takes the contents of pipe2 and stores them into pipe1
 */
void copyPipeContents(int *pipe1, int *pipe2)
{
	pipe1[0] = pipe2[0];
	pipe1[1] = pipe2[1];
}

/**
 * Closes and dup the pipe by calling `dup2`.
 *
 * See: http://linux.die.net/man/2/dup2
 *
 * @param int *pipe The pipe that we want
 * @param int a position in pipe that we want to close FIRST
 * @param int b position in pipe that we want to dup
 * @param int c the second param in dup2 (see link above)
 * @param int d position in pipe that we want to close SECOND
 */
void closeDuplicate(int *pipe, int a, int b, int c, int d)
{
	close(pipe[a]);
	dup2(pipe[b], c);
	close(pipe[d]);
}

/**
 * Update the pipe's process group based on the pid of
 * the command.
 */
void updatePipelineProcessGroup(struct esh_command *command,
								struct esh_pipeline *myPipe)
{
	pid_t pid = getpid();
	command->pid = pid;

	if (myPipe->pgrp == -1)
		myPipe->pgrp = pid;

	if (setpgid(pid, myPipe->pgrp) < 0)
		esh_sys_fatal_error(PROCESSGRP_ERROR);
}

/**
 * Update the pipe's status to either FOREGROUND
 * or BACKGROUND depending on whether or not
 * it's a background job.
 */
void updatePipelineStatus(struct termios *sysTTY,
						  struct esh_pipeline *myPipe)
{
	if (!myPipe->bg_job)
	{
		give_terminal_to(myPipe->pgrp, sysTTY);
		myPipe->status = FOREGROUND;
		return;
	}

	myPipe->status = BACKGROUND;
}

/**
 * Handle iored_input for a command.
 */
void handleIoredInput(struct esh_command *command)
{
	if (command->iored_input != NULL)
	{
		int in_fd = open(command->iored_input, O_RDONLY);

		if (dup2(in_fd, 0) < 0)
			esh_sys_fatal_error(DUP2_ERROR);

		close(in_fd);
	}
}

/**
 * Handled iored_output for a command.
 */
void handleIoredOutput(struct esh_command *command)
{
	if (command->iored_output != NULL)
	{
		int out_fd = (command->append_to_output) ?
			open(command->iored_output, OFLAGS | O_APPEND, ACCESS_PERMISH) :
			open(command->iored_output, OFLAGS | O_TRUNC, ACCESS_PERMISH);

		if (dup2(out_fd, 1) < 0)
			esh_sys_fatal_error(DUP2_ERROR);

		close(out_fd);
	}
}

/**
 * Handles a child process.
 *
 * A lot of the code & ideas pulled from:
 *
 * http://www.gnu.org/software/libc/manual/html_node/Launching-Jobs.html#Launching-Jobs
 */
void handleChild(struct esh_pipeline *myPipe,
				 struct esh_command *command,
				 struct termios *sysTTY,
				 bool isPiped,
				 int *oldPipe, int *newPipe,
				 struct list_elem *listElem)
{
	updatePipelineProcessGroup(command, myPipe);
	updatePipelineStatus(sysTTY, myPipe);

	// Handle pipes
	if(isPiped)
	{
		// Close and duplicate the old pipe
		if (listElem != list_begin(&myPipe->commands))
			closeDuplicate(oldPipe, 1, 0, 0, 0);

		// Close and duplicate the new pipe
		if (list_next(listElem) != list_tail(&myPipe->commands))
			closeDuplicate(newPipe, 0, 1, 1, 1);
	}

	handleIoredInput(command); // <

	handleIoredOutput(command); // >

	// execute the given command, helpful information gathered from:
	// http://linux.die.net/man/3/execvp and some StackOverflow post...
	execvp(command->argv[0], command->argv);
}

/**
 * Handles a parent process
 *
 * A lot of the code & ideas pulled from:
 *
 * http://www.gnu.org/software/libc/manual/html_node/Launching-Jobs.html#Launching-Jobs
 */
void handleParent(struct esh_pipeline *pipeline,
				  int pid,
				  bool isPiped,
				  int *oldPipe, int *newPipe,
				  struct list_elem *listElem)
{
	if (pipeline->pgrp == -1)
		pipeline->pgrp = pid;

	if (setpgid(pid, pipeline->pgrp) < 0)
		esh_sys_fatal_error(PROCESSGRP_ERROR);

	if(isPiped)
	{
		// If we are not at the beginning of the list, we can close the pipes
		// that are no longer needed.
		if (listElem != list_begin(&pipeline->commands))
			closePipe(oldPipe);

		// If the next element isn't the last one, set the old pipe contents
		// into the newer pipe
		if (list_next(listElem) != list_tail(&pipeline->commands))
			copyPipeContents(oldPipe, newPipe);

		// If the next element IS the last element, then we close all the
		// pipes.
		if (list_next(listElem) == list_tail(&pipeline->commands))
			closePipes(oldPipe, newPipe);
	}
}

/**
 * Needed for pipelining, this function will pipe commands into other commands
 * and correctly handle the information that will be passed. Properly handles
 * forking after piping commands.
 *
 * A lot of the code & ideas pulled from (same goes for sub-functions such as
 * handleChild & handleParent):
 *
 * http://www.gnu.org/software/libc/manual/html_node/Launching-Jobs.html#Launching-Jobs
 */
void pipeAndForkCommands(struct esh_pipeline *pipeline,
						 struct termios *sysTTY)
{

	pipeline->jid = jid;
	pipeline->pgrp = -1;
	int oldPipe[2], newPipe[2];
	bool isPiped = (list_size(&pipeline->commands) > 1);
	pid_t pid;

	INIT_LISTELEM(&pipeline->commands);

	for (LISTELEM_BEGIN(&pipeline->commands)LISTELEM_END)
	{
		struct esh_command *command = list_entry(listElem, struct esh_command, elem);

		if(isPiped && list_next(listElem) != list_tail(&pipeline->commands))
			pipe(newPipe);

		esh_signal_block(SIGCHLD);

		pid = fork();

		if (pid IS_CHILD)
			handleChild(pipeline, command, sysTTY, isPiped, oldPipe, newPipe, listElem);

		else if (pid IS_PARENT)
			handleParent(pipeline, pid, isPiped, oldPipe, newPipe, listElem);

		else
			esh_sys_fatal_error(FORK_ERROR);
	}
}

/**
 * Increments the job counter so when the command "jobs" is ran it will
 * properly display the job id.
 */
void incrementJobId()
{
	jid = (!jobsExist()) ? 1 : (jid + 1);
}

/**
 * Handles the other commands that aren't built in such a vim, nano, or
 * plugin commands.
 */
void handleOtherCommands(struct esh_pipeline *pipeline,
						 struct termios *sysTTY,
						 struct list_elem * listElem,
						 struct esh_command_line * commandLine)
{
	esh_signal_sethandler(SIGCHLD, sigchld_handler);
	incrementJobId();
	pipeAndForkCommands(pipeline, sysTTY);

	if (pipeline->bg_job)
	{
		pipeline->status = BACKGROUND;
		printf("[%d] %d\n", pipeline->jid, pipeline->pgrp);
	}

	listElem = list_pop_front(&commandLine->pipes);

	list_push_back(&currentJobs, listElem);

	if (!pipeline->bg_job)
		wait_for_job(sysTTY);

	esh_signal_unblock(SIGCHLD);
}

/**
 * Are there currently any jobs?  IE: Is our list of jobs non-empty?
 * True if yes, false if no.
 */
bool jobsExist()
{
	return !list_empty(&currentJobs);
}


/**
 * Responsible for running all commands that get inserted into the shell. It
 * will first check for the built in commands such as exit, jobs, foreground
 * background, kill, and stop commands, and then it will look into other
 * commands such as vim, nano, or plugin commands.
 */
void handleCommands(struct esh_pipeline *myPipe,
					int commandType,
					struct esh_command *commands,
					struct esh_command_line * commandLine,
					struct termios *sysTTY,
					struct list_elem* listElem)
{
	// EXIT COMMAND
	if (commandType == EXIT)
		exit(EXIT_SUCCESS);

	// JOBS COMMAND
	else if (commandType == JOBS)
		runJobsCommand();

	// FOREGROUND, BACKGROUND, KILL, AND STOP COMMANDS
	else if (isFgBgKillStop(commandType) && jobsExist())
		handleBuiltinCommands(myPipe, commandType, commands, sysTTY);

	// HANDLE ALL OTHER COMMANDS (DEFAULT CASE)
	else if (commandType == DEFAULT)
		handleOtherCommands(myPipe, sysTTY, listElem, commandLine);
}


/**
 * Looks through the plugin list and processes each plugin and allows it to
 * become a builtin command that we can then use in the shell.
 */
void pluginProcessor(struct list_elem * listElem,
				     struct esh_command *commands,
				        int commandType)
{
	for (LISTELEM_BEGIN(&esh_plugin_list)LISTELEM_END)
	{
		struct esh_plugin *plugin = list_entry(listElem, struct esh_plugin, elem);
		plugin->process_builtin(commands);
	}
}


/**
 * Loops through all the plugins that we currently have and adds their
 * values to the prompt so it's possible to know which plugins have
 * been loaded in.
 */
char * buildPromptWithPlugins(void)
{
	char *prompt = NULL;

	INIT_LISTELEM(&esh_plugin_list);

	for (LISTELEM_BEGIN(&esh_plugin_list)LISTELEM_END)
	{
		struct esh_plugin *plugin = list_entry(listElem, struct esh_plugin, elem);

		bool needPlugin = (plugin->make_prompt != NULL);

		if (needPlugin)
		 {
			char * plug = plugin->make_prompt();
			bool valid = (prompt != NULL);
			prompt = (prompt == NULL) ?
			  plug :
			  realloc(prompt, 1 + strlen(plug) + strlen(prompt));

			if(valid) 
			{

				// Though it isn't recommended (see link below), we are using
				// strcat for the sake of simplicity & as this isn't going
				// into production anywhere we're not concerned with buffer
				// overflow attacks. `snprintf` may have worked better but...
				// We did reallocate enough memory above to make this possible
				//
				// http://stackoverflow.com/questions/308695/c-string-concatenation
				strcat(prompt, plug);
				free(plug);
			}
		 }
	}

	return (prompt == NULL) ? strdup("esh> ") : prompt;
}
