package simulatedAnnealing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import execution.TimeStartAndStop;
import validSolution.Simple;

/**
 * Class that offers a static method that calculates a good solution for a given
 * SOP problem. This method uses simulated Annealing to determine an optimal
 * solution.
 * 
 * @author Daniel Lucas
 *
 */
public class sa {
	private static int[][] A;
	private static Random generator = new Random();

	/**
	 * Returns optimal solution for given matrix and asks the user for his input
	 * 
	 * @param matrix
	 *            Given matrix for the SOP problem.
	 * @return A valid good solution.
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static ExecutionTimeAndSolution simulatedAnnealing(int[][] matrix) throws NumberFormatException, IOException {
		A = matrix;
		List<Integer> solution = null;
		long startTime = 0;
		long elapsedTime = 0;
		ExecutionTimeAndSolution returner = null;
		
		// input prep
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(in);
		System.out.println("Please choose by entering a number:");
		System.out.println("1 - Default parameters.");
		System.out.println("2 - Choice of parameters.");

		int n = Integer.parseInt(br.readLine());

		// Executing algorithm according to user and calculating execution
		// time.
		switch (n) {
		case 1:
			// execution and stopping of execution time.
			startTime = TimeStartAndStop.startTime();
			solution = simulatedAnnealing1();
			elapsedTime = TimeStartAndStop.stopTime(startTime);
			break;
		case 2:
			// Choosing parameters
			// Temperature
			System.out.println("Enter temperature.");
			double temp = Double.parseDouble(br.readLine());

			// temperature decrement
			System.out.println("Enter temperature decrementation.");
			double tempDecr = Double.parseDouble(br.readLine());

			// iterations
			System.out.println("Enter amount of iterations.");
			int itera = Integer.parseInt(br.readLine());
			
			//execution and stopping of execution time.
			startTime = TimeStartAndStop.startTime();
			solution = simulatedAnnealing2(temp, tempDecr, itera);
			elapsedTime = TimeStartAndStop.stopTime(startTime);
			break;
		default:
			System.out.println("Invalid input.");
			return returner;
		}
		//preping the solution and returning it
		returner = new ExecutionTimeAndSolution();
		returner.setSolution(solution);
		returner.setTimeForExecution(elapsedTime);
		
		return returner;
	}

	//DEFAULT MODE SA METHOD
	/**
	 * Default mode, no user input for parameters. Returns a optimal solution as
	 * a list according to the given restrictions inside the matrix.
	 * 
	 * @return An optimal tour as List of Integers corresponding to the vertices
	 *         in the matrix.
	 */
	private static List<Integer> simulatedAnnealing1() {
		// in Order to start, we need a valid solution
		// BEWARE S DOES NOT CONTAIN START AND END VERTEX.
		// getting a valid solution for start.
		List<Integer> s0 = Simple.firstIdea(A);
		s0 = fixIndexStart(s0);
		List<Integer> s1 = null;
		double T = 500;
		int kmax = 1000;

		for (int k = 0; k < kmax; k++) {
			T = temperature(T, true, 0);
			s1 = randomNeighbour(s0);
			if (P(cost(s0), cost(s1), T) >= generator.nextDouble()) {
				s0 = copyList(s1);
			}
		}

		s0 = fixIndexEnd(s0);
		return s0;
	}

	//USER INPUT SA METHOD
	/**
	 * Called with user input parameters. Returns a optimal solution as a list
	 * according to the given restrictions inside the matrix.
	 * 
	 * @return An optimal tour as List of Integers corresponding to the vertices
	 *         in the matrix.
	 */
	private static List<Integer> simulatedAnnealing2(double userTemp, double userTempDecr, int userMaxIt) {
		// in Order to start, we need a valid solution
		// BEWARE S DOES NOT CONTAIN START AND END VERTEX.
		// getting a valid solution for start.
		List<Integer> s0 = Simple.firstIdea(A);
		s0 = fixIndexStart(s0);
		List<Integer> s1 = null;
		double T = userTemp;
		int kmax = userMaxIt;

		for (int k = 0; k < kmax; k++) {
			T = temperature(T, false, userTempDecr);
			s1 = randomNeighbour(s0);
			if (P(cost(s0), cost(s1), T) >= generator.nextDouble()) {
				s0 = copyList(s1);
			}
		}

		s0 = fixIndexEnd(s0);
		return s0;
	}

	/*
	 * ____________HELPING METHODS (SA RELATED)____________________________
	 */
	/**
	 * Returns probability to switch to given solution S1, depending on the
	 * temperature, cost of S0 and cost of S1.
	 * 
	 * @param cost0
	 *            Cost of the current solution S0.
	 * @param cost1
	 *            Cost of the possible solution S1.
	 * @param T
	 *            Current given temperature
	 * @return The probability.
	 */
	private static double P(int cost0, int cost1, double T) {
		if (T == 0 && cost1 < cost0) {
			return 1;
		} else if (T == 0) {
			return 0;
		} else if (cost1 < cost0) {
			return 1;
		} else {
			return Math.exp((cost1 - cost0) / T);
		}
	}

