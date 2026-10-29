@echo off
setlocal
title Smart Canteen Token System - Kalasalingam Academy

set "APP_DIR=%~dp0"
set "JAVA_HOME=%APP_DIR%.jdk\jdk-17.0.12+7"
set "M2_HOME=%APP_DIR%.maven\apache-maven-3.9.6"
set "PATH=%JAVA_HOME%\bin;%M2_HOME%\bin;%PATH%"

echo ================================================================
echo   Smart Canteen Token System
echo   Kalasalingam Academy of Research and Education
echo   Dept of CSE - Cyber Security
echo ================================================================
echo.
echo Java Version:
"%JAVA_HOME%\bin\java.exe" -version
echo.

cd /d "%APP_DIR%"
call "%M2_HOME%\bin\mvn.cmd" spring-boot:run

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Server stopped or encountered an error.
    pause
)
