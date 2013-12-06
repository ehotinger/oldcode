#!/usr/bin/python
#
# Input/Output/Append test for the shell. 
# Tests that the shell correctly uses ">>" to append text to the end of a file.
# 
# Requires use of the following commands:
#		echo, cat, >>, exit
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

# run an echo command; do not output the trailing newline (-n).
# place 'banan' in 'fruit'
c.sendline("echo -n banan >> fruit")

# print the contents of the fruit file
c.sendline("cat fruit")

# make sure banan is in the file
assert c.expect("banan") == 0, "Shell did not print expected prompt (banan)"

# add an 'a' to the end of banan to correct the typo
c.sendline("echo a >> fruit")

# print out contents of fruit again
c.sendline("cat fruit")

# now fruit should have banana in it
assert c.expect_exact("banana") == 0, "Shell did not print the expected prompt (banana)"

# remove the fruit file and clean-up.
c.sendline("rm fruit")

# exit
c.sendline("exit")
assert c.expect_exact("exit\r\n") == 0, "Shell output extraneous characters"

shellio.success()
