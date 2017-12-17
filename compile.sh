mkdir build
javac -d build $PWD/SOP-Solver/src/convertSOPFileToArray/parser.java
javac -d build $PWD/SOP-Solver/src/execution/TimeStartAndStop.java
javac -d build $PWD/SOP-Solver/src/simulatedAnnealing/ExecutionTimeAndSolution.java

javac -d build $PWD/SOP-Solver/src/validSolution/GreedySOP.java
javac -d build $PWD/SOP-Solver/src/validSolution/OneSolution.java
javac -d build $PWD/SOP-Solver/src/validSolution/Simple.java
javac -d build -cp build $PWD/SOP-Solver/src/tryAll/Permutations.java
javac -d build $PWD/SOP-Solver/src/tryAll/recursiveBruteForce.java
javac -d build -cp build $PWD/SOP-Solver/src/simulatedAnnealing/sa.java

javac -d build -cp build $PWD/SOP-Solver/src/execution/Exe.java
