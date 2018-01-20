# SOP-Solver
SOP Solver project for the Module 'Bachelorprojekt' at the group 'Algorithmen and Komplexitaet' at University of Kiel.

1) `convertSOPFileToArray` contains the parser that reads a given SOP file and converts it into an array[][] that is used to calculate the solutions. In order to write your own instances, it is important to include the header lines: `DIMENSION:` with the correct Integer behind it, and EDGE_WEIGHT_SECTION with a random integer behind it. Followed by the matrix you want to have solved. This is important, parser will not work otherwise.
2) `dynamicProgramming` contains code to find a solution with the Dynamic Programming method.
3) `execution` contains the class that contains the Main-method `Exe.java`. Also contains some Utility classes. One that calculates the systems time to determine the runtime of algorithms and one that creates and object that is used to save cost and solution in an shared object.
4) `genetic` containts code to find a solution with the Genetic Algorithmn method. Splitted into three classes. `StartGenAlg.java` is a wrapper that controls the start and iterations of the algorithmn. `GenAlg.java`  is the algorithmn itself. `UsefulMethods.java` contains some often used methods.
5) `simulatedAnealing` containts code to find a solution with the Simulated Annealing method. `Sa.java` contains a method to start and control the algorithmn. Two methods according to the choice between user-input and default parameters, and some methods that are needed for SA itself. Also `Utility.java` contains useful methods that help calculate the solution but don't belong to the SA algorithm.
6) `tryAll` contains some implementations of Brute-Force-algorithmns to find an optimal solution.
7) `validSolution` contains some implementations of Greedy-algorithms to find a valid solution.

Contact: stu124145@mail.uni-kiel.de

