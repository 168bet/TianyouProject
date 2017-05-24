@echo off
set DIST_APK=%1
cd %CD%
java -jar ..\tools\dx.jar --dex --output %DIST_APK%.dex %DIST_APK%