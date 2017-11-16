package tryAll;

import java.util.List;
import validSolution.GreedySOP;

/**
 * A Class containing a Method for solving a SOP and gives the optimal tour.
 * 
 * @author Malte Jendroschek
 *
 */

public class Permutations {

	/**
	 * 
	 * @param A
	 *            The distances of a SOP given in a matrix. In A[i][j] is the
	 *            distance from node i to j. If there is a -1 node j has to be
	 *            visited before node i.
	 * @return A List containing the perfect tour for a given SOP. The List not
	 *         includes node 1 and n, because they are added in the main class.
	 * 
	 */
	public static List<Integer> checkAllPossibilities(int[][] A) {
		// get two valid starting tours with a simple greedy algorithm
		List<Integer> bestSolution = GreedySOP.greedy(A);
		List<Integer> newSolution = GreedySOP.greedy(A);
		// get the length of the tour without node 1 and node n
		int n = bestSolution.size();
		// call the recursive method trying all permutations.
		searchAll(n, newSolution, A, bestSolution);
		// return the optimal tour
		return bestSolution;
	}

	/**
	 * A Method trying all combinations and save the best one.
	 * 
	 * @param n
	 *            The lenngth of the tour
	 * @param newSolution
	 *            A new solution which distance will be compared to the best
	 *            one.
	 * @param A
	 *            The distances of a SOP given in a matrix. In A[i][j] is the
	 *            distance from node i to j. If there is a -1 node j has to be
	 *            visited before node i.
	 * @param bestSolution
	 *            The actual best solution
	 */
	private static void searchAll(int n, List<Integer> newSolution, int[][] A, List<Integer> bestSolution) {
		// if the method is call with n == 0 we have a new permutation (tour)
		if (n == 0) {
			// check if it is valid
			if (isValid(newSolution, A)) {
				// if it is a better tour
				if (costs(newSolution, A) < costs(bestSolution, A)) {
					// copy the new better tour in bestSolution
					bestSolution.clear();
					bestSolution.addAll(newSolution);
				}
			}
			// if n > 0 we have to change some nodes to get a new one and call
			// the method recursive
		} else {
			// creating new permutations
			for (int i = 0; i < n; i++) {
				swap(newSolution, i, n - 1);
				searchAll(n - 1, newSolution, A, bestSolution);
				swap(newSolution, i, n - 1);
			}
		}

	}

	/**
	 * A method change the places of two nodes i and j
	 * 
	 * @param newSolution
	 *            The Tour has to be changed
	 * @param i
	 *            Node i which change to the place of j
	 * @param j
	 *            Node j which change to the place of i
	 */
	private static void swap(List<Integer> newSolution, int i, int j) {
		int temp = newSolution.get(i);
		newSolution.set(i, newSolution.get(j));
		newSolution.set(j, temp);
	}

	/**
	 * A method which checks if a tour is valid.
	 * 
	 * @param newSolution
	 *            The checked tour
	 * @param A
	 *            The distances of a SOP given in a matrix. In A[i][j] is the
	 *            distance from node i to j. If there is a -1 node j has to be
	 *            visited before node i.
	 * @return True if it is a valdi tour, false if not.
	 */
	private static boolean isValid(List<Integer> newSolution, int[][] A) {
		// get the length of the tour
		int n = A.length - 1;
		// create a boolean to look if a node is visited yet
		boolean[] visited = new boolean[newSolution.size() + 1];
		// look for all nodes if the dependencies are fullfilled
		for (int i = 0; i < newSolution.size() - 1; i++) {
			// mark a node as visited
			visited[newSolution.get(i) - 1] = true;
			for (int j = 1; j < n; j++) {
				// check if another one has to be visited before
				if (A[newSolution.get(i) - 1][j] == -1 && visited[j] == false)
					return false;
			}
		}
		return true;
	}

	/**
	 * Calculate the costs of a tour
	 * 
	 * @param bestSolution
	 *            The tour which costs has to be calcuated
	 * @param A
	 *            The distances of a SOP given in a matrix. In A[i][j] is the
	 *            distance from node i to j. If there is a -1 node j has to be
	 *            visited before node i.
	 * @return The costs of the tour
	 */
	private static int costs(List<Integer> bestSolution, int[][] A) {

		int distance = 0;
		for (int i = 0; i < bestSolution.size() - 1; i++) {
			distance += A[bestSolution.get(i) - 1][bestSolution.get(i + 1) - 1];
		}
		return distance;
	}
}