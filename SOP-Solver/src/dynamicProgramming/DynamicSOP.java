package dynamicProgramming;

import java.util.ArrayList;
import java.util.List;

public class DynamicSOP {

	public static List<Integer> solveDynamic(int[][] matrix) {
		// get help variables:
		// number of vertices
		int n = matrix.length;

		// create solution List
		List<Integer> solution = new ArrayList<Integer>();
		// check trivial cases up to 3 vertices
		if (n <= 3) {
			// just add the vertices to a list and return them
			for (int i = 0; i < n; i++)
				solution.add(i);
			return solution;
		}
		/*
		 * If we have more vertices we start with the dynamic programming
		 * algorithm. At first it creates a matrix which all subtours ending in
		 * every vertex. In tours[i][j] is the costs of a tour ending in i. The
		 * tour contains a set of vertices representing through a value j. J is
		 * the decimal value of a set of vertices as a binary number. In index
		 * [0] is vertex 2 up to n-1 at index n-2. Example: In tours[3][33] are
		 * the costs of a tour over the vertices 2,5 and 6 (because 2^(2-2) +
		 * 2(^5-2) + 2^(6-2) = 33) ending up in vertex 5-2 = 3;
		 */
		
		// Maximum n = 21 !!!!!!!!!!
		int[][] tours = new int[n - 2][(int) Math.pow(2, n - 2)];
		/*
		 * Create two arrays of List for saving the subtours
		 */
		
		// create a list to save the subtours to the tour lists

		// Saving old code !!!!!!!!!!
		// calculate matrix with recursive formula
		tours = calculateTours(matrix, tours, solution);
		

		// got the tour as a list via backtracking

		solution = calculateRoute(tours, matrix);

		// return solution list
		return solution;
	}

	private static int[][] calculateTours(int[][] matrix, int[][] tours, List<Integer> solution) {

		// get help variables
		// number of vertices
		int n = matrix.length;
		// number of vertices except 1 and n
		int v = n - 2;
		// the value for infinity given by the matrix
		final int inf = matrix[0][n - 1];

		/*
		 * Start Algorithm:
		 */
		/*
		 * initialize values for the sub tours with length one which are only the
		 * values in matrix[0][i-1] for every vertex i;
		 */

		// create boolean array to represent sets of vertices and a variable for
		boolean[] binSet = new boolean[v];
		int binVal = 0;
		// begin with sets of Maechtigkeit 2
		int m = 2;
		init(binSet, m);
	
		

		while (m <= v) {

			// calculate value of actual set
			binVal = calculateSet(binSet);
			// a boolean to check the dependencies
			boolean valid = true;
			// Get all subtours8

			for (int i = 0; i < v; i++) {
				valid = true;
				// if we a vertex is in the Set
				if (binSet[i]) {
					// check dependencies
					for (int j = 0; j < v; j++) {
						if (i != j && binSet[j])
							if (matrix[j + 1][i + 1] == -1)
								valid = false;
					}
					// wenn das Set so zulaessig ist, dass Tour in i enden kann
					if (valid) {
					
						// loesche knoten aus set fuer subtour
						binSet[i] = false;
						tours[i][binVal] = minSubtour(binVal - Math.pow(2, i), matrix, tours, i, binSet);
						// fuege wieder hinzu
						binSet[i] = true;
					} else
						tours[i][binVal] = inf;
				}

			}

			m = changeSet(binSet, m);

		}
		return tours;
	}

	private static List<Integer> calculateRoute(int[][] tours, int[][] matrix) {
		// create List to return the tour
		List<Integer> solution = new ArrayList<Integer>();
		int costs = 0;
		// go back through the array
		// get start point n
		int n = matrix.length;
		// solution.add(n);
		int curr = n - 1;
		// get a help variable for saving actual closest vertex
		int cl = 1;
		// get array for looking which vertex is already visited
		boolean[] visited = new boolean[n];
		visited[curr] = true;
		visited[0] = true;
		// create a binValue representing set of unvisited nodes
		int binVal = (int) Math.pow(2, n - 2) - 1;
		// go through the tours array and search the shortest way
		while (solution.size() < n - 2) {
			cl = firstUnvisited(visited);
			for (int i = 0; i < n - 1; i++) {
				if (visited[i] == false
						&& tours[i - 1][binVal] + matrix[i][curr] < tours[cl - 1][binVal] + matrix[cl][curr])
					cl = i;
			}
			if (solution.isEmpty())
				costs = tours[cl - 1][binVal];

			curr = cl;
			solution.add(0, curr);
			binVal -= (int) Math.pow(2, curr - 1);
			visited[curr] = true;
			if (solution.size() == n - 2)
				costs += matrix[0][curr];
		}

		return solution;
	}

