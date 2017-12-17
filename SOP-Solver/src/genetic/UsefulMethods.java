package genetic;

/**
 * A util-class that contains methods that are used by both other classes in the package.
 * 
 * @author Thore Paetsch
 *
 */
public class UsefulMethods {
	
	/**
	 * Compares two given solutions for the SOP-instance and checks which one is shorter.
	 * 
	 * @param solution1, solution2
	 * 			given solutions for the SOP-instance given with the param matrix which have to be compared.
	 * @param matrix
	 * 			an SOP-instance which fits in the pattern of the SOP-instances of the TSP-lib.
	 * @return true if solution1 is better or equal than solution2 or if solution2 is empty and false otherwise.
	 */
	public static boolean compareSolutions(int[] solution1, int[] solution2, int[][] matrix){
		boolean result = true;
		if(solution2[0]!=0){
			//solution2 does contain a solution-path (0 is the initial value)
			if(pathLength(solution2, matrix) < pathLength(solution1, matrix)){
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * Calculates the length of the given path based on the distances of the given matrix.
	 * 
	 * @param path
	 * 			path of which to calculate the length.
	 * @param matrix
	 * 			an SOP-instance which fits in the pattern of the SOP-instances of the TSP-lib.
	 * @return the length of the path which is the combined length of every edge between the nodes of the path.
	 */
	public static int pathLength(int[] path, int[][] matrix){
		int pathLength = 0;
		for(int i=0; i < path.length -1; i++){
			pathLength += matrix[path[i]][path[i+1]];
		}
		return pathLength;
	}
	
	/**
	 * Copies the first Path to another variable.
	 * 
	 * @param target
	 * 			the path given as an array of integers that has to be copied.
	 * @param destination
	 * 			the destination which is also an array of integers.
	 */
	public static void copyPath(int[] target, int[] destination){
		for(int i=0; i<target.length; i++){
			destination[i] = target[i];
		}
	}

}
