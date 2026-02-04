@echo off
REM ----------------------------------------------------------------------------
REM Auctioneer API - Application Startup Script (Windows)
REM ----------------------------------------------------------------------------

setlocal

REM Find Java
if defined JAVA_HOME (
    set "JAVACMD=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVACMD=java"
)

REM Verify Java is available
where %JAVACMD% >nul 2>&1
if errorlevel 1 (
    echo Error: Java is not installed or JAVA_HOME is not set correctly.
    exit /b 1
)

REM Get script directory
set "SCRIPT_DIR=%~dp0"

REM Optional JVM options
if not defined JAVA_OPTS set "JAVA_OPTS=-Xms256m -Xmx512m"

echo Starting Auctioneer API...
echo Using Java: %JAVACMD%

REM Build and run with Maven wrapper
if exist "%SCRIPT_DIR%mvnw.cmd" (
    cd /d "%SCRIPT_DIR%"
    call mvnw.cmd spring-boot:run
) else (
    echo Error: Maven wrapper not found. Run from project root.
    exit /b 1
)

endlocal
