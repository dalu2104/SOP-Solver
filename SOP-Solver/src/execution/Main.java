package execution;

import java.io.*;
import java.util.List;
import convertSOPFileToArray.parser;
import validSolution.Simple;

/**
 * Main Class. Executes the programm, lets the user choose the algorithm and
 * takes the time the algorithm takes to finish.
 *
 */
public class Main {

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
		// parse the file given as an argument when the programm was exucuted
		int[][] matrix = parser.parse(args[0]);

		// Letting the User choose the algorithm.
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(in);
		System.out.println("Choose an algorithm by number:");
		System.out.println("1 - Simple valid solution algorithm.");
		int n = Integer.parseInt(br.readLine());
		switch (n) {
		case 1:
			solution = Simple.firstIdea(matrix);
			break;
		default:
			System.out.println("Entered invalid number");
			return;
		}

		// Printing the solution
		int cost = calculateCost(matrix, solution);
		printSolution(solution, cost);
	}

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
	 */
	private static void printSolution(List<Integer> solution, int cost) {
		System.out.println("Tour is:");
		// 1 is always the start vertex.
		System.out.print("1");
		for (int x : solution) {
			System.out.print(" - " + x);
		}
		// n is always the end vertex.
		System.out.print(" - " + (solution.size() + 2) + "\n");
		System.out.println("With cost:" + cost);
	}
}
