package dynamicProgramming;

import java.util.ArrayList;
import java.util.List;

public class DynamicSOPSolver {
	
	public static List<Integer> solve(int [][] matrix){
		
		List<Integer> solution = new ArrayList<>();
		// get number of vertices
		int n = matrix.length;
		// check how many vertices have to be visited
		// if they are less than 21, we can use the fast algorithm with one big table
		if(n <= 21)
			solution = DynamicSOP.solveDynamic(matrix);
		// if not the slower algorithm with dynamic lists is chosen
		else
			solution = DynamicSOPLists.solveDynamic(matrix);
				
		return solution;
	}
	

}
