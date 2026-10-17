@echo off
setlocal EnableExtensions
cd /d "%~dp0"
title KYRO VISUALS - Build Mod

echo ==========================================
echo   KYRO VISUALS - JAR BUILDER
echo ==========================================
echo.

where java >nul 2>nul
if errorlevel 1 (
  echo ERROR: Java was not found. Install Java 25 and reopen this window.
  pause
  exit /b 1
)

for /f "tokens=3" %%V in ('java -version 2^>^&1 ^| findstr /i "version"') do set JAVA_VERSION=%%V
echo Detected Java: %JAVA_VERSION%
echo.

set "GRADLE_VERSION=9.2.1"
set "TOOLS_DIR=%CD%\.tools"
set "GRADLE_HOME=%TOOLS_DIR%\gradle-%GRADLE_VERSION%"
set "GRADLE_ZIP=%TOOLS_DIR%\gradle-%GRADLE_VERSION%-bin.zip"

if not exist "%GRADLE_HOME%\bin\gradle.bat" (
  if not exist "%TOOLS_DIR%" mkdir "%TOOLS_DIR%"
  echo Downloading Gradle %GRADLE_VERSION%... This requires internet access.
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%GRADLE_ZIP%'"
  if errorlevel 1 (
    echo.
    echo ERROR: Gradle could not be downloaded. Check your internet connection.
    pause
    exit /b 1
  )
  echo Extracting Gradle...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath '%GRADLE_ZIP%' -DestinationPath '%TOOLS_DIR%' -Force"
  if errorlevel 1 (
    echo ERROR: Could not extract Gradle.
    pause
    exit /b 1
  )
)

echo.
echo Building KYRO VISUALS... The first build may take a while.
echo.
call "%GRADLE_HOME%\bin\gradle.bat" --no-daemon build
if errorlevel 1 (
  echo.
  echo BUILD FAILED. Copy the error text above or take a screenshot and send it to ChatGPT.
  pause
  exit /b 1
)

echo.
echo ==========================================
echo BUILD SUCCESSFUL!
echo Your mod JAR should be in:
echo %CD%\build\libs\
echo ==========================================
start "" "%CD%\build\libs"
pause
