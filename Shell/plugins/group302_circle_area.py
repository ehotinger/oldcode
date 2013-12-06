#!/usr/bin/python
#
# Circle area plugin test for the shell!
# @author Eric Hotinger
#
# Requires use of the following command:
#		factorial
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

# print out circle area of 9
c.sendline("circlearea 9")

# and make sure it's equal to 254
assert c.expect_exact("254") == 0, "Shell did not print expected prompt (254)"

# print out circle area of 0
c.sendline("circlearea 0")

# and make sure it's equal to 0
assert c.expect_exact("0") == 0, "Shell did not print expected prompt (0)"

# exit
c.sendline("exit")
assert c.expect_exact("exit\r\n") == 0, "Shell output extraneous characters"

shellio.success()
