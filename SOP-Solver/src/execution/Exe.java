package execution;

import java.io.*;
import java.util.List;
import convertSOPFileToArray.parser;
import genetic.StartGenAlg;
import simulatedAnnealing.ExecutionTimeAndSolution;
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
	public static void main(String[] args) throws NumberFormatException, IOException {
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
		int[][] matrix = parser.parse(args[0]);
		
		
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
			System.out.println("6 - Simulated Annealing.");
			System.out.println("7 - Genetic Algorithm.");

			System.out.println("Other options:");
			System.out.println("8 - Exit programm.");

			int n = Integer.parseInt(br.readLine());

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
				//Time tracking for SA happens internally, due to expanded user input.
				ExecutionTimeAndSolution saSol = sa.simulatedAnnealing(matrix);
				solution = saSol.getSolution();
				elapsedTime = saSol.getTimeForExecution();
				break;
			case 7:
				startTime = TimeStartAndStop.startTime();
				solution = StartGenAlg.runAlg(matrix);
				elapsedTime = TimeStartAndStop.stopTime(startTime);
				if(solution == null){
					//the algorithm did not find a valid solution (maybe there is none)
					System.out.println("The Algorithm didn't find a valid solution in Time: " + elapsedTime + "ms.");
					return;
				}
				break;
			case 8:
				return;
			default:
				System.out.println("Entered invalid number");
				return;
			}

			// Printing the solution
			cost = calculateCost(matrix, solution);
			printSolution(solution, cost, elapsedTime);
		
			//continue?
			System.out.println("Continue? y/n");
			String str = br.readLine();
			
			if(!str.equals("y")){
				return;
			}
		}
	}

	/* ________________________HELPING METHODS__________________________ */
	/**
	 * Calculates the cost of the given solution with the given matrix.
	 * Excluding the Start and Stop vertex.
	 */
	private static int calculateCost(int[][] matrix, List<Integer> solution) {
		int cost = 0;

		// To size-1 because we don't travel anywhere from the last vertex;
		for (int i = 0; i < solution.size() - 1; i++) {
			// we have got to fix the solution index since the matrix starts at
			// 0, but the instances numbering starts at 1.
			cost += matrix[solution.get(i) - 1][solution.get(i + 1) - 1];
		}
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
		for (int x : solution) {
			System.out.print(" - " + x);
		}
		// n is always the end vertex.
		System.out.print(" - " + (solution.size() + 2) + "\n");
		System.out.println("With cost:" + cost);
		System.out.println("In time: " + elapsedTime + "ms");
	}
}
