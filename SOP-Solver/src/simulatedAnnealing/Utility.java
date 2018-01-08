package simulatedAnnealing;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that offers utility methods for the Simulated Annealing algorithmn.
 * These methods include switchting two vertices in a list, Checking a list for
 * validness in a given matrix, calculating the costs of a tour with a given
 * matrix, and copying a list to a new list object.
 * 
 * @author Daniel Lucas
 *
 */
public class Utility {

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
	protected static List<Integer> switchTwo(List<Integer> s0, int r, int k) {
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
	public static boolean isValid(List<Integer> newSolution, int[][] A) {
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
	protected static int cost(List<Integer> tour, int[][] A) {
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
	protected static List<Integer> copyList(List<Integer> from) {
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
