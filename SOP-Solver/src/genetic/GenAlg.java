package genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains a genetic algorithm which solves a given SOP-problem with a single run.
 * 
 * @author Thore Paetsch
 *
 */
public class GenAlg {
	
	/**
	 * dim is NOT exactly the dimension of the SOP-instance but the dimension of the solution paths which
	 * 		are dim long with indices 0 to dim-1 including the numbers (nodes) 1 to dim (= actual DIM -1).
	 * 		The starting node 0 and the destination node dim will be added later in the main-method.
	 */
	private static int dim;
	
	/**
	 * The size of each Generation.
	 */
	private static final int GEN_SIZE = dim;
	
	/**
	 * Number of iterations and also generations.
	 */
	private static final int ITERATIONS = 50 + dim;
	
	/**
	 * The chance that a node in a candidate will mutate.
//	 * MUTATION_RATE of 500 is a chance of 1/500 (=0.002) that a node will mutate.
	 */
	private static final int MUTATION_RATE = 500;
	
	/**
	 * Saves the best found valid solution of all generations in this run.
	 */
	private static int[] bestRunSolution;
	
	/**
	 * Saves the length of the shortest possible path through the graph. The path doesn't need to be a valid solution for the problem.
	 * The value is stored to get a constant value that scales with the lengths of the edges in the given instance.
	 * It is used in the fitness-function.
	 */
	private static int shortestPathLength;
	
	/**
	 * Scales the modification on the fitness-value of a path, if it is a valid solution.
	 * If C is 1.5 for example, the fitness-value is 1.5 times bigger than if it was not valid.
	 */
	private static final double C = 1.5;
	
	/**
	 * The random-generator to be used in the run of the genetic algorithm.
	 */
	private static Random random = new Random();
	
	/**
	 * A Genetic Algorithm that finds a good (not necessarily the best) for a given SOP-Instance.
	 * 
	 * @param matrix
	 * 			the given instance of a SOP-Problem to be solved as a matrix. Contains all needed distances and dependencies.
	 * 			The first node is always the start and the last node always the destination. Therefore supports the SOP-instances of the TSP-lib.
	 * @return the best valid solution-path found or null if no valid path was found.
	 */
	public static int[] geneticSOP(int[][] matrix){
		
		dim = matrix[0].length-2;	//TODO vorher war da -1
		shortestPathLength = calculateShortestPathLength(matrix);
		
		int[][] generation = new int[GEN_SIZE][dim];
		
		//generate the starting-generation
		for(int i=0; i < GEN_SIZE; i++){
			//generating dim numbers that go from 1 to dim (second border is excluding)
			generation[i] = random.ints(dim, 1, dim+1).toArray();	//TODO vorher war hier dim und in den comments dim-1
			//exchange nodes that are multiple times in the path with nodes that are missing
			//after that every node is in the path exactly once.
			deleteMults(generation[i]);
		}
		
		//starting an iteration.
		//each iteration includes rating the current generation and generating a new generation via crossing and mutation.
		for(int iteration=0; iteration < ITERATIONS; iteration++){
			//rating the generation
			double[] fitness = rateGeneration(generation, matrix);
			//summing up all fitness-ratings of the candidates of the current generation.
			//it is needed later on to select a candidate properly.
			double overallFitness = 0;
			for(int i=0; i < fitness.length; i++){
				overallFitness += fitness[i];
			}
			
			//creating a new generation via crossing GEN_SIZE selected pairs and mutation
			int[][] newGeneration = new int[GEN_SIZE][dim];
			for(int pair=0; pair < GEN_SIZE; pair++){
				//selecting a pair of 2 candidates. both candidates can also be the same candidate.
				int candidate1 = selectCandidate(generation, fitness, overallFitness);
				int candidate2 = selectCandidate(generation, fitness, overallFitness);
				//choosing the point in which the candidates are crossed, last index in a path is dim-1.
				//		Note that dim can also be picked as crosspoint, which leads to picking just candidate1 again.
				int crossPoint = random.ints(1, 0, dim+1).findFirst().getAsInt();
				//crossing the candidates, everything left from crossPoint comes from 1, everything else from 2
				int[] newCandidate = new int[dim];
				if(crossPoint==0){
					newCandidate = generation[candidate1];
				} else if(crossPoint==dim){
					newCandidate = generation[candidate2];
				} else {
					for(int i=0; i < newCandidate.length; i++){
						if(i < crossPoint){
							newCandidate[i] = generation[candidate1][i];
						} else {
							newCandidate[i] = generation[candidate2][i];
						}
					}
				}
				//possible mutation of each node in the new candidate
				for(int i=0; i < newCandidate.length; i++){
					if(mutation()){
						//50% chance to get +1, 50% to get -1 on the node
						if(random.nextBoolean()){
							newCandidate[i]++;
						} else {
							newCandidate[i]--;
						}
					}
				}
				//exchange nodes that are multiple times in the path with nodes that are missing
				//after that every node is in the path exactly once.
				deleteMults(newCandidate);
				//putting the new candidate in the new generation
				UsefulMethods.copyPath(newCandidate, newGeneration[pair]);
			}
			//now the old generation is replaced by the new one
			generation = newGeneration;
			//now the next iteration starts
		}
		
		//all iterations are done. bestRunSolultion now includes the best solution from all generations except the last (and current) one.
		//another run of rateGeneration will change that. Note that the actual ratings are not used.
		double[] neverUsedFitness = rateGeneration(generation, matrix);
		
		//now bestRunSolution is the best found solution of the hole run of the genetic algorithm and is returned
		return bestRunSolution;
	}
	
