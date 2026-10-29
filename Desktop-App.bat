@echo off
title KARE Smart Canteen Desktop App
echo Starting KARE Smart Canteen Desktop Application...

REM Check if the server is running on port 8080
netstat -ano | findstr :8080 >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Starting Canteen Server in background...
    start /min "CanteenServer" run.bat
    timeout /t 6 /nobreak >nul
)

REM Open as a standalone native desktop application window
where msedge >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    start msedge --app=http://localhost:8080
    goto end
)

where chrome >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    start chrome --app=http://localhost:8080
    goto end
)

start http://localhost:8080

:end
exit
