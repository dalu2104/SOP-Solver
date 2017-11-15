package validSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which contains a Method for solving a SOP Problem given as an Array
 * using a greedy algorithm. The method does not solve the problem optimal, but
 * give a valid solution, which can be optimized.
 * 
 * @author Malte Jendroschek
 *
 */
public class GreedySOP {
	/**
	 * The method using a greedy algorithm for solving a SOP probably not
	 * optimal.
	 * 
	 * @param A
	 *            The SOP given as an Array. A[i][j] gives the distance between
	 *            node 1 and j. If a cell contains a -1, node j has to be
	 *            visited before node i.
	 * @return A List containing the nodes in visiting order except of node 1
	 *         and node n = A.length, because starting and end node are set.
	 */
	public static List<Integer> greedy(int[][] A) {

		// the number of nodes
		final int n = A.length;
		// the list to save the order of visited nodes
		List<Integer> solution = new ArrayList<Integer>();
		// start with the first node
		int currentNode = 0;
		// add it for the first step
		solution.add(currentNode);
		// get a Integer for saving the next node
		int nextNode;
		// boolean array if a node is already chosen
		boolean[] visited = new boolean[n];
		// first and last node are set
		visited[0] = true;
		visited[n - 1] = true;
		// starting chose the next vertex until we have all nodes in the solution (except node n)
		while (solution.size() < n-1) {
			// calculate nearest neighbor
			nextNode = nearestNeighbor(A, currentNode, visited);
			//adding the nearest available Node to the solution. +1 because we count nodes from 1 and not from 0, like in the Matrix
			solution.add(nextNode + 1);
			// in the next step we will look from the added node
			currentNode = nextNode;
			// mark the node as visited
			visited[currentNode] = true;
			
		}
		
		// remove the first node because it is already added in the main class
		solution.remove(0);
		// return the solution list of nodes
		return solution;
	}
/**
 * The method for getting the nearest neighbor of a node
 * @param A The SOP given as an Array. A[i][j] gives the distance between
	 *            node 1 and j. If a cell contains a -1, node j has to be
	 *            visited before node i.
 * @param currentNode The node from which we search the nearest neighbor.
 * @param visited A boolean array in which index i is true if a node i is already chosen or false if not.
 * @return The nearest node which can be chosen.
 */
	private static int nearestNeighbor(int[][] A, int currentNode, boolean[] visited) {
		// get start values
		int minNode = 0;
		int min = A[0][A.length - 1];
		// go through all nodes
		for (int i = 1; i < A.length - 1; i++) {
			// if a not chosen node has a smaller distance to currentNode
			if (A[currentNode][i] < min && visited[i] == false) {
				// check if it there are dependencies that the node is not available yet
				if (isValid(i, A, visited)) {
					// if it is available chose it as nextNode 
					min = A[currentNode][i];
					minNode = i;
				}

			}
		}
		// return the nearest neighbor
		return minNode;
	}
/**
 * A method for checking if a nearest node has any open dependencies
 * @param nextNode The chosen Node which dependencies has to be checked
 * @param A The SOP given as an Array. A[i][j] gives the distance between
	 *            node 1 and j. If a cell contains a -1, node j has to be
	 *            visited before node i.
 * @param visited A boolean array in which index i is true if a node i is already chosen or false if not.
 * @return True if nextNode can be chosen, false if not.
 */
	private static boolean isValid(int nextNode, int[][] A, boolean[] visited) {
		for (int i = 1; i < A.length - 1; i++) {
			if (A[nextNode][i] == -1 && visited[i] == false)
				return false;
		}
		return true;
	}
}
