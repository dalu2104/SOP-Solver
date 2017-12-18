@echo off
echo Willkommen beim SOP-Solver.
:FILE
echo Waehlen Sie bitte eine Testinstanz aus dem Ordner 'SOP-Instanzen':
set /p dateiname=Dateiname inkl. Dateiendung, zB esc07.sop:
java -cp build execution.Exe "%~dp0\SOP-Instanzen\%dateiname%"
set /p neueDatei=Neue Datei ausfuehren? y/n
if /i %neueDatei%=="y" goto FILE
echo Zum Beenden:
PAUSE