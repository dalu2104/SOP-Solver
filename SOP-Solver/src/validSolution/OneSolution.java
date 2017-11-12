package validSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds a valid Solution for a SOP-Instance, given as an array.
 * The instance has to have the first node as fixed starting point and the last node as fixed destination.
 * Therefore supports the SOP-Instances of the TSPLib.
 * @author Thore Paetsch
 *
 */
public class OneSolution {
	
	/**
	 * 
	 * @param matrix
	 * 			the given SOP-Instance.
	 * @return the solution as an (2-dim.) int-array. solution[0] contains the nodes in the correct order as an int-array,
	 * 			solution[1] contains the length of the tour as a int-array which only contains one element.
	 */
	public static List<Integer> findSolution(int[][] matrix){
		
		final int DIM = matrix[0].length;
		
		int[] solution = new int[DIM];
		int solCounter = 0;
		
		int currentNode = 0;
		
		//dontBuckets[i] contains all nodes which had allready been tested as next node from node i without success.
		//they shall not picked again as the node directly after node i.
		List<Integer>[] dontBuckets = new List[DIM];
		for(int i=0; i<DIM; i++){
			dontBuckets[i] = new ArrayList<Integer>();
		}
		
		//putting the starting point into the solution
		solution[solCounter] = currentNode;
		solCounter++;
		
		while(solCounter < DIM){
			int min = Integer.MAX_VALUE;
			int nextNode = 0;
			//searches in all possible Nodes for the shortest distance
			for(int i=0; i<DIM; i++){
				//the destination node is not allowed untill it is the last remained
				if(i < DIM-1 || solCounter == DIM-1){
					//we don't want the node itself but another one
					if(i != currentNode){
						//we don't want a node that we allready have in the solution-path
						if(!inSolution(i, solution, solCounter)){
							//if the node was allready picked at this place we don't want it again
							if(!dontBuckets[currentNode].contains(i)){
								int distance = matrix[currentNode][i];
								//now we check the dependencies
								if(dependenciesCheck(i, solution, solCounter, matrix, DIM)){
									//greedy: we want the shortest possible distance
									if(distance != -1 && distance < min){
										min = distance;
										nextNode = i;
									}
								}
							}
						}
					}
				}
			}
			if(min == Integer.MAX_VALUE){
				//no node was found to go on with. we need to go back to the last node and try again with another
				int lastNode = solution[solCounter-1];
				//we put this node (which didn't work out) in the dontBucket of the node we came from
				//		because we don't want to pick it again at the same place
				dontBuckets[lastNode].add(currentNode);
				//we don't want to keep the don't-list of the node we now leave because everything can be different,
				//		when we come to it again.
				dontBuckets[currentNode].clear();
				currentNode = lastNode;
			} else {
				//we can go on
				currentNode = nextNode;
				solution[solCounter] = currentNode;
				solCounter++;
			}
		}
		
		//modifying to the solution-format of the Main-Class.
		List<Integer> solList = changeToMain(solution);
		
		return solList;
	}
	
	/**
	 * checks if a given node is allready part of the solution-path so far
	 * 
	 * @param node
	 * 			the node to check if it is in the solution-path
	 * @param solution
	 * 			the solution-path as an array
	 * @return true if node is part of the solution-path (till index solCounter-1) and false otherwise.
	 */
	private static boolean inSolution(int node, int[] solution, int solCounter){
		boolean result = false;
		for(int i=0; i<solCounter; i++){
			if(solution[i]==node){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * Checks if another node has to be added before this one concerning the dependencies.
	 * If so, adding this node in this place will never lead to a valid solution.
	 * 
	 * @param i
	 * 			the node that could be added in the solution.
	 * @param solution
	 * 			the solution-path so far, currentNode (and last node of the path so far) is solution[solCounter-1].
	 * @param solCounter
	 * 			the position in the solution-path on which node i would be added if it leads to a valid solution.
	 * @param matrix
	 * 			the matrix containing the dependencies. If node i has to be before node j in the path then matrix[j][i]=-1.
	 * @return true, if all dependencies are fullfilled if i is added as the next node in the solution-path and false otherwise.
	 */
	private static boolean dependenciesCheck(int i, int[] solution, int solCounter, int[][] matrix, final int DIM){
		boolean result = true;
		for(int k=0; k<DIM; k++){
			//checking if the solution-path allready contains node k
			boolean kInSolution = false;
			for(int l=0; l<solCounter; l++){
				if(solution[l] == k){
					kInSolution = true;
				}
			}
			if(!kInSolution){
				//node k is not in the solution so far
				if(matrix[i][k]==-1){
					//but node k has to be in the solution-path before node i.
					//adding node i now wont lead to a valid solution
					result = false;
				}
			}
		}
		return result;
	}
	
	/**
	 * Transform the solution to a format that fits the format of the Main-Method.
	 * 
	 * @param solution
	 * 			the solution as an array of integers. nodes beginning by 0.
	 * @return the solution as an ArrayList. nodes beginning by 2, because 1 and n will be put into the solution by the Main.
	 */
	private static List<Integer> changeToMain(int[] solution){
		List<Integer> solList = new ArrayList<Integer>();
		//the start and the end-node will be put into the solution by the Main.
		for(int i=1; i < solution.length-1; i++){
			//+1 on all nodes, because they shall start with 1, not with 0.
			solution[i]++;
			//putting the nodes in the list
			solList.add(solution[i]);
		}
		return solList;
	}

}