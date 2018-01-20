package validSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Add simple algorithm that solves an SOP problem in indefinite runtime.
 * MODIFIED BRUTE-FORCE-ALGORITHMN FROM recursiveBruteForce. Detects instances with no valid solution and will return null.
 * Also picks its next vertex Greedy style.
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
		curPath.add(0);
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
	 * the path. Picks nearest nodes first.
	 * 
	 * @param current
	 *            the current solution-path so far.
	 * 
	 * @return A valid path or null if no node was found.
	 */
	private static List<Integer> recursion(List<Integer> current) {
		List<Integer> breakOutOfRecursion = null;
		// array that saves us what we have visited so far.
		boolean[] visited = new boolean[DIM];
		initializeVisited(visited, current);
		// distance from current node to node at specified index.
		int[] distance = new int[DIM];
		boolean invalid = initializeDistance(distance, current);
		// reached dead end. current last vertex has some unfulfilled dependencies. Jump out of this loop.
		if (invalid) {
			return null;
		}

		// we can visit another vertex.
		while (visitable(visited)) {
			// choose shortest reachable vertex.
			int minDist = Integer.MAX_VALUE;
			int nextNode = -1;
			for (int i = 0; i < distance.length; i++) {
				if (visited[i] == false) {
					if (distance[i] < minDist) {
						minDist = distance[i];
						nextNode = i;
					}
				}
			}
			// shortest distance, add node and mark as visited.
			current.add(nextNode);
			visited[nextNode] = true;

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
		// no valid soluion was found in this branch, or if terminates, in the
		// whole matrix.
		return null;

	}

	/** Initializes the distance array according to our current last vertex in list. From this last vertex, calculates distance with the help of the matrix.
 	 * 
	 * @param distance Array that saves the distance to the vertex at the index.
	 * @param list Contains the vertex we are looking at, at the last available index.
	 * @return True, if there is an unfulfilled dependency and false otherwise.
	 */
	private static boolean initializeDistance(int[] distance, List<Integer> list) {
		for (int i = 0; i < distance.length; i++) {
			// detected that a vertex that is not in solution but needs to come
			// before
			if (matrix[list.get(list.size() - 1)][i] == -1 && !list.contains(i)) {
				return true;
			}
			distance[i] = matrix[list.get(list.size() - 1)][i];
		}
		return false;
	}
	
	/** Initializes the visited array. Sets array to true, if node is already in the list.
	 * 
	 * @param visited	Array to be initialized.
	 * @param list	List that contains all nodes that have been visited.
	 */
	private static void initializeVisited(boolean[] visited, List<Integer> list) {
		for (int i : list) {
			visited[i] = true;
		}
	}

	/** Tells us whether there is another vertex that can be visited.
	 * 
	 * @param visited Array that tells us, if there is another vertex to be visited.
	 * @return Returns true, if we can visit another vertex, and false otherwise.
	 */
	private static boolean visitable(boolean[] visited) {
		for (int i = 0; i < visited.length; i++) {
			if (visited[i] == false) {
				return true;
			}
		}
		return false;
	}

}
