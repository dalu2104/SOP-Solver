package oneSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds a valid Solution for a SOP-Instance, given as an array.
 * The instance has to have the first node as fixed starting point and the last node as fixed destination.
 * Therefore supports the SOP-Instances of the TSPLib.
 * @author Thore
 *
 */
public class oneSolution {
	
	/**
	 * 
	 * @param matrix
	 * 			the given SOP-Instance.
	 * @return the solution as an (2-dim.) int-array. solution[0] contains the nodes in the correct order as an int-array,
	 * 			solution[1] contains the length of the tour as a int-array which only contains one element.
	 */
	public int[][] findSolution(int[][] matrix){
		
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
					//if the node was allready picked at this place we don't want it again
					if(!dontBuckets[currentNode].contains(i)){
						int distance = matrix[currentNode][i];
						if(distance != -1 && distance < min){
							min = distance;
							nextNode = i;
						}
					}
				}
			}
			if(min == Integer.MAX_VALUE){
				//no node was found to go on with. we need to go back to the last node and try again with another
				//we put this node (which didn't work out) in the dontBucket of the node we came from
				//		because we don't want to pick it again at the same place
				int lastNode = solution[solCounter-1];
				dontBuckets[lastNode].add(currentNode);
				currentNode = lastNode;
			} else {
				//we can go on
				currentNode = nextNode;
				solution[solCounter] = currentNode;
				solCounter++;
			}
		}
		
		//preparing the solution to return
		int[][] output = new int[2][];
		int[] pathLength = new int[1];
		pathLength[0] = 0;
		
		//calculating the length of the solution-path
		for(int i=0; i<DIM-1; i++){
			int firstNode = solution[i];
			int secondNode = solution[i+1];
			pathLength[0] += matrix[firstNode][secondNode];
		}
		
		output[0] = solution;
		output[1] = pathLength;
		
		return output;
	}

}
