@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\projeto.ps1" run
set "resultado=%errorlevel%"
if not "%resultado%"=="0" pause
exit /b %resultado%