	/**
	 * Rates the fitness of each candidate in a given generation by a defined fitness-function.
	 * Also saves the best valid solution in bestRunSolution.
	 * 
	 * @param generation
	 * 			a given number of candidates. Each candidate is a path which can include the same node multiple times.
	 * @return an array which includes a fitness-value for every candidate of the generation.
	 * 			fitness[i] is the fitness-value of the candidate generation[i].
	 * 			Fitness-values are always positive. The greatest fitness-value is the best.
	 */
	private static double[] rateGeneration(int[][] generation, int[][] matrix){
		double[] fitness = new double[GEN_SIZE];
		
		/*	current Fitness-function is:  length of the shortest path
		 * 								 -----------------------------     * C 
		 * 								  length of the path to rate      |----| -> only if the path is valid
		 */
		
		for(int path = 0; path < GEN_SIZE; path++){
			if(isValid(generation[path], matrix)){
				//the path is a valid solution for the SOP-instance
				fitness[path] = (double)(shortestPathLength / UsefulMethods.pathLength(generation[path], matrix) * C);
				if(UsefulMethods.compareSolutions(generation[path], bestRunSolution, matrix)){
					//a new best path was found
					UsefulMethods.copyPath(generation[path], bestRunSolution);
				}
			} else {
				//the path is not a valid solution
				fitness[path] = (double)(shortestPathLength / UsefulMethods.pathLength(generation[path], matrix));
			}
		}
		return fitness;
	}
	
	/**
	 * Selects a candidate from the given generation. A candidate with a better fitness-value has a greater chance to be picked.
	 * 
	 * @param generation
	 * 			the current generation of candidates.
	 * @param fitness
	 * 			the fitness-values of the candidates from the generation.
	 * @param overallFitness
	 * 			the sum of all fitness-values from the current generation.
	 * @return a selected candidate, defined by its index in generation.
	 */
	private static int selectCandidate(int[][] generation, double[] fitness, double overallFitness){
		double randomNum = random.doubles(1, 0, overallFitness+1).findFirst().getAsDouble();
		double sum = 0;
		for(int candidate = 0; candidate < GEN_SIZE; candidate++){
			sum += fitness[candidate];
			if(sum >= randomNum){
				return candidate;
			}
		}
		//should never be reached
		return 0;
	}
	
	/**
	 * Chooses if a node in a candidate shall mutate or not based on the MUTAION_RATE defined above.
	 * 
	 * @return true if a candidate shall mutate and false otherwise.
	 */
	private static boolean mutation(){
		boolean result = false;
		int randomNum = random.ints(1, 0, MUTATION_RATE+1).findFirst().getAsInt();
		if(randomNum==MUTATION_RATE){
			//the chance to get here should mirror the mutation-rate
			result = true;
		}
		return result;
	}
	
