#!/usr/bin/python
#
# Input/Output/In-Redirection test for the shell.
# Tests that the shell correctly uses "<" to direct input.
#
# Requires use of the following commands:
#		vim, cat, <, exit
#
# This file is based on the /basic/ test cases provided to us:
# 		bg_test, ctrl-c_test, ctrl-z_test, fg_test, jobs_test, kill_test, and stop_test
#
#
import sys, imp, atexit
sys.path.append("/home/courses/cs3214/software/pexpect-dpty/");
import pexpect, shellio, signal, time, os, re, proc_check

# Ensure the shell process is terminated
def force_shell_termination(shell_process):
	c.close(force=True)

# pulling in the regular expression and other definitions
definitions_scriptname = sys.argv[1]
def_module = imp.load_source('', definitions_scriptname)
logfile = None
if hasattr(def_module, 'logfile'):
    logfile = def_module.logfile

# spawn an instance of the shell
c = pexpect.spawn(def_module.shell, drainpty=True, logfile=logfile)
atexit.register(force_shell_termination, shell_process=c)

# set default timeout to 2 seconds
c.timeout = 2

# ensure that shell prints expected prompt
assert c.expect(def_module.prompt) == 0, "Shell did not print expected prompt"

# put strawberry in a fruit file
c.sendline("echo -n strawberry > fruit")

# print out the fruit file
c.sendline("cat < fruit")

# and make sure it's equal to strawberry
assert c.expect_exact("strawberry") == 0, "Shell did not print expected prompt (strawberry)"

# exit
c.sendline("exit")
assert c.expect_exact("exit\r\n") == 0, "Shell output extraneous characters"

shellio.success()