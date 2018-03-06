@echo off

set JAVA_HOME="%JAVA_HOME%"

echo JAVA_HOME=%JAVA_HOME%

cd /d %~dp0

echo WORKING_DIRECTORY=%~dp0

start "run" "%JAVA_HOME%\bin\javaw" -Xms10m -Xmx15m -cp %~dp0\target\mouse-informer.jar "ru.ezhov.mouseinformer.App"