	/**
	 * Checks if the given path is a valid solution for the SOP-instance given by the matrix.
	 * Does not include a check wether there are nodes in the path multiple times. Every path that is tested here
	 * 		is allready checked and reworked by the method deleteMults() and therefore does include every node just once.
	 * 
	 * @param path
	 * 			the path to check, does include every node exactly once.
	 * @param matrix
	 * 			the given SOP-instace for which the path could be a valid solution.
	 * @return true, if the path is a valid solution for the instance and false otherwise.
	 */
	private static boolean isValid(int[] path, int[][] matrix){
		for(int node = 0; node < path.length; node++){
			List<Integer> nodeDeps = new ArrayList<Integer>();
			//putting all nodes that have to be visited before the current node in the list (excluding the starting point and
			//		the destination
			for(int i=1; i < matrix[0].length -1; i++){
				if(matrix[node][i] == -1){
					//i has to be visited before node
					nodeDeps.add(i);
				}
			}
			//now nodeDeps contains all the nodes that have to be visited before the current node
			//if there is a dependency but the current node is the first one in the path, the path is no valid solution
			if(node == 0 && !nodeDeps.isEmpty()){
				return false;
			}
			//if one of the nodes in the list is not visited before the current node in the path the path is no valid solution
			//all fulfilled dependencies are now removed from the list
			for(int i=0; i < node; i++){
				if(nodeDeps.contains(path[i])){
					//the node is included in the path before the current node, so the dependency is fulfilled
					//the cast is necessary because we don't want to remove the element with index i but i itself from the list
					nodeDeps.remove((Integer) i);
				}
			}
			//now all fulfilled dependencies are removed from the list. if there is still a node on the list the path is no valid solution
			if(!nodeDeps.isEmpty()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Exchanges nodes that are in a given path multiple times through nodes that are missing.
	 * Afterwards every node is in the path exactly once.
	 * 
	 * @param path
	 * 			the path to delete multiple nodes from.
	 */
	private static void deleteMults(int[] path){
		//howManyTimes[i] saves how many times node i is in the given path.
		int[] howManyTimes = new int[dim+1];
		for(int i=0; i < path.length; i++){
			howManyTimes[path[i]]++;
		}
		//saving the missing nodes in a list (starting with node 1 because node 0 is the starting point and
		//		not featured in the solution-paths)
		List<Integer> missingNodes = new ArrayList<Integer>();
		for(int i=1; i < howManyTimes.length; i++){
			if(howManyTimes[i] == 0){
				missingNodes.add(i);
			}
		}
		if(!missingNodes.isEmpty()){
			//there are nodes missing and other nodes are multiple times in the path
			boolean[] allready = new boolean[dim+1];
			for(int i=0; i < path.length; i++){
				if(allready[path[i]]){
					//the node is allready in the path before the current position (index i)
					//the node path[i] is replaced by the first node in the list of the missing nodes
					path[i] = missingNodes.remove(0);
				} else {
					//the node was not in path before the current position (index i)
					allready[path[i]] = true;
				}
			}
		}
	}
	
	/**
	 * Calculates the length of the shortest path from the starting point to the destination in the given instance.
	 * The path is not necessarily a valid solution for the SOP-problem.
	 * Implements Dijkstras Algorithm (Source: "Algorithmen und Datenstrukturen"-Lecture by professor Jansen, 2015).
	 * 
	 * @param matrix
	 * 			the given instance of the SOP-problem.
	 * @return the length of the shortest path.
	 */
	private static int calculateShortestPathLength(int[][] matrix){
		int result = 0;
		int n = matrix[0].length;
		List<Integer> path = new ArrayList<Integer>();
		path.add(0);
		List<Integer> notInPath = new ArrayList<Integer>();
		int[] distances = new int[n];
		for(int i=1; i < n; i++){
			notInPath.add(i);
			distances[i] = matrix[0][i];
		}
		while(path.size() != n){
			int k = 0;
			int min = Integer.MAX_VALUE;
			for(int i=0; i < distances.length; i++){
				if(distances[i] < min && notInPath.contains(i)){
					k = i;
					min = distances[i];
				}
			}
			path.add(k);
			notInPath.remove((Integer) k);
			result += min;
			for(int j : notInPath){
				int newDistance = min + matrix[k][j];
				if(newDistance < distances[j]){
					distances[j] = newDistance;
				}
			}
		}
		return result;
	}

}
