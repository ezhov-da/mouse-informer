@echo off

chcp1251

set JAVA_HOME="%JAVA_HOME%"

echo JAVA_HOME=%JAVA_HOME%

cd /d %~dp0

echo WORKING_DIRECTORY=%~dp0

set /p PID=<pid.txt

echo KILL PID=%PID%

TASKKILL /PID %PID% /F