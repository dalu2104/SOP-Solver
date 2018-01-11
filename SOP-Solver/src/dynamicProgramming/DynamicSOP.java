package dynamicProgramming;

import java.util.ArrayList;
import java.util.List;

public class DynamicSOP {

	public static List<Integer> solveDynamic(int[][] matrix) {
		// get help variables
		// number of vertices
		int n = matrix.length;
		// create solution List
		List<Integer> solution = new ArrayList<Integer>();
		// start with the dynamic program
		// create an array for the distances
		// first index is the vertex, second is a Set representing through
		// binary numbers
		int[][] tours = new int[n - 2][(int) Math.pow(2, n - 2)];
		// // initialize values: tours with length one
		// for (int i = 1; i <= n - 2; i++)
		// tours[i - 1][(int) Math.pow(2, i - 1)] = matrix[0][i]+1;
		// calculate matrix with recursive formula
		tours = calculateTours(matrix, tours, solution);
		// got the tour as a list via backtracking

		solution = calculateRoute(tours, matrix);

		// return solution list
		return solution;
	}

	private static List<Integer> calculateRoute(int[][] tours, int[][] matrix) {
		// create List to return the tour
		List<Integer> solution = new ArrayList<Integer>();
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
			curr = cl;
			solution.add(0, curr);
			binVal -= (int) Math.pow(2, curr - 1);
			visited[curr] = true;
		}
		return solution;
	}

	private static int firstUnvisited(boolean[] visited) {
		int c = 0;
		while (visited[c])
			c++;
		return c;
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
		// create boolean array to represent sets of vertices and a variable for
		boolean[] binSet = new boolean[v];
		// start with sets of one

		int binVal = 0;
		// begin with sets of Mächtigkeit 2
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
				if (binSet[i]) {

					// check dependencies
					for (int j = 0; j < v; j++) {
						if (i != j && binSet[j])
							if (matrix[j + 1][i + 1] == -1)
								valid = false;
					}
					if (valid) {
						// lösche knoten aus set für subtour
						binSet[i] = false;
						tours[i][binVal] = minSubtour(binVal - Math.pow(2, i), matrix, tours, i, binSet);
						// füge wieder hinzu
						binSet[i] = true;
					} else
						tours[i][binVal] = inf;
				}

				/*
				 * Check dependencies !!!!!!!!!
				 */
			}

			m = changeSet(binSet, m);

		}
		return tours;
	}

	private static int minSubtour(double d, int[][] matrix, int[][] tours, int goal, boolean[] binSet) {
		// debugging
		int a = 0;
		if (d == 13)
			a = 4;
		// calculate representing value
		int binVal = (int) d;
		// check if we have a ZweierPotenz
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
		// get length of binSet
		int v = binSet.length;

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
