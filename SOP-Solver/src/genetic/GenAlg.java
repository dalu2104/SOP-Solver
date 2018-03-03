package genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains a genetic algorithm which solves a given SOP-problem with a single
 * run.
 * 
 * @author Thore Paetsch
 *
 */
public class GenAlg {

	/**
	 * dim is NOT exactly the dimension of the SOP-instance but the dimension of
	 * the solution paths which are dim long with indices 0 to dim-1 including
	 * the numbers (nodes) 1 to dim (= actual DIM -1). The starting node 0 and
	 * the destination node dim will be added later in the main-method.
	 */
	private int dim;

	/**
	 * The size of each Generation.
	 */
	private int genSize;

	/**
	 * Number of iterations and also generations.
	 */
	private int iterations;

	/**
	 * The chance that a node in a candidate will mutate. MUTATION_RATE of 500
	 * is a chance of 1/500 (=0.002) that a node will mutate. MUTATION_RATE of
	 * 200 is a chance of 1/200 (=0.005) that a node will mutate.
	 */
	private final int MUTATION_RATE = 200;

	/**
	 * Saves the best found valid solution of all generations in this run.
	 */
	private int[] bestRunSolution;

	/**
	 * Saves the path length of the bestRunSolution.
	 */
	private int bestRunSolutionLength;

	/**
	 * Scales the modification on the fitness-value of a path, if it is a valid
	 * solution. If C is 1.5 for example, the fitness-value is 1.5 times bigger
	 * than if it was not valid.
	 */
	private final double C = 2.5;

	/**
	 * The random-generator to be used in the run of the genetic algorithm.
	 */
	private Random random = new Random();

	/**
	 * A Genetic Algorithm that finds a good (not necessarily the best) solution
	 * for a given SOP-Instance.
	 * 
	 * @param matrix
	 *            the given instance of a Sequential Ordering Problem to be
	 *            solved as a matrix. Contains all needed distances and
	 *            dependencies. The first node is always the start and the last
	 *            node always the destination. Therefore supports the
	 *            SOP-instances of the TSP-lib.
	 * @return the best valid solution-path found or null if no valid path was
	 *         found.
	 */
	public int[] geneticSOP(int[][] matrix) {

		dim = matrix[0].length - 2;
		genSize = 4 * dim;
		iterations = 1000 * dim;

		int[][] generation = new int[genSize][dim];

		// Generate the starting-generation.
		for (int i = 0; i < genSize; i++) {
			// Generating dim numbers that go from 1 to dim (second border is
			// excluding).
			generation[i] = random.ints(dim, 1, dim + 1).toArray();
			// Exchange nodes that are multiple times in the path with nodes
			// that are missing.
			// After that every node is in the path exactly once.
			deleteMults(generation[i]);
		}

		// Starting an iteration.
		// Each iteration includes rating the current generation and generating
		// a new generation via crossing and mutation.
		for (int iteration = 0; iteration < iterations; iteration++) {
			// Rating the generation.
			double[] fitness = rateGeneration(generation, matrix);
			// Summing up all fitness-ratings of the candidates of the current
			// generation. They are needed later on to select a candidate
			// properly.
			double overallFitness = 0;
			for (int i = 0; i < fitness.length; i++) {
				overallFitness += fitness[i];
			}

			// Creating a new generation via crossing GEN_SIZE selected pairs
			// and mutation.
			int[][] newGeneration = new int[genSize][dim];
			for (int pair = 0; pair < genSize; pair++) {
				// Selecting a pair of 2 candidates. Both candidates can also be
				// the same candidate.
				int candidate1 = selectCandidate(generation, fitness, overallFitness);
				int candidate2 = selectCandidate(generation, fitness, overallFitness);
				// Choosing the point in which the candidates are crossed, last
				// index in a path is dim-1.
				// Note that dim can also be picked as crossing point, which
				// leads to picking just candidate1 again.
				int crossPoint = random.ints(1, 0, dim + 1).findFirst().getAsInt();
				// Crossing the candidates, everything left from crossPoint
				// comes from 1, everything else from 2.
				int[] newCandidate = new int[dim];
				if (crossPoint == 0) {
					newCandidate = generation[candidate1];
				} else if (crossPoint == dim) {
					newCandidate = generation[candidate2];
				} else {
					for (int i = 0; i < newCandidate.length; i++) {
						if (i < crossPoint) {
							newCandidate[i] = generation[candidate1][i];
						} else {
							newCandidate[i] = generation[candidate2][i];
						}
					}
				}
				// Possible mutation of each node in the new candidate.
				for (int i = 0; i < newCandidate.length; i++) {
					if (mutation()) {
						// There is a mutation.
						if (i == 0) {
							// The node is the first one, exchange it with the
							// next node.
							int mutateNode = newCandidate[i];
							newCandidate[i] = newCandidate[i + 1];
							newCandidate[i + 1] = mutateNode;
						} else if (i == newCandidate.length - 1) {
							// The node is the last one, exchange it with the
							// previous node.
							int mutateNode = newCandidate[i];
							newCandidate[i] = newCandidate[i - 1];
							newCandidate[i - 1] = mutateNode;
						} else {
							// 50% chance to change with the next neighbor, 50%
							// to change with the previous.
							if (random.nextBoolean()) {
								// Exchange it with the next node.
								int mutateNode = newCandidate[i];
								newCandidate[i] = newCandidate[i + 1];
								newCandidate[i + 1] = mutateNode;
							} else {
								// Exchange it with the previous node.
								int mutateNode = newCandidate[i];
								newCandidate[i] = newCandidate[i - 1];
								newCandidate[i - 1] = mutateNode;
							}
						}
					}
				}
				// Exchange nodes that are multiple times in the path with nodes
				// that are missing. After that every node is in the path
				// exactly once.
				deleteMults(newCandidate);
				// Putting the new candidate in the new generation.
				UsefulMethods.copyPath(newCandidate, newGeneration[pair]);
			}
			// Now the old generation is replaced by the new one.
			generation = newGeneration;
			// Now the next iteration starts.
		}

		// All iterations are done. bestRunSolultion now includes the best
		// solution from all generations except the last (and current) one.
		// another run of rateGeneration will change that. Note that the actual
		// ratings are not used.
		@SuppressWarnings("unused")
		double[] neverUsedFitness = rateGeneration(generation, matrix);

		// Now bestRunSolution is the best found solution of the hole run of the
		// genetic algorithm and is returned.
		return bestRunSolution;
	}