	/**
	 * Decrements the temperature T.
	 * 
	 * @param T
	 * @param defaultMode
	 * @return new temperature
	 */
	private static double temperature(double T, boolean defaultMode, double step) {
		if (defaultMode) {
			T--;
		} else {
			T -= step;
		}
		return T;
	}

	/**
	 * Finds a random valid neighbor of the given list s0. By switching two
	 * random vertices that are next to each other in the list.
	 * 
	 * @param s0
	 *            Given valid solution for SOP problem.
	 * @param matrix
	 * @return A valid neighbor of the given list.
	 */
	private static List<Integer> randomNeighbour(List<Integer> s0) {
		// copy to safe the original
		List<Integer> original = new ArrayList<Integer>();
		original = copyList(s0);

		while (true) {
			s0 = copyList(original);
			int r = generator.nextInt(s0.size());
			s0 = switchVertex(r, s0);
			// Made a valid change, breaking out of loop.
			if (isValid(s0)) {
				break;
			}
		}
		return s0;
	}

	// Include this method in SA related due to its randomness.
	/**
	 * Switches the vertex at the index r in the list s0 with a random neighbor.
	 * 
	 * @param r
	 *            Index in the list.
	 * @param s0
	 *            List of vertices to switch.
	 */
	private static List<Integer> switchVertex(int r, List<Integer> s0) {
		// first case, r is the last index, can only switch with the lower
		// neighbor
		if (r == s0.size() - 1) {
			s0 = switchLower(s0, r);
			// r is index 0, can only switch with the upper neighbor.
		} else if (r == 0) {
			s0 = switchUpper(s0, r);
			// r is somewhere in the middle, switch with a random neighbor.
		} else {
			// int ud = 0 for lower neighbor, ud = 1 for upper.
			int ud = generator.nextInt(2);
			if (ud == 1) {
				s0 = switchUpper(s0, r);
			} else {
				s0 = switchLower(s0, r);
			}
		}
		return s0;
	}

	/*
	 * ____________HELPING METHODS (NON SA RELATED)_________________________
	 */

	/**
	 * Switches vertex at the index in the list with its upper neighbor.
	 * 
	 * @param s0
	 *            Given list.
	 * @param r
	 *            Given index.
	 * @return The list with the switched vertices.
	 */
	private static List<Integer> switchUpper(List<Integer> s0, int r) {
		int temp = s0.get(r + 1);
		s0.set(r + 1, s0.get(r));
		s0.set(r, temp);
		return s0;
	}

	/**
	 * Switches vertex at the index in the list with its upper neighbor.
	 * 
	 * @param s0
	 *            Given list.
	 * @param r
	 *            Given index.
	 * @return The list witht the switched vertices.
	 */
	private static List<Integer> switchLower(List<Integer> s0, int r) {
		int temp = s0.get(r - 1);
		s0.set(r - 1, s0.get(r));
		s0.set(r, temp);
		return s0;
	}

	/**
	 * A method checking if a tour is valid.
	 * 
	 * @param tour
	 *            The tour to check.
	 * @param A
	 *            The distances of a SOP given in a matrix. In A[i][j] is the
	 *            distance from node i to j. If there is a -1 node j has to be
	 *            visited before node i.
	 * @return True if it the tour is valid, false if not.
	 */
	private static boolean isValid(List<Integer> newSolution) {
		for (int i = 1; i < newSolution.size(); i++) {
			for (int j = 0; j < i; j++) {
				// check if another one has to be visited before
				if (A[newSolution.get(j)][newSolution.get(i)] == -1)
					return false;
			}
		}
		return true;
	}

	/**
	 * Calculate the costs of a tour.
	 * 
	 * @param tour
	 *            Tour to calculate.
	 * @param A
	 *            The distances of a SOP given in a matrix. In A[i][j] is the
	 *            distance from node i to j. If there is a -1 node j has to be
	 *            visited before node i.
	 * @return The costs of the tour
	 */
	private static int cost(List<Integer> tour) {
		int distance = 0;
		for (int i = 0; i < tour.size() - 1; i++) {
			distance += A[tour.get(i)][tour.get(i + 1)];
		}
		return distance;
	}

	/**
	 * Decrement indexes due to given solution.
	 */
	private static List<Integer> fixIndexStart(List<Integer> s0) {
		for (int i = 0; i < s0.size(); i++) {
			int temp = s0.get(i);
			temp--;
			s0.set(i, temp);
		}
		return s0;
	}

	/**
	 * Increment indexes due to expected solution in main.
	 */
	private static List<Integer> fixIndexEnd(List<Integer> s0) {
		for (int i = 0; i < s0.size(); i++) {
			int temp = s0.get(i);
			temp++;
			s0.set(i, temp);
		}
		return s0;
	}

	/**
	 * Method that copies the contents in a list to another new list.
	 * 
	 * @param from
	 *            Contents come from this list.
	 * @return The new copy of the list.
	 */
	private static List<Integer> copyList(List<Integer> from) {
		List<Integer> to = new ArrayList<Integer>();
		for (int i : from) {
			to.add(i);
		}
		return to;
	}
}
