package simulatedAnnealing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import execution.ExeTimeSolutionCost;
import execution.TimeStartAndStop;
import validSolution.Simple;
import simulatedAnnealing.Utility;

/**
 * Class that offers a static method that calculates a good solution for a given
 * SOP problem. This method uses simulated Annealing to determine an a good
 * solution. This method is a heuristic.
 * 
 * @author Daniel Lucas
 *
 */
public class Sa {
	private static int[][] A;
	private static Random generator = new Random();

	/**
	 * Returns a solution for given matrix. Executes an algorithm according to
	 * choice of user. Algorithm can be executed with default values, or with
	 * user given values.
	 * 
	 * @param matrix
	 *            Given matrix for the Sequential ordering problem.
	 * @param defaultMode
	 *            Indicates whether this method was called in default mode or
	 *            not. True, if default method is called. False, otherwise.
	 * @return An Object that saves a solution and the according costs and
	 *         execution time. The solution is null if there is no valid path.
	 *         An empty path if matrix has only 2 vertices (Start and stop).
	 */
	public static ExeTimeSolutionCost simulatedAnnealing(int[][] matrix, boolean defaultMode) {
		ExeTimeSolutionCost returner = new ExeTimeSolutionCost();
		// check if matrix contains more than just Start and stop vertex. No
		// solution needed otherwise. (Return empty list).
		if (matrix.length == 2) {
			returner.setSolution(new ArrayList<Integer>());
			return returner;
			// only one node between start and end vertex.
		} else if (matrix.length == 3) {
			returner.setSolution(new ArrayList<Integer>());
			// add only vertex and check for validness.
			returner.getSolution().add(1);
			if (!Utility.isValid(returner.getSolution(), matrix)) {
				// If invalid, return empty list.
				returner.setSolution(null);
			}
			return returner;
		}

		// variable Initialization.
		A = matrix;
		long startTime = 0;
		double tempDecr;
		double temp;
		int itera;

		// Executing algorithm according to user and calculating execution
		// time.
		if (defaultMode) {
			// Default mode, execution and stopping of execution time.
			startTime = TimeStartAndStop.startTime();
			returner = simulatedAnnealing1();
			returner.setTimeForExecution(TimeStartAndStop.stopTime(startTime));
		} else {
			// User input was chosen. Input preparation.
			InputStreamReader in = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(in);

			try {
				// Choosing starting temperature.
				System.out.println("Enter temperature.");
				temp = Double.parseDouble(br.readLine());

				// Choosing temperature decrement.
				System.out.println("Enter temperature decrementation.");
				tempDecr = Double.parseDouble(br.readLine());

				// Choosing number of iterations.
				System.out.println("Enter amount of iterations.");
				itera = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("Incorrect input.");
				return null;
			}

			// User input values execution and stopping of execution time.
			startTime = TimeStartAndStop.startTime();
			returner = simulatedAnnealing2(temp, tempDecr, itera);
			returner.setTimeForExecution(TimeStartAndStop.stopTime(startTime));
		}
		return returner;
	}

	// DEFAULT MODE SA METHOD
	/**
	 * Executes algorithm in default mode, with parameters that were chosen by
	 * the developer. Returns a solution as a list according to the given
	 * restrictions inside the matrix.
	 * 
	 * @return An tour with minimal costs, as found in the process of Simulated
	 *         Annealing.
	 */
	private static ExeTimeSolutionCost simulatedAnnealing1() {
		// in Order to start, we need a valid solution.
		// BEWARE STARTING SOLUTION DOES NOT CONTAIN START AND END VERTEX.
		// Saving the solution in extra objects, because this way, we need to
		// calculate the costs for a solution only once.
		ExeTimeSolutionCost s0 = new ExeTimeSolutionCost();
		// The starting solution was found with a developed greedy algorithm.
		s0.setSolution(Simple.firstIdea(A));
		// check if there is a valid solution to the problem, if not the greedy
		// algorithm would tell us.
		if (s0.getSolution() == null) {
			// there is no valid solution for this test instance.
			return s0;
		}
		s0.setCost(Utility.cost(s0.getSolution(), A));
		ExeTimeSolutionCost s1 = new ExeTimeSolutionCost();

		// Scaling temperature decrease and iterations according to the size of
		// our problem.
		// Runtime now O(n^2). Still a lot faster than brute force algorithm.
		int n = s0.getSolution().size();
		double T = 200;
		// testing has shown that instances with dimensions of 100 or higher do
		// not terminate in realistic time, so we generate an upper bound.
		int kmax;
		if (n < 100) {
			kmax = 120 * n * n;
		} else {
			kmax = 120 * 100 * 100;
		}
		// Bigger instances should cool down slower.
		double tempDecr;
		if (n < 55) {
			tempDecr = 0.00001;
		} else {
			tempDecr = 0.000001;
		}

		// saves the global best solution. Initial solution is created valid
		// solution.
		ExeTimeSolutionCost bestSolution = new ExeTimeSolutionCost();
		bestSolution.setSolution(Utility.copyList(s0.getSolution()));
		bestSolution.setCost(s0.getCost());

		for (int k = 0; k < kmax; k++) {
			// calculate new temperature.
			T = temperature(T, tempDecr);

			// create a new neighbor to s0.
			s1.setSolution(randomNeighbour(s0.getSolution()));
			s1.setCost(Utility.cost(s1.getSolution(), A));

			if (P(s0.getCost(), s1.getCost(), T) >= generator.nextDouble()) {
				s0.setSolution(Utility.copyList(s1.getSolution()));
				s0.setCost(s1.getCost());

				// if new solution is better as best one we have had so far, we
				// safe it as our global best.
				if (s0.getCost() < bestSolution.getCost()) {
					bestSolution.setSolution(Utility.copyList(s0.getSolution()));
					bestSolution.setCost(s0.getCost());
				}
			}
		}

		return bestSolution;
	}

