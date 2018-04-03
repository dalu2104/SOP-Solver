package dynamicProgramming;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DynamicSOPLists {

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

		/*
		 * Create two arrays of List for saving the subtours
		 */

		

		
		// calculate matrix with recursive formula
		solution = calculateTours(matrix);
		// Saving old code !!!!!!!!!!!

		// got the tour as a list via backtracking

		// solution = calculateRoute(tours, matrix);

		// return solution list
		return solution;
	}

	private static List<Integer> calculateTours(int[][] matrix) {
		// get help variables
		// number of vertices
		int n = matrix.length;
		// number of vertices except 1 and n
		int v = n - 2;
		// the value for infinity given by the matrix

		// help lists for saving costs
		List<ArrayList<Integer>> costs1 = new ArrayList<ArrayList<Integer>>(n - 1);
		List<ArrayList<Integer>> costs2 = new ArrayList<ArrayList<Integer>>(n - 1);

		
		List<ArrayList<Integer>> binVal1 = new ArrayList<ArrayList<Integer>>(n - 1);
		List<ArrayList<Integer>> binVal2 = new ArrayList<ArrayList<Integer>>(n - 1);
		
		/*
		 * The tours will be saved in two list. One is a list of the vertices. If a tour is in place
		 * i in the first list, then will be the last vertex of a set of tours vertex i.
		 * The second LinkedList is a list of all tours of the same power which will end in the vertex
		 * with the index of the ArrayList
		 */
		ArrayList<LinkedList<Tour>> tours_1 = new ArrayList<LinkedList<Tour>>(n-2);
		ArrayList<LinkedList<Tour>> tours_2 = new ArrayList<LinkedList<Tour>>(n-2);
		
		
		/*
		 * Start Algorithm:
		 */
		
		// calculate the first column
		tours_1 = startColumn(matrix);
		// prepare the second column(prepare means create the array list and add empty linked list
		// so that we can add a tour in chosen index i in case we have no possible tour ending in a vertex i-1
		tours_2 =  prepareColumn(v);
		// create boolean array to represent sets of vertices and a variable for
		// the representing bin Value
		boolean[] binSet = new boolean[v];
		int binVal = 0;
		// begin with sets of power 2
		int power = 2;
		
		// initialize method (stating with vertex 2 and 3)
		binSet = initSet(power, v);
		
		// initialize first column( just the tours from 1 to each vertex)
		initCosts1(costs1, matrix);
		initBinVal1(binVal1, n);
		
		// initialize second column with zeros to make it possible, that get calls will get somethin
		initCosts2(costs2, n);
		initBinVal2(binVal2, n);

		// a boolean to toggle between lists
		boolean nextColumnOne = false;
		
		// check all sets of the vertices
		
		while (power <= v) {
			
			// save power to make a check if it will be increased and if lists have to be deleted 
			int oldM = power;

			// calculate value of actual set
			binVal = calculateSet(binSet);
			// a boolean to check the dependencies
			boolean valid = true;
			/* 
			 * Get all tours:
			 */
			
			// a counter how many vertices are checked to save runtime
			int goals = 0;
			for (int i = 0; i < v; i++) {
				// check if we already checked all vertices in the set
				if (goals == power)
					break;
				
				// if vertex i is in the actual set
				if (binSet[i]) {
					// count up the counter for checked vertices
					goals++;
					
					// check if we can end in vertex i
					valid = true;
					for (int j = 0; j < v; j++) {
						if (i != j && binSet[j])
							// if there is a (-1) in the distance matrix we cannot end in vertex i
							if (matrix[j + 1][i + 1] == -1) {
								// the tour cannot end in i
								valid = false;
								break;
							}
					}
					// only if we have a valid set go further to calculate the distances
					if (valid) {

						// delete vertex from set
						binSet[i] = false;

						// calculate costs in actual lists:
						if (nextColumnOne) {
							minCost(tours_1, binVal - Math.pow(2, i), matrix, tours_2, i, binSet, power-1);
							// costs1.get(i).add(c);

							binVal1.get(i).add(binVal);

						} else {
							minCost(tours_2, binVal - Math.pow(2, i), matrix, tours_1, i, binSet, power-1);
							// costs2.get(i).add(c);

							binVal2.get(i).add(binVal);

						}
						// at the end we add the vertex again
						binSet[i] = true;
					}

				}

			}
			// change Set for next tour
			power = changeSet(binSet, power);
			
			// if we increased the power of the sets we have to delete a column
			// and prepare the new ones
			if (oldM != power) {
				if (nextColumnOne) {
					nextColumnOne = false;
					tours_2 = prepareColumn(v);
					// old stuff
					
					
					costs2.clear();
					binVal2.clear();
					initCosts2(costs2, n);
					initBinVal2(binVal2, n);
					
					

				} else {
					nextColumnOne = true;
					tours_1 = prepareColumn(v);
					
					// old stuff
					
					costs1.clear();
					binVal1.clear();
					initCosts2(costs1, n);
					initBinVal2(binVal1, n);
					

				}
			}
		}
		
		// when all columns were calculated search the shortest and return it
		// if there are no tours left, there is no valid tour and we will return a tour with costs infinity
		
		// create Tour object to return
		Tour shortest = new Tour();
		// first set costs infinite
		shortest.setCosts(matrix[0][n-1]);
		
		// check which row has to be read
		if (v % 2 == 0) {

			// go through all vertices and check there value
			for (int i = 0; i < costs2.size(); i++) {
					
					// get length of tour with all vertices end in i
					int candidate = costs2.get(i).get(1);
					if (candidate != 0 && candidate < shortest.getCosts()) {
						shortest.setCosts(candidate);
					
				}
			}
			
		} else {
			for (int i = 0; i < tours_1.size(); i++) {
				if(!tours_1.get(i).isEmpty())
				for (int j = 0; j < tours_1.get(i).size(); j++) {
					int cand = tours_1.get(i).get(j).getCosts();
					if (cand != 0 && cand < shortest.getCosts()) {
						shortest.setCosts(cand); 
						shortest.setTour(tours_1.get(i).get(j).getTour());
						
					}
				}
			}

			
		}

		return shortest.getTour();
	}

	

	/**
	 * 
	 * @param tours_1
	 * @param d
	 * @param matrix
	 * @param tours_2
	 * @param goal
	 * @param binSet
	 * @param binVal1
	 */
	private static void minCost(ArrayList<LinkedList<Tour>> tours_1, double d, int[][] matrix,
			ArrayList<LinkedList<Tour>> tours_2, int goal, boolean[] binSet, int m) {

		// get the maximum value of the array
		int min = matrix[0][matrix.length - 1];
		// an empty tour to save the shortest
		LinkedList<Integer> shortestTour = new LinkedList<Integer>();
		// calculate representing value
		long binVal = (long) d;
		// check if we have a power of two
		double rest = d;
		while (rest > 1)
			rest = rest / 2.0;
		// if we only have one vertex left
		if (rest == 1 || d == 1) {
			min = matrix[log(binVal) + 1][goal + 1];
			shortestTour.add(log(binVal));

		}
		// if not calculate more
		else {
			int goals = 0;
			// look for the shortest tour with the recursive formula
			for (int i = 0; i < matrix.length - 2; i++) {
				if(goals == m)
					break;
				if(binSet[i])
					goals++;
				if (i != goal && binSet[i]) {
					for (int j = 0; j < tours_2.get(i).size(); j++) {
						if (tours_2.get(i).get(j).getBinVal() == binVal)
							if (min > tours_2.get(i).get(j).getCosts() + matrix[i + 1][goal + 1]
									&& matrix[i + 1][goal + 1] >= 0) {
								min = tours_2.get(i).get(j).getCosts() + matrix[i + 1][goal + 1];
								shortestTour.clear();
								shortestTour.addAll(tours_2.get(i).get(j).getTour());
								j = tours_2.get(i).size();
							}
						
					}
				}
			}
		}
		shortestTour.add(goal);
		Tour tour = new Tour(min, d + Math.pow(2, goal), shortestTour);	
		tours_1.get(goal).add(tour);

	}

	private static int log(long binVal) {
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
	private static boolean[] initSet(int m, int v) {
		boolean[] set = new boolean[v];
		for(int i = 0; i < m; i++)
			set[i] = true;
		return set;
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
	//_________________________________INIT METHODS__________________________________//
	/**
	 * Method creates the first column of the SOP Algorithm: The costs and tours from
	 * 1 to each vertex and the costs from distance matrix[0][i] for all i of (1,..,n-1).
	 * @param matrix The given distance matrix
	 * @return A list of tours contains just the tours from vertex 1 to each other vertex except n.
	 */
	private static ArrayList<LinkedList<Tour>> startColumn(int [][] matrix){
		// get number of vertices
		int n = matrix.length;
		// create ArrayList
		ArrayList<LinkedList<Tour>> firstColumn = new ArrayList<>(n-2);
		// add each vertex
		for (int i =0; i < n-1; i++){
			Tour tour  = new Tour (matrix[0][i+1], Math.pow(2, i),i);
			LinkedList<Tour> linkedList = new LinkedList<Tour>();
			linkedList.add(tour);
			firstColumn.add(linkedList);
		}
		return firstColumn;
	}
	/**
	 * Method preparing a new ArrayList with empty LinkedList. It will be prepared that way to add Tours in a special place
	 * we have to chose with a get(index), which causes problem with empty lists.
	 * @param v The number of vertices
	 * @return The prepared ArrayList.
	 */
	private static ArrayList<LinkedList<Tour>> prepareColumn(int v) {
		// create list and save an empty linked list in every index
		ArrayList<LinkedList<Tour>> arrayList = new ArrayList<>(v);
		for(int i = 0; i < v; i++)
			arrayList.add(new LinkedList<Tour>());
		return arrayList;
	}
	
	
	
	private static void initBinVal1(List<ArrayList<Integer>> binVal1, int n) {
		for (int i = 0; i < n - 1; i++) {
			ArrayList<Integer> l = new ArrayList<Integer>();
			l.add((int) Math.pow(2, i));
			binVal1.add(l);
		}

	}

	private static void initCosts1(List<ArrayList<Integer>> costs1, int[][] matrix) {
		for (int i = 0; i < matrix.length - 1; i++) {
			ArrayList<Integer> l = new ArrayList<Integer>();
			l.add(matrix[0][i + 1]);
			costs1.add(l);
		}

	}

	private static void initCosts2(List<ArrayList<Integer>> costs2, int n) {
		for (int i = 0; i < n - 1; i++) {
			ArrayList<Integer> l = new ArrayList<Integer>();
			
			costs2.add(l);
		}
	}

	private static void initBinVal2(List<ArrayList<Integer>> binVal2, int n) {
		for (int i = 0; i < n - 1; i++) {
			ArrayList<Integer> l = new ArrayList<Integer>();
			
			binVal2.add(l);
		}
	}

}
