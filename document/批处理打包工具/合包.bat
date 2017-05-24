@echo off
set DIST_APK=%1
cd %CD%
tools\apktool.bat b -f %DIST_APK%