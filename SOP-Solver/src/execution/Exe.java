package execution;

import java.io.*;
import java.util.List;
import convertSOPFileToArray.parser;
import dynamicProgramming.DynamicSOP;
import genetic.StartGenAlg;
import simulatedAnnealing.sa;
import tryAll.recursiveBruteForce;
import tryAll.Permutations;
import validSolution.GreedySOP;
import validSolution.OneSolution;
import validSolution.Simple;

/**
 * Main Class. Executes the program, lets the user choose the algorithm and
 * takes the time the algorithm takes to finish.
 *
 */
public class Exe {

	/**
	 * Main method.
	 * 
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static void main(String[] args) {
		// check for input
		if (args.length != 1) {
			System.out.println("Error with your given input.");
			return;
		}

		// initialization
		List<Integer> solution;
		int cost = 0;
		long startTime = 0;
		long elapsedTime = 0;
		// parse the file given as an argument when the program was executed
		int[][] matrix;
		try {
			matrix = parser.parse(args[0]);
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + args[0]);
			return;
		}

		while (true) {
			// Letting the User choose the algorithm.
			InputStreamReader in = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(in);
			System.out.println("Choose an algorithm by number:");

			System.out.println("Valid solutions:");
			System.out.println("1 - Intuitive algorithm.");
			System.out.println("2 - Greedy algorithm I.");
			System.out.println("3 - Greedy algorithm II.");

			System.out.println("Optimal solution:");
			System.out.println("4 - Recursive brute-force algorithm.");
			System.out.println("5 - Recursive brute-force algorithm II.");

			System.out.println("Advanced algorithms:");
			System.out.println("6 - Simulated Annealing (default parameters).");
			System.out.println("7 - Simulated Annealing (user-input parameters).");
			System.out.println("8 - Genetic Algorithm.");
			System.out.println("9 - Dynamic Programming.");

			System.out.println("Other options:");
			System.out.println("Any other key - Exit programm.");

			int n;
			try {
				n = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				// Catch the any other key option by setting n to a value not
				// mentioned in the switch case.
				n = Integer.MAX_VALUE;
			}

			// Executing algorithm according to user and calculating execution
			// time.
			switch (n) {
			case 1:
				startTime = TimeStartAndStop.startTime();
				solution = Simple.firstIdea(matrix);
				elapsedTime = TimeStartAndStop.stopTime(startTime);
				break;
			case 2:
				startTime = TimeStartAndStop.startTime();
				solution = OneSolution.findSolution(matrix);
				elapsedTime = TimeStartAndStop.stopTime(startTime);
				break;
			case 3:
				startTime = TimeStartAndStop.startTime();
				solution = GreedySOP.greedy(matrix);
				elapsedTime = TimeStartAndStop.stopTime(startTime);
				break;
			case 4:
				startTime = TimeStartAndStop.startTime();
				solution = recursiveBruteForce.perfectResult(matrix);
				elapsedTime = TimeStartAndStop.stopTime(startTime);
				break;
			case 5:
				startTime = TimeStartAndStop.startTime();
				solution = Permutations.checkAllPossibilities(matrix);
				elapsedTime = TimeStartAndStop.stopTime(startTime);
				break;
			case 6:
				// Time tracking for SA happens internally, due to expanded user
				// input.
				ExeTimeSolutionCost saSol1 = sa.simulatedAnnealing(matrix, true);
				solution = saSol1.getSolution();
				elapsedTime = saSol1.getTimeForExecution();
				if (solution == null) {
					// the algorithm did not find a valid solution (maybe there
					// is none)
					System.out.println("The Algorithm didn't find a valid solution in Time: " + elapsedTime + "ms.");
				}
				break;
			case 7:
				// Time tracking for SA happens internally, due to expanded user
				// input.
				ExeTimeSolutionCost saSol2 = sa.simulatedAnnealing(matrix, false);
				solution = saSol2.getSolution();
				elapsedTime = saSol2.getTimeForExecution();
				if (solution == null) {
					// the algorithm did not find a valid solution (maybe there
					// is none)
					System.out.println("The Algorithm didn't find a valid solution in Time: " + elapsedTime + "ms.");
				}
				break;
			case 8:
				startTime = TimeStartAndStop.startTime();
				solution = StartGenAlg.runAlg(matrix);
				elapsedTime = TimeStartAndStop.stopTime(startTime);
				if (solution == null) {
					// the algorithm did not find a valid solution (maybe there
					// is none)
					System.out.println("The Algorithm didn't find a valid solution in Time: " + elapsedTime + "ms.");
				}
				break;
			case 9:
				startTime = TimeStartAndStop.startTime();
				solution = DynamicSOP.solveDynamic(matrix);
				elapsedTime = TimeStartAndStop.stopTime(startTime);
				break;
			default:
				return;
			}

			// Printing the solution, if a solution was found
			if (solution != null) {
				cost = calculateCost(matrix, solution);
				printSolution(solution, cost, elapsedTime);
			}

			// continue?
			System.out.println("Continue with an algorithm on this test instance? y/n");
			String str = "";
			try {
				str = br.readLine();
			} catch (IOException e) {
				System.out.println("An Error occured.");
			}

			if (!str.equals("y")) {
				return;
			}
		}
	}

	/* ________________________HELPING METHODS__________________________ */
	/**
	 * Calculates the cost of the given solution with the given matrix. Solution
	 * should contain indices according to array logic, where 0 is the start
	 * vertex and n-1 is the end vertex. Given list should exclude the Start and
	 * Stop vertex but the distances from the start vertex to the first vertex
	 * of the list and from the last vertex of the list to the end vertex are
	 * added to the cost as well.
	 */
	private static int calculateCost(int[][] matrix, List<Integer> solution) {
		int cost = 0;
		// distance from the start vertex to the first vertex of the list
		cost += matrix[0][solution.get(0)];
		// distances between the vertices in the list
		// To size-1 because we don't travel anywhere from the last vertex;
		for (int i = 0; i < solution.size() - 1; i++) {
			// Indices in solution should be according to array logic, where 0
			// is the start vertex and n-1 is the end vertex.
			cost += matrix[solution.get(i)][solution.get(i + 1)];
		}
		// distance from the last vertex of the list to the end vertex
		cost += matrix[solution.get(solution.size() - 1)][matrix[0].length - 1];
		return cost;
	}

	/**
	 * Prints the given solution;
	 * 
	 * @param elapsedTime
	 */
	private static void printSolution(List<Integer> solution, int cost, long elapsedTime) {
		System.out.println("Tour is:");
		// 1 is always the start vertex.
		System.out.print("1");
		int toPrint;
		for (int x : solution) {
			toPrint = x + 1;
			System.out.print(" - " + toPrint);
		}
		// n is always the end vertex.
		System.out.print(" - " + (solution.size() + 2) + "\n");
		System.out.println("With cost:" + cost);
		System.out.println("In time: " + elapsedTime + "ms");
	}
}
