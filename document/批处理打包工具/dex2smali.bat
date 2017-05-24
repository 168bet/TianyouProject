@echo off
set DIST_APK=%1
cd %CD%
java -jar ..\tools\baksmali.jar %DIST_APK%