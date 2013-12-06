#!/usr/bin/python

#
# This file contains definitions that describe the output of your esh.
#
# You may adapt all settings in this file to match the output of your
# shell. (Alternatively, you may write your shell to match these templates.)
#

# the shell executable.
shell = "./esh"

# the prompt printed by your shell
prompt = "esh>"

#
# a regexp matching the message printed when a job is sent into the background
# must capture (jobid, processid)
#
bgjob_regex = "\[(\d+)\] (\d+)"

#
# a regexp matching a job status when printed using the 'jobs' command
# must capture (jobid, jobstatus, commandline)
#
job_status_regex =  "\[(\d+)\].?\s+(\S+)\s+\((.+?)\)\r\n"

#
# job status messages
#
jobs_status_msg = {
    'stopped' 		: "Stopped",
    'running' 		: "Running"
}

#
# builtin commands
#
# Use printf-style formats. stop, kill, fg, and bg expect job ids.
# If your shell requires a % before the jobid, use %%%s.
#
builtin_commands = {
	'exit' : 'exit',
    'jobs' : 'jobs',
    'stop' : 'stop %s',
    'kill' : 'kill %s',
    'fg'   : 'fg %s',
    'bg'   : 'bg %s'
}
