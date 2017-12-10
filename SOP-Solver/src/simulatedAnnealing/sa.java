package simulatedAnnealing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

	/**
	 * Returns a optimal solution as a list according to the given restrictions
	 * inside the matrix.
	 * 
	 * @param matrix
	 *            Given matrix that contains the distances and restrictions.
	 * @return An optimal tour as List of Integers corresponding to the vertices
	 *         in the matrix.
	 */
	public static List<Integer> simulatedAnnealing1(int[][] matrix) {
		// in Order to start, we need a valid solution
		// BEWARE S DOES NOT CONTAIN START AND END VERTEX. INDIZES ARE
		// INCREMENTED TO MATCH LOGICAL DESCRIPTION OF SOP !!!
		List<Integer> s0 = Simple.firstIdea(matrix);
		s0 = fixIndexStart(s0);
		Random generator = new Random();
		List<Integer> s1 = null;
		double T = 100;
		int kmax = 105;

		for (int k = 0; k < kmax; k++) {
			T = temperature(T, true);
			s1 = randomNeighbour(s0, matrix, generator);
			if (P(cost(s0, matrix), cost(s1, matrix), T) >= generator.nextDouble()) {
				s0 = s1;
			}
		}

		s0 = fixIndexEnd(s0);
		return s0;
	}

	/*
	 * ____________HELPING METHODS (SA RELATED____________________________
	 */
	/**
	 * Returns probability to switch to given solution S1, depending on the
	 * temperature, cost of S0 and cost of S1.
	 * 
	 * @param costs0
	 *            Cost of the current solution S0.
	 * @param costs1
	 *            Cost of the possible solution S1.
	 * @param T
	 *            Current given temperature
	 * @return The probability.
	 */
	private static double P(int costs0, int costs1, double T) {
		if(T == 0 && costs1<costs0){
			return 1;
		}else if(T == 0){
			return 0;
		}else if(costs1< costs0){
			return 1;
		} else {
			return Math.exp(-((costs1 - costs0)/T));
		}
	}

	/**
	 * Decrements the temperature T.
	 * 
	 * @param T
	 * @param defaultMode
	 * @return new temperature
	 */
	private static double temperature(double T, boolean defaultMode) {
		if (defaultMode) {
			T--;
		} else {
			// TODO
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
	private static List<Integer> randomNeighbour(List<Integer> s0, int[][] matrix, Random generator) {
		// copy to safe the original
		List<Integer> original = new ArrayList<Integer>(); 
		for(int i: s0){
			original.add(i);
		}

		
		int r = generator.nextInt(s0.size());
		s0 = switchVertex(r, s0);

		if (isValid(s0, matrix) == false) {
			randomNeighbour
		}
		return s0;

	}

	/*
	 * ____________HELPING METHODS (NON SA RELATED)_________________________
	 */

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
	private static boolean isValid(List<Integer> newSolution, int[][] A) {
		for (int i = 1; i < newSolution.size() - 1; i++) {
			// check if another one has to be visited before
			if (A[newSolution.get(i - 1)][newSolution.get(i)] == -1)
				return false;
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
	private static int cost(List<Integer> tour, int[][] A) {
		int distance = 0;
		for (int i = 0; i < tour.size(); i++) {
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
}
