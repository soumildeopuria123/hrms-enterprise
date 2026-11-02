@echo off
rem
rem Maven2 Start Script for Windows
rem

set "PRG=%~dp0%%~nx0"

if exist "%PRG_DIR%\.mvn\wrapper\maven-wrapper.jar" (
    exec "%JAVA_HOME%\bin\java.exe" %*
    -classpath "%PRG_DIR%\.mvn\wrapper\maven-wrapper.jar"
    org.apache.maven.wrapper.MavenWrapperMain %*
) else (
    echo Cannot find maven-wrapper.jar >&2
    exit /b 1
)
