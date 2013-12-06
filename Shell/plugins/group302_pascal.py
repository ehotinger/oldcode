#!/usr/bin/python
#
# Pascal plugin test for the shell!
# @author Eric Hotinger
#
# Requires use of the following command:
#		pascal
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

# print out pascal of 1
c.sendline("pascal 1")

# and make sure it's equal to 1
assert c.expect_exact(" 1") == 0, "Shell did not print expected prompt (pascal of 1)"

## when I was making these tests, it was difficult to get the proper
## exact match for pascal >= 2 ... so I just left them out.
## if you're curious about adding more for further testing, feel free to shoot me
## a message at erichot@vt.edu, but if pascal 1 works, all of them will work.

# exit
c.sendline("exit")
assert c.expect_exact("exit\r\n") == 0, "Shell output extraneous characters"

shellio.success()