	/**
	 * Rates the fitness of each candidate in a given generation by a defined
	 * fitness-function. Also saves the best valid solution in bestRunSolution.
	 * 
	 * @param generation
	 *            a given number of candidates. Each candidate is a path which
	 *            can include the same node multiple times.
	 * @return an array which includes a fitness-value for every candidate of
	 *         the generation. fitness[i] is the fitness-value of the candidate
	 *         generation[i]. Fitness-values are always positive. The greatest
	 *         fitness-value is the best.
	 */
	private double[] rateGeneration(int[][] generation, int[][] matrix) {
		double[] fitness = new double[genSize];

		/*
		 * Current Fitness-function is: 			   1
		 * 								----------------------------- * C
		 * 								  length of the path to rate
		 * 															 |----| -> only if the path is valid
		 */

		for (int path = 0; path < genSize; path++) {
			int pathLength = UsefulMethods.pathLength(generation[path], matrix);
			if (isValid(generation[path], matrix)) {
				// The path is a valid solution for the SOP-instance.
				fitness[path] = (double) 1 / pathLength * C;
				if (bestRunSolution == null) {
					// It is the first time that a valid solution is found.
					bestRunSolution = new int[dim];
					UsefulMethods.copyPath(generation[path], bestRunSolution);
					bestRunSolutionLength = pathLength;
				} else if (bestRunSolutionLength > pathLength) {
					// A new best path was found.
					UsefulMethods.copyPath(generation[path], bestRunSolution);
					bestRunSolutionLength = pathLength;
				}
			} else {
				// The path is not a valid solution.
				fitness[path] = (double) 1 / pathLength;
			}
		}
		return fitness;
	}

