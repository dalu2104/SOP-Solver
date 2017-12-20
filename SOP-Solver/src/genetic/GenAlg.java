package genetic;

import java.nio.file.Path;
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
	private static int genSize;
	
	/**
	 * Number of iterations and also generations.
	 */
	private static int iterations;
	
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
	 * The sum of the shortest value of each column (without 0, -1 and 1.000.000).
	 * It is used in the fitness function to get a constant value which scales with the length
	 * 		of the edges in the given instance.
	 * Note that it should be shorter than every actual path through the graph.
	 */
	private static int shortDistance;
	
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
		
		System.out.println("Run-Start"); //TODO delete
		dim = matrix[0].length-2;
		genSize = dim;
		iterations = 50 + dim;
		shortDistance = calculateShortDistance(matrix);
		System.out.println("shortestDistance: " + shortDistance);//TODO delete
		
		int[][] generation = new int[genSize][dim];
		
		//generate the starting-generation
		for(int i=0; i < genSize; i++){
			//generating dim numbers that go from 1 to dim (second border is excluding)
			generation[i] = random.ints(dim, 1, dim+1).toArray();
			//exchange nodes that are multiple times in the path with nodes that are missing
			//after that every node is in the path exactly once.
			deleteMults(generation[i]);
		}
		
		//starting an iteration.
		//each iteration includes rating the current generation and generating a new generation via crossing and mutation.
		for(int iteration=0; iteration < iterations; iteration++){
			System.out.println("Generation: ");//TODO delete
			//rating the generation
			double[] fitness = rateGeneration(generation, matrix);
			//summing up all fitness-ratings of the candidates of the current generation.
			//it is needed later on to select a candidate properly.
			double overallFitness = 0;
			for(int i=0; i < fitness.length; i++){
				overallFitness += fitness[i];
			}
			
			//creating a new generation via crossing GEN_SIZE selected pairs and mutation
			int[][] newGeneration = new int[genSize][dim];
			for(int pair=0; pair < genSize; pair++){
				//selecting a pair of 2 candidates. both candidates can also be the same candidate.
				int candidate1 = selectCandidate(generation, fitness, overallFitness);
				int candidate2 = selectCandidate(generation, fitness, overallFitness);
				//choosing the point in which the candidates are crossed, last index in a path is dim-1.
				//		Note that dim can also be picked as crossing point, which leads to picking just candidate1 again.
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
		double[] fitness = new double[genSize];
		
		/*	current Fitness-function is:  		shortDistance
		 * 								 -----------------------------     * C 
		 * 								  length of the path to rate      |----| -> only if the path is valid
		 */
		
		for(int path = 0; path < genSize; path++){
			if(isValid(generation[path], matrix)){
				System.out.println("rateGeneration: Valid found"); //TODO delete
				//the path is a valid solution for the SOP-instance
				fitness[path] = (double) shortDistance / UsefulMethods.pathLength(generation[path], matrix) * C;
				//bestRunSolution is initialized if it wasn't before (because it is the first time that a valid solution is found
				if(bestRunSolution == null){
					bestRunSolution = new int[dim];
				}
				if(UsefulMethods.compareSolutions(generation[path], bestRunSolution, matrix)){
					//a new best path was found
					UsefulMethods.copyPath(generation[path], bestRunSolution);
				}
			} else {
				//the path is not a valid solution
				fitness[path] = (double) shortDistance / UsefulMethods.pathLength(generation[path], matrix);
			}
			System.out.println("Path: "+goString(generation[path])+", Rating: "+fitness[path]);//TODO delete
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
		//getting a random double between 0.0 and overallFitness, nextDouble gives a Double between 0.0 and 1.0
		double randomNum = random.nextDouble() * overallFitness;
		double sum = 0.0;
		for(int candidate = 0; candidate < genSize; candidate++){
			sum += fitness[candidate];
			if(sum >= randomNum){
				//the random number lies in the range of the current candidate
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
			//		the destination)
			for(int i=1; i < matrix[0].length -1; i++){
				if(matrix[path[node]][i] == -1){
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
					nodeDeps.remove((Integer) path[i]);
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
	 * Sorts out nodes that mutated over the normal node-range. For example if dim = 7 there should only be
	 * 		nodes from 1 to 7 in the path. But a node of 7 can mutate to 8 and a node of 1 can mutate to 0.
	 * 		Therefore these nodes will be exchanged here as well.
	 * 
	 * @param path
	 * 			the path to delete multiple nodes from.
	 */
	private static void deleteMults(int[] path){
		//howManyTimes[i] saves how many times node i is in the given path. note that there can be the node
		//		path.length +1 because the normally highest node (which should be path.length) can mutate.
		int[] howManyTimes = new int[path.length +2];
		for(int i=0; i < path.length; i++){
			howManyTimes[path[i]] = howManyTimes[path[i]] +1;
		}
		//saving the missing nodes in a list (starting with node 1 because node 0 is the starting point and
		//		not featured in the solution-paths)
		List<Integer> missingNodes = new ArrayList<Integer>();
		for(int i=1; i < howManyTimes.length -1; i++){
			//we leave out the starting node (with index 0) and the last index (path.length+1) which is only
			//		there by failure mutation
			if(howManyTimes[i] == 0){
				missingNodes.add(i);
			}
		}
		if(!missingNodes.isEmpty()){
			//there are nodes missing and other nodes are multiple times in the path
			boolean[] allready = new boolean[path.length +2];
			for(int i=0; i < path.length; i++){
				if(allready[path[i]] || path[i] > path.length || path[i] == 0){
					//the node is allready in the path before the current position (index i) or too big or too small
					//		because of mutation
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
	 * Calculates the sum of the shortest value of each column (without 0, -1 and 1.000.000).
	 * The 1.000.000 must be left out because the last column does only include 1.000.000 and 0
	 * 		in many instances. In the instances, it is only used to avoid picking the distance
	 * 		between the starting node and the destination.
	 * 
	 * @param matrix
	 * 			the given instance of the SOP-problem.
	 * @return a sum of short edges in the graph.
	 * 			Note that it should be shorter than every actual path through the graph.
	 */
	private static int calculateShortDistance(int[][] matrix){
		int result = 0;
		int allDim = matrix[0].length;
		for(int i=0; i < allDim; i++){
			int min = Integer.MAX_VALUE;
			for(int j=0; j < allDim; j++){
				int value = matrix[i][j];
				if(value != -1 && value != 0 && value != 1000000 && value < min){
					min = value;
				}
			}
			if(min != Integer.MAX_VALUE){
				result += min;
			}
		}
		return result;
	}
	
	//TODO delete
	private static String goString(int[] array){
		String result = "";
		for(int i=0; i < array.length; i++){
			result += array[i];
		}
		return result;
	}

}
