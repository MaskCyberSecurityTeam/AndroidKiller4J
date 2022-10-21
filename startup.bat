@echo off
setlocal enabledelayedexpansion
@set classpath=.
@for %%c in (libs\*.jar) do (
@set classpath=!classpath!;%%c
)
jre\bin\java.exe -cp %classpath% com.richardtang.androidkiller4j.Application
pause