#!/bin/bash
echo Willkommen beim SOP-Solver.
echo Waehlen Sie bitte eine Testinstanz aus dem Ordner 'SOP-Instanzen':
read -p "Dateiname inkl. Dateiendung, zB esc07.sop:" dateiname
java -cp build execution.Exe $PWD/SOP-Instanzen/$dateiname