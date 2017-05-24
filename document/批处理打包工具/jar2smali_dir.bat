@echo off
set DIST_APK=%1
cd %CD%
java -jar ..\tools\dx.jar --dex --output classes.dex %DIST_APK%\*.jar
java -jar ..\tools\baksmali.jar classes.dex