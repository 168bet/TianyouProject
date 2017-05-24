@echo off
set BASE_DIR=%~dp0
set DIST_APK=%1
cd %CD%

jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore %BASE_DIR%tools\tianyouxi.keystore -storepass tianyouxi -keypass tianyouxi -signedjar %DIST_APK% %DIST_APK% tianyouxi