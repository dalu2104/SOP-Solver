package validSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Add simple algorithm that solves an SOP problem in indefinite runtime.
 * MODIFIED BRUTE-FORCE-ALGORITHMN FROM recursiveBruteForce SO IT WILL JUST PICK
 * THE FIRST SOLUTION IT FINDS. ALSO DETECTS IF THERE IS NO SOLUTION.
 * 
 * @author D. LUCAS
 *
 */
public class Simple {
	private static int[][] matrix;
	private static int DIM;

	/**
	 * Finds the valid Solution for a given SOP-Instance.
	 * 
	 * @param A
	 *            given MAtrix
	 * @return a result as List of node-numbers. Or null, if no valid solution
	 *         was found.
	 */
	public static List<Integer> firstIdea(int[][] A) {
		matrix = A;
		DIM = A[0].length;
		List<Integer> path = new ArrayList<Integer>();
		List<Integer> curPath = new ArrayList<Integer>();
		path = recursion(curPath);
		if (path == null) {
			return path;
		}
		// we need to delete the first and the last node, because the
		// Main-method will add them.
		path.remove(0);
		path.remove(path.size() - 1);
		return path;
	}

	/**
	 * Picks all nodes that can be picked to lead to a valid solution. If the
	 * path is not complete, calls itself again to pick more nodes and complete
	 * the path.
	 * 
	 * @param current
	 *            the current solution-path so far.
	 * 
	 * @return A valid path or null if no node was found.
	 */
	private static List<Integer> recursion(List<Integer> current) {
		List<Integer> breakOutOfRecursion = null;
		for (int i = 0; i < DIM; i++) {
			if (validPath(i, current)) {

				current.add(i);
				// checking if the solution-Path is complete.
				// it is complete with DIM-1 nodes (because starting point and
				// destination have to miss)
				if (current.size() < DIM) {
					// cur-Path is not a complete solution-Path
					breakOutOfRecursion = recursion(current);
				} else {
					return current;
				}

				// we found a valid solution in recursion, break out of
				// recursion.
				if (breakOutOfRecursion != null) {
					return current;
				}
				current.remove(current.size() - 1);

			}
		}
		// no valid soluion was found in this branch, or if terminates, in the
		// whole matrix.
		return null;
	}

	/**
	 * Checks if adding a given node to a given solution-path can lead to a
	 * valid solution for the SOP-problem. Contains checking if the node is not
	 * already in the path and if all dependencies are fulfilled after adding
	 * it. *
	 * 
	 * @param node
	 *            the given node
	 * @param current
	 *            the given solution-path to check.
	 * @return true, adding the node to the path can lead to a valid solution
	 *         for the problem.
	 */
	private static boolean validPath(int node, List<Integer> current) {
		boolean result = true;
		if (!current.isEmpty()) {
			if (current.size() == DIM - 1) {
				// only the destination is missing
				if (node != DIM - 1) {
					result = false;
				}
			} else {
				// there are still more nodes than the destination missing
				if (node == DIM - 1) {
					// we don't want the destination node if we don't have all
					// the other nodes in the path
					result = false;
				} else if (node == 0) {
					// we don't want the starting point unless curPath is empty
					result = false;
				} else if (current.contains(node)) {
					// checking if the node is already in the path
					result = false;
				} else {
					// checking the dependencies
					for (int i = 0; i < DIM; i++) {
						if (matrix[node][i] == -1) {
							if (!current.contains(i)) {
								result = false;
							}
						}
					}
				}
			}
		} else {
			// curPath is empty, only the starting point is allowed
			if (node != 0) {
				result = false;
			}
		}
		return result;
	}

}