	// USER INPUT SA METHOD. Similar to the default mode method, except the
	// parameters that are given through the method are used.
	/**
	 * Called with user input parameters. Returns a solution as a list according
	 * to the given restrictions inside the matrix.
	 * 
	 * @return An tour with minimal costs, as found in the process of Simulated
	 *         Annealing.
	 */
	private static ExeTimeSolutionCost simulatedAnnealing2(double userTemp, double userTempDecr, int userMaxIt) {
		// in Order to start, we need a valid solution (found by Greedy.
		// BEWARE S DOES NOT CONTAIN START AND END VERTEX.
		// getting a valid solution for start.
		// Saving the solution in extra objects, because this way, we need to
		// calculate the costs for a solution only once.
		ExeTimeSolutionCost s0 = new ExeTimeSolutionCost();
		s0.setSolution(Simple.firstIdea(A));
		// check if there is a valid solution to the problem, if not the greedy
		// algorithm would tell us.
		if (s0.getSolution() == null) {
			// there is no valid solution for this test instance.
			return s0;
		}
		s0.setCost(Utility.cost(s0.getSolution(), A));
		ExeTimeSolutionCost s1 = new ExeTimeSolutionCost();
		// user input
		double T = userTemp;
		int kmax = userMaxIt;

		// saves the global best solution. Initial solution is created valid
		// solution.
		ExeTimeSolutionCost bestSolution = new ExeTimeSolutionCost();

		bestSolution.setSolution(Utility.copyList(s0.getSolution()));
		bestSolution.setCost(s0.getCost());

		for (int k = 0; k < kmax; k++) {
			// calculate new temperature.
			T = temperature(T, userTempDecr);
			// create a new neighbor to s0.
			s1.setSolution(randomNeighbour(s0.getSolution()));
			s1.setCost(Utility.cost(s1.getSolution(), A));

			if (P(s0.getCost(), s1.getCost(), T) >= generator.nextDouble()) {
				s0.setSolution(Utility.copyList(s1.getSolution()));
				s0.setCost(s1.getCost());

				// if new solution is better as best one we have had so far, we
				// safe it as our global best.
				if (s0.getCost() < bestSolution.getCost()) {
					bestSolution.setSolution(Utility.copyList(s0.getSolution()));
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
		// Always accept better solution.
		if (cost1 < cost0) {
			return 1;
			// If temperature is decreased to maximum (zero), we use Greedy
			// technique and only accept better solutions.
		} else if (T == 0) {
			return 0;
		} else {
			// Calculating the probability of accepting a new solution.
			return Math.exp((cost0 - cost1) / T);
		}
	}

	/**
	 * Decrements the temperature T.
	 * 
	 * @param T
	 *            Given temperature to decrement.
	 * @param step
	 *            Step by which the temperature is decreased.
	 * @return decremented temperature
	 */
	private static double temperature(double T, double step) {
		// T can only be reduced if its above zero.
		if (T == 0) {
			return T;
		}

		T *= 1 - step;
		return T;
	}

	/**
	 * Finds a random valid neighbor of the given list s0. By switching two
	 * random vertices in the list.
	 * 
	 * @param s0
	 *            Given valid solution for SOP problem.
	 * @return A valid neighbor of the given list.
	 */
	private static List<Integer> randomNeighbour(List<Integer> s0) {
		// copy to safe the original, s0 now makes its changes on its own
		// version of the list.
		List<Integer> original = new ArrayList<Integer>();
		original = Utility.copyList(s0);

		// From original, switch two random vertices. If switched list is valid,
		// break. If it is not valid, get original again and switch two random
		// vertices and so on.
		while (true) {
			s0 = Utility.copyList(original);
			int r = generator.nextInt(s0.size());
			int k = generator.nextInt(s0.size());
			s0 = Utility.switchTwo(s0, r, k);
			// Made a valid change, breaking out of loop.
			if (Utility.isValid(s0, A)) {
				break;
			}
		}
		return s0;
	}
}
