mkdir build
javac -d build "%~dp0\SOP-Solver\src\convertSOPFileToArray\parser.java"

javac -d build "%~dp0\SOP-Solver\src\execution\TimeStartAndStop.java"
javac -d build "%~dp0\SOP-Solver\src\execution\ExeTimeSolutionCost.java"

javac -d build "%~dp0\SOP-Solver\src\validSolution\GreedySOP.java"
javac -d build "%~dp0\SOP-Solver\src\validSolution\OneSolution.java"
javac -d build "%~dp0\SOP-Solver\src\validSolution\Simple.java"

javac -d build -cp build "%~dp0\SOP-Solver\src\tryAll\Permutations.java"
javac -d build "%~dp0\SOP-Solver\src\tryAll\recursiveBruteForce.java"

javac -d build -cp build "%~dp0\SOP-Solver\src\simulatedAnnealing\Utility.java"
javac -d build -cp build "%~dp0\SOP-Solver\src\simulatedAnnealing\Sa.java"

javac -d build -cp build "%~dp0\SOP-Solver\src\genetic\UsefulMethods.java"
javac -d build -cp build "%~dp0\SOP-Solver\src\genetic\GenAlg.java"
javac -d build -cp build "%~dp0\SOP-Solver\src\genetic\StartGenAlg.java"

javac -d build -cp build "%~dp0\SOP-Solver\src\dynamicProgramming\Tour.java"
javac -d build -cp build "%~dp0\SOP-Solver\src\dynamicProgramming\DynamicSOP.java"
javac -d build -cp build "%~dp0\SOP-Solver\src\dynamicProgramming\DynamicSOPLists.java"
javac -d build -cp build "%~dp0\SOP-Solver\src\dynamicProgramming\DynamicSOPSolver.java"

javac -d build -cp build "%~dp0\SOP-Solver\src\execution\Exe.java"