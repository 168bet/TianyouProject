@echo off
set DIST_APK=%1
cd %CD%
tools\apktool.bat d -f %DIST_APK%