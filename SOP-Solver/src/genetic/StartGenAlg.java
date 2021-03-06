package genetic;

import java.util.ArrayList;
import java.util.List;

/**
 * Solves a Sequential Ordering Problem via a genetic Algorithm with multiple
 * runs. Note that it might not give the optimal result.
 * 
 * @author Thore Paetsch
 *
 */
public class StartGenAlg {

	/**
	 * Defines how many times the genetic Algorithm has to run through.
	 */
	private static final int RUNS = 3;

	/**
	 * Solves a Sequential Ordering Problem, given by a matrix of distances and
	 * dependencies, by calling a genetic Algorithm. Puts the solution into a
	 * shape that the main-method in the Exe-class can handle.
	 * 
	 * @param matix
	 *            the given instance of a SOP to be solved as a matrix. Contains
	 *            all needed distances and dependencies. The first node is
	 *            always the start and the last node always the destination.
	 *            Therefore supports the SOP-instances of the TSP-lib.
	 * @return the best valid solution all runs of the genetic Algorithm could
	 *         find or null, if no valid solution could be found. The solution
	 *         path does not contain the starting point and the destination
	 *         because these will be added by the main-method. Therefore
	 *         contains integers higher or equal than 2.
	 */
	public static List<Integer> runAlg(int[][] matrix) {

		// for explenation of the decrement see GenAlg.dim
		int dim = matrix[0].length - 2;

		// checking for irregular instances
		if (dim < 0) {
			// The given instance has just one node (which is the start and end
			// node), there is no path to find.
			return null;
		} else if (dim == 0) {
			// The given matrix contains only the start and the end node, the
			// path between them is empty.
			return new ArrayList<Integer>();
		} else if (dim == 1) {
			// The given instance contains only one node between the start and
			// the end node.
			// It is the only node in the return-path.
			ArrayList<Integer> solution = new ArrayList<Integer>();
			solution.add(1);
			return solution;
		}

		// Saves the best solution path of all runs through the genetic
		// Algorithm and is returned later.
		int[] bestSolution = new int[dim];

		for (int run = 0; run < RUNS; run++) {
			GenAlg genAlg = new GenAlg();
			int[] aSolution = genAlg.geneticSOP(matrix);
			if (aSolution != null) {
				// aSolution is a valid solution for the SOP
				if (UsefulMethods.compareSolutions(aSolution, bestSolution, matrix)) {
					UsefulMethods.copyPath(aSolution, bestSolution);
				}
			}
		}
		if (bestSolution[0] == 0) {
			// bestSolution was never filled with a real solution and is still
			// in the initial state.
			// No valid solution was found in all runs of the genetic algorithm.
			return null;
		}
		// bestSolution now contains the result and is put into a List of
		// Integers to match the main-method.
		// The starting point (node 0) and the destination (node n) will be
		// added by the main-method and are therefore missing in the list that
		// is returned.
		List<Integer> returnSolution = new ArrayList<Integer>();
		for (int i = 0; i < bestSolution.length; i++) {
			returnSolution.add(bestSolution[i]);
		}
		return returnSolution;
	}

}
