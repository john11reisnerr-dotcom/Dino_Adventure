@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\projeto.ps1" verify
set "resultado=%errorlevel%"
pause
exit /b %resultado%