	/**
	 * Selects a candidate from the given generation. A candidate with a better
	 * fitness-value has a greater chance to be picked.
	 * 
	 * @param generation
	 *            the current generation of candidates.
	 * @param fitness
	 *            the fitness-values of the candidates from the generation.
	 * @param overallFitness
	 *            the sum of all fitness-values from the current generation.
	 * @return a selected candidate, defined by its index in generation.
	 */
	private int selectCandidate(int[][] generation, double[] fitness, double overallFitness) {
		// Getting a random double between 0.0 and overallFitness, nextDouble
		// gives a Double between 0.0 and 1.0.
		double randomNum = random.nextDouble() * overallFitness;
		double sum = 0.0;
		for (int candidate = 0; candidate < genSize; candidate++) {
			sum += fitness[candidate];
			if (sum >= randomNum) {
				// The random number lies in the range of the current candidate.
				return candidate;
			}
		}
		// Should never be reached.
		return 0;
	}

	/**
	 * Chooses if a node in a candidate shall mutate or not based on the
	 * MUTAION_RATE defined above.
	 * 
	 * @return true if a candidate shall mutate and false otherwise.
	 */
	private boolean mutation() {
		boolean result = false;
		int randomNum = random.ints(1, 0, MUTATION_RATE + 1).findFirst().getAsInt();
		if (randomNum == MUTATION_RATE) {
			// The chance to get here should mirror the mutation-rate.
			result = true;
		}
		return result;
	}

	/**
	 * Checks if the given path is a valid solution for the SOP-instance given
	 * by the matrix. Does not include a check whether there are nodes in the
	 * path multiple times. Every path that is tested here is already checked
	 * and reworked by the method deleteMults() and therefore does include every
	 * node just once.
	 * 
	 * @param path
	 *            the path to check, does include every node exactly once.
	 * @param matrix
	 *            the given SOP-instance for which the path could be a valid
	 *            solution.
	 * @return true, if the path is a valid solution for the instance and false
	 *         otherwise.
	 */
	private boolean isValid(int[] path, int[][] matrix) {
		for (int i = 0; i < path.length; i++) {
			for (int j = 0; j < i; j++) {
				// matrix[a][b] == -1 if b has to be visited before a.
				if (matrix[path[j]][path[i]] == -1) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Exchanges nodes that are in a given path multiple times through nodes
	 * that are missing. Afterwards every node is in the path exactly once.
	 * 
	 * @param path
	 *            the path to delete multiple nodes from.
	 */
	private void deleteMults(int[] path) {
		// howManyTimes[i] saves how many times node i is in the given path.
		int[] howManyTimes = new int[path.length + 1];
		for (int i = 0; i < path.length; i++) {
			howManyTimes[path[i]] = howManyTimes[path[i]] + 1;
		}
		// Saving the missing nodes in a list (starting with node 1 because node
		// 0 is the starting point and not featured in the solution-paths).
		List<Integer> missingNodes = new ArrayList<Integer>();
		for (int i = 1; i < howManyTimes.length; i++) {
			// We leave out the starting node (with index 0).
			if (howManyTimes[i] == 0) {
				missingNodes.add(i);
			}
		}
		if (!missingNodes.isEmpty()) {
			// There are nodes missing and other nodes are multiple times in the
			// path.
			boolean[] allready = new boolean[path.length + 2];
			for (int i = 0; i < path.length; i++) {
				if (allready[path[i]] || path[i] > path.length || path[i] == 0) {
					// The node is already in the path before the current
					// position (index i) or too big or too small
					// because of mutation.
					// The node path[i] is replaced by the first node in the
					// list of the missing nodes.
					path[i] = missingNodes.remove(0);
				} else {
					// The node was not in path before the current position
					// (index i).
					allready[path[i]] = true;
				}
			}
		}
	}

	/**
	 * Only used for testing.
	 * 
	 * @param array
	 *            the array to print.
	 * @return a String representation of the content of the array
	 */
	@SuppressWarnings("unused")
	private String arrayToString(int[] array) {
		String result = "";
		for (int i = 0; i < array.length; i++) {
			result += array[i] + " ";
		}
		return result;
	}

}
