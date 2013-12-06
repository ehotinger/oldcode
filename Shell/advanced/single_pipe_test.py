#!/usr/bin/python
#
# Single pipe test for the shell. 
# Tests that the shell correctly uses "|" to pipe together commands.
# 
# Requires use of the following commands:
#		echo, cat, |, wc
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

# test for single pipe; print out number of words in the echo
c.sendline("echo mangos kiwis oranges | wc -w")

# make sure it's 3
assert c.expect_exact("3") == 0, "Shell did not print expected prompt (3)"

# print out length of longest line in the sentence
c.sendline("echo mangos kiwis oranges | wc -L")

# make sure it's 20
assert c.expect_exact("20") == 0, "Shell did not print expected prompt (20)"

# exit
c.sendline("exit")
assert c.expect_exact("exit\r\n") == 0, "Shell output extraneous characters"

shellio.success()
