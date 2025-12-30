@echo off
REM Change directory to the location of this batch file
cd /d %~dp0

REM Compile all Java files
javac --module-path "C:\javafx\lib" --add-modules javafx.controls,javafx.fxml Module_1\*.java Module_2\*.java Module_3\*.java Module_4\*.java Module_5\*.java

IF %ERRORLEVEL% NEQ 0 (
    echo Compilation failed.
    pause
    exit /b
)

REM Run the application
java --module-path "C:\javafx\lib" --add-modules javafx.controls,javafx.fxml Module_5.MainApp

pause
