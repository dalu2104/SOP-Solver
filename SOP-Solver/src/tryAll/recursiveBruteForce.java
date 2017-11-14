package tryAll;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds the perfect Solution for a given SOP-Instance.
 * Implements the Brute-Force-Method via recursion.
 * The instance has to have the first node as fixed starting point and the last node as fixed destination.
 * Therefore supports the SOP-Instances of the TSPLib.
 * @author Thore Paetsch
 *
 */
public class recursiveBruteForce {
	
	/**
	 * Finds the perfect Solution for a given SOP-Instance via the Brute-Force-Method.
	 * Calculates every possible path and picks the shortest one.
	 * Calls the recursive method "recursion".
	 * 
	 * @param weights
	 * 			the given SOP-Instance of the TSP-Lib as matrix of edge-lengths (weights) and dependencies.
	 * @return the perfect result as List of node-numbers. Does not contain the starting node (which is always the first)
	 * 			and the destination (which is always the last node). They will be added by the Main-Class.
	 * 			Therefore Nodes beginning with 2.
	 */
	public static List<Integer> perfectResult(int[][] weights){
		matrix = weights;
		//the dimension without the destination
		DIM = weights[0].length;
		bestPath = new ArrayList<Integer>();
		bestLength = Integer.MAX_VALUE;
		List<Integer> curPath = new ArrayList<Integer>();
		int curLength = 0;
		recursion(curPath, curLength);
		//adding +1 to all paths, because the Main-method wants the node-numbers to start with 1.
		for(int i=0; i < bestPath.size(); i++){
			int value = bestPath.get(i).intValue() +1;
			bestPath.set(i, value);
		}
		return bestPath;
	}
	
	/**
	 * Picks all nodes that can be picked to lead to a valid solution.
	 * If the path is not complete, calls itself again to pick more nodes and complete the path.
	 * If the path is complete, checks the Length of it. If it is the current Minimum, saves the path and the length.
	 * @param curPath
	 * 			the current solution-path so far.
	 * @param curLength
	 * 			the length the current solution path has so far.
	 */
	private static void recursion(List<Integer> curPath, int curLength){
		System.out.println(curPath.toString());///////////////////////////////////////print-line-debugging
		for(int i=0; i<DIM; i++){
			if(validPath(i, curPath)){
				if(!curPath.isEmpty()){
					curLength += matrix[curPath.get(curPath.size()-1)][i];
				}
				curPath.add(i);
				//checking if the solution-Path is complete.
				//	it is complete with DIM-1 nodes (because starting point and destination have to miss)
				if(curPath.size()<DIM){
					//cur-Path is not a complete solution-Path
					recursion(curPath, curLength);
				} else {
					//cur-Path is a complete solution-Path
					if(curLength < bestLength){
						//update the best result yet
						bestLength = curLength;
						bestPath = curPath;
					}
				}
				curPath.remove(curPath.size()-1);
			}
		}
	}
	
	/**
	 * Checks if adding a given node to a given solution-path can lead to a valid solution for the SOP-problem.
	 * Contains checking if the node is not allready in the path and if all dependencies are fullfilled after adding it.
	 * All dependencies for the node are fullfilled, if every node that has to be visited before the node to check are allready in the path.
	 * If node i has to be visited before the node, matrix[node][i] is -1.
	 * Also ensures, that the first node is always the start of a path and the last node always the destination.
	 * 
	 * @param node
	 * 			the given node that is eventually added to the solution-path.
	 * @param curPath
	 * 			the given solution-path to check.
	 * @return true, adding the node to the path can lead to a valid solution for the problem.
	 */
	private static boolean validPath(int node, List<Integer> curPath){
		boolean result = true;
		if(!curPath.isEmpty()){
			if(curPath.size()==DIM-1){
				//only the destination is missing
				if(node != DIM-1){
					result = false;
				}
			} else {
				//there are still more nodes than the destination missing
				if(node == DIM-1){
					//we don't want the destination node if we don't have all the other nodes in the path
					result = false;
				} else if(node == 0){
					//we don't want the starting point unless curPath is empty
					result = false;
				} else if(curPath.contains(node)){
					//checking if the node is allready in the path
					result = false;
				} else {
					//checking the dependencies
					for(int i=0; i<DIM; i++){
						if(matrix[node][i]==-1){
							if(!curPath.contains(i)){
								result=false;
							}
						}
					}
				}
			}
		} else {
			//curPath is empty, only the starting point is allowed
			if(node != 0){
				result = false;
			}
		}
		return result;
	}
	
	private static int[][] matrix;
	private static int DIM;
	private static List<Integer> bestPath;
	private static int bestLength;

}
