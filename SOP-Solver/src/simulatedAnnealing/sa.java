package simulatedAnnealing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import execution.ExeTimeSolutionCost;
import execution.TimeStartAndStop;
import validSolution.OneSolution;
import validSolution.Simple;

/**
 * Class that offers a static method that calculates a good solution for a given
 * SOP problem. This method uses simulated Annealing to determine an a good
 * solution. This method is a heuristic.
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
	public static ExeTimeSolutionCost simulatedAnnealing(int[][] matrix) throws NumberFormatException, IOException {
		A = matrix;
		long startTime = 0;
		ExeTimeSolutionCost returner = new ExeTimeSolutionCost();

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
			returner = simulatedAnnealing1();
			returner.setTimeForExecution(TimeStartAndStop.stopTime(startTime));
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

			// execution and stopping of execution time.
			startTime = TimeStartAndStop.startTime();
			returner = simulatedAnnealing2(temp, tempDecr, itera);
			returner.setTimeForExecution(TimeStartAndStop.stopTime(startTime));
			break;
		default:
			System.out.println("Invalid input.");
			return returner;
		}

		return returner;
	}

	// DEFAULT MODE SA METHOD
	/**
	 * Default mode, no user input for parameters. Returns a optimal solution as
	 * a list according to the given restrictions inside the matrix.
	 * 
	 * @return An tour with minimal costs, as found in the process of Simulated
	 *         Annealing.
	 */
	private static ExeTimeSolutionCost simulatedAnnealing1() {
		// in Order to start, we need a valid solution
		// BEWARE S DOES NOT CONTAIN START AND END VERTEX.
		// getting a valid solution for start.
		// Saving the solution in extra objects, because this way, we need to
		// calculate the costs for a solution only once
		ExeTimeSolutionCost s0 = new ExeTimeSolutionCost();
		s0.setSolution(OneSolution.findSolution(A));
		s0.setCost(cost(s0.getSolution()));
		System.out.println(s0.getCost());
		ExeTimeSolutionCost s1 = new ExeTimeSolutionCost();

		// Scale our values according to the size of our problem.
		int n = s0.getSolution().size();
		
		//Runtime now O(n^2). Still a lot faster than brute force algorithm.
		double T = 115 * n * n;
		int kmax = 120 * n * n ;
		// saves the global best solution. Initial solution is created valid
		// solution.
		ExeTimeSolutionCost bestSolution = new ExeTimeSolutionCost();
		bestSolution.setSolution(copyList(s0.getSolution()));
		bestSolution.setCost(s0.getCost());
		
		for (int k = 0; k < kmax; k++) {
			// calculate new temperature.
			T = temperature(T, true, 0);
			// create a new neighbor to s0.
			s1.setSolution(randomNeighbour(s0.getSolution()));
			s1.setCost(cost(s1.getSolution()));
			
			if (P(s0.getCost(), s1.getCost(), T) >= generator.nextDouble()) {
				s0.setSolution(copyList(s1.getSolution()));
				s0.setCost(s1.getCost());

				// if new solution is better as best one we have had so far, we
				// safe it as our global best.
				if (s0.getCost() < bestSolution.getCost()) {
					bestSolution.setSolution(copyList(s0.getSolution()));
					bestSolution.setCost(s0.getCost());
				}
			}
		}

		return bestSolution;
	}

	// USER INPUT SA METHOD
	/**
	 * Called with user input parameters. Returns a optimal solution as a list
	 * according to the given restrictions inside the matrix.
	 * 
	 * @return An tour with minimal costs, as found in the process of Simulated
	 *         Annealing.
	 */
	private static ExeTimeSolutionCost simulatedAnnealing2(double userTemp, double userTempDecr, int userMaxIt) {
		// in Order to start, we need a valid solution
		// BEWARE S DOES NOT CONTAIN START AND END VERTEX.
		// getting a valid solution for start.
		// Saving the solution in extra objects, because this way, we need to
		// calculate the costs for a solution only once.
		ExeTimeSolutionCost s0 = new ExeTimeSolutionCost();
		s0.setSolution(OneSolution.findSolution(A));
		s0.setCost(cost(s0.getSolution()));
		ExeTimeSolutionCost s1 = new ExeTimeSolutionCost();
		// user input
		double T = userTemp;
		int kmax = userMaxIt;

		// saves the global best solution. Initial solution is created valid
		// solution.
		ExeTimeSolutionCost bestSolution = new ExeTimeSolutionCost();

		bestSolution.setSolution(copyList(s0.getSolution()));
		bestSolution.setCost(s0.getCost());

		for (int k = 0; k < kmax; k++) {
			// calculate new temperature.
			T = temperature(T, false, userTempDecr);
			// create a new neighbor to s0.
			s1.setSolution(randomNeighbour(s0.getSolution()));
			s1.setCost(cost(s1.getSolution()));
			
			if (P(s0.getCost(), s1.getCost(), T) >= generator.nextDouble()) {
				s0.setSolution(copyList(s1.getSolution()));
				s0.setCost(s1.getCost());
				
				// if new solution is better as best one we have had so far, we
				// safe it as our global best.
				if (s0.getCost() < bestSolution.getCost()) {
					bestSolution.setSolution(copyList(s0.getSolution()));
					bestSolution.setCost(s0.getCost());
				}
			}
		}

		return bestSolution;
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
			return Math.exp((cost0 - cost1) / T);
		}
	}

	/**
	 * Decrements the temperature T.
	 * 
	 * @param T
	 *            Given temperature to decrement.
	 * @param defaultMode
	 *            Switch that tells us whether or not this method is called in
	 *            default mode. True for dedault mode and false otherwise.
	 * @return decremented temperature
	 */
	private static double temperature(double T, boolean defaultMode, double step) {
		// T can only be reduced if its above zero.
		if (defaultMode && T > 0) {
			T--;
			// Temperature cannot go below zero.
		} else if (T == 0) {
			return T;
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
	 * @return A valid neighbor of the given list.
	 */
	private static List<Integer> randomNeighbour(List<Integer> s0) {
		// copy to safe the original, s0 now makes its changes on its own
		// version of the list.
		List<Integer> original = new ArrayList<Integer>();
		original = copyList(s0);

		// From original, switch two random vertices. If switched list is valid,
		// break. If it is not valid, get original again and switch two random
		// vertices and so on.
		while (true) {
			s0 = copyList(original);
			int r = generator.nextInt(s0.size());
			int k = generator.nextInt(s0.size());
			s0 = switchTwo(s0, r, k);
			// Made a valid change, breaking out of loop.
			if (isValid(s0)) {
				break;
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
	 * @param k
	 *            Given index.
	 * @return The list with the switched vertices.
	 */
	private static List<Integer> switchTwo(List<Integer> s0, int r, int k) {
		int temp = s0.get(k);
		s0.set(k, s0.get(r));
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
				// check if another vertex has to be visited before
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
	 * Method that copies the contents in a list to another new list.
	 * 
	 * @param from
	 *            Contents come from this list.
	 * @return The new copy of the list.
	 */
	private static List<Integer> copyList(List<Integer> from) {
		// creating a new list, so the target will have an own copy of its list
		// and not refer to the same list (which might be randomly changed).
		List<Integer> to = new ArrayList<Integer>();
		// adding all elements to a new list.
		for (int i : from) {
			to.add(i);
		}
		return to;
	}
}
