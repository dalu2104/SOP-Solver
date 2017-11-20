@echo off
echo Willkommen beim SOP-Solver.
echo Waehlen Sie bitte eine Testinstanz aus dem Ordner 'SOP-Instanzen':
set /p dateiname=Dateiname inkl. Dateiendung, zB esc07.sop:
java -cp build execution.Exe "%~dp0\SOP-Instanzen\%dateiname%"
echo Zum Beenden:
PAUSE