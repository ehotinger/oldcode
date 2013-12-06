#!/usr/bin/python
#
# Input/Output/Multi-pipe test for the shell. 
# Tests that the shell correctly uses multiple "|" to pipe commands together.
# 
# Requires use of the following commands:
#		echo, cat, ls, grep, wc, rm, multiple |
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

# put "apple" in a fruit file; -n removes newline character
c.sendline("echo -n apple > fruit")

# print contents of fruit file
c.sendline("cat fruit")

# it should be equal to apple
assert c.expect("apple") == 0, "Shell did not print expected prompt (apple)"

# pipe ls, grep, and wc together on the fruit file
c.sendline("ls | grep fruit | wc -w")

# should output the number of words with name fruit (only 1)
assert c.expect("1") == 0, "Shell did not print expected prompt (1)"

# remove fruit file
c.sendline("rm fruit")

# exit
c.sendline("exit")
assert c.expect_exact("exit\r\n") == 0, "Shell output extraneous characters"