	private static int firstUnvisited(boolean[] visited) {
		int c = 0;
		while (visited[c])
			c++;
		return c;
	}

	private static int minSubtour(double d, int[][] matrix, int[][] tours, int goal, boolean[] binSet) {

		// calculate representing value
		int binVal = (int) d;
		// check if we have a ZweierPotenz = trivial case
		double rest = d;
		while (rest > 1)
			rest = rest / 2.0;
		// if we only have one vertex left
		if (rest == 1 || d == 1)
			return matrix[log(binVal) + 1][goal + 1];
		// if not calculate more
		// get the maximum value of the array
		int min = matrix[0][matrix.length - 1];

		// look for the shortest tour with the recursive formula
		for (int i = 0; i < matrix.length - 2; i++) {
			if (i != goal && binSet[i]) {
				if (min > tours[i][binVal] + matrix[i + 1][goal + 1] && matrix[i + 1][goal + 1] >= 0)
					min = tours[i][binVal] + matrix[i + 1][goal + 1];
			}
		}
		// return minimum end vertex of subtour
		return min;
	}

	private static int log(int binVal) {
		int log = 0;
		while (binVal > 1) {
			binVal /= 2;
			log++;
		}
		return log;
	}

	/**
	 * 
	 * @param binSet
	 * @param m
	 */
	private static void init(boolean[] binSet, int m) {
		for (int i = 0; i < m; i++)
			binSet[i] = true;

	}

	private static int calculateSet(boolean[] binSet) {
		int s = 0;
		for (int i = 0; i < binSet.length; i++) {
			if (binSet[i])
				s += Math.pow(2, i);
		}
		return s;
	}

	private static int changeSet(boolean[] binSet, int m) {

		// ----------------------------------------
		// check increase m

		boolean incr = true;
		for (int i = 1; i <= m; i++) {
			if (binSet[binSet.length - i] == false) {
				incr = false;
				break;
			}
		}
		if (incr) {
			m++;
			for (int i = 0; i < binSet.length; i++) {
				if (i < m)
					binSet[i] = true;
				else
					binSet[i] = false;
			}
			return m;
		}
		// -----------------------------------------
		// check if we need a new first
		boolean newFirst = true;
		for (int i = 1; i < m; i++) {
			if (binSet[binSet.length - i] == false) {
				newFirst = false;
				break;
			}
		}

		if (newFirst) {

			int first = 0;
			while (!binSet[first])
				first++;
			for (int i = 0; i < binSet.length; i++) {
				if (i <= first)
					binSet[i] = false;
				else if (i <= first + m)
					binSet[i] = true;
				else
					binSet[i] = false;
			}

			return m;
		}
		// -----------------------------------------

		// check new second
		if (binSet[binSet.length - 1]) {
			int lastNodes = binSet.length - 1;
			while (binSet[lastNodes])
				lastNodes--;
			lastNodes = binSet.length - lastNodes - 1;

			int c = 0;
			int p = 0;
			while (c < m - lastNodes) {
				if (binSet[p])
					c++;
				p++;
			}
			binSet[p - 1] = false;
			for (int i = p; i < binSet.length; i++)
				if (i < p + m - c + 1)
					binSet[i] = true;
				else
					binSet[i] = false;
			return m;

		}

		// -----------------------------------------
		// set next bit
		int c = binSet.length;
		while (binSet[c - 1] == false)
			c--;
		binSet[c - 1] = false;
		binSet[c] = true;
		return m;
	}
}
