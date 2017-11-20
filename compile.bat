mkdir build
mkdir build
javac -d build "%~dp0\SOP-Solver\src\convertSOPFileToArray\parser.java"
javac -d build "%~dp0\SOP-Solver\src\validSolution\GreedySOP.java"
javac -d build "%~dp0\SOP-Solver\src\validSolution\OneSolution.java"
javac -d build "%~dp0\SOP-Solver\src\validSolution\Simple.java"
javac -d build -cp build "%~dp0\SOP-Solver\src\tryAll\Permutations.java"
javac -d build "%~dp0\SOP-Solver\src\tryAll\recursiveBruteForce.java"

javac -d build -cp build "%~dp0\SOP-Solver\src\execution\Exe.java"
