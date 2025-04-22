@echo off
setlocal enabledelayedexpansion

echo Cleaning previous app image...
if exist gtdFX (
    rmdir /s /q gtdFX
)

echo Building the project...
call .\mvnw.cmd clean package

echo Packaging with jpackage...
jpackage ^
    --input target ^
    --name gtdFX ^
    --module-path "target\libs;target\classes" ^
    --module com.melliott.gtdfx.gtdclient/com.melliott.gtdfx.gtdclient.GtdApplication ^
    --type app-image ^
    --app-version 0.2.0 ^
    --java-options "-Dorg.slf4j.simpleLogger.logFile=log"

echo Done!