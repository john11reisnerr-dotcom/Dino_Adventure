@echo off
cd /d "%~dp0"
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0JOGAR.ps1"
set "resultado=%errorlevel%"
if not "%resultado%"=="0" pause
exit /b %resultado%
