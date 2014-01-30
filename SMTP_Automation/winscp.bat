@echo off

REM WinSCP Settings:

set userLogin = eventualName
set scriptLocation=C:\Documents and Settings\user\Desktop\TestingGrounds\trial.txt
set iniLocation=C:\Program Files (x86)\WinSCP\WinSCP.ini
set logLocation=C:\Documents and Settings\user\Desktop\TestingGrounds\ERRORLOG.txt

"C:\Program Files (x86)\WinSCP\winscp.exe" "%userLogin%" /script="%scriptLocation%" /ini="%iniLocation%" /log="%logLocation%"


REM E-mail SMTP Settings:

set emailFrom=user@asdf.com
set emailFromName=Eric Hotinger
set emailTo=user@asdf.com
set smtpServer=smtp.asdf.com
set smtpLogon=user@asdf.com


if errorlevel 1 goto error
set subject=%DATE:~-4%-%DATE:~4,2%-%DATE:~7,2% - Successful Script
set body=We didn't detect any errors, but be sure to look at the log file at %logLocation% anyways. The error log is attached to this email for your convenience.
goto end


:error
set subject=%DATE:~-4%-%DATE:~4,2%-%DATE:~7,2% - Failed Script
set body=We detected errors! Please look at the log file at %logLocation%  The error log is attached to this email for your convenience.
:end

REM Synchronously run the sendEmail vbs in the same window
pushd %~dp0
cscript sendEmail.vbs "%emailFrom%" "%emailFromName%" "%emailTo%" "%smtpServer%" "%smtpLogon%" "%subject%" "%body%" "%logLocation%"

PAUSE