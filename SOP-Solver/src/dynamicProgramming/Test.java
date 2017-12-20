package dynamicProgramming;

import java.util.List;

public class Test {

	public static void main(String[] args) {
		
		int[][] matrix = new int[5][5];
		init(matrix);
		List<Integer> L = DynamicSOP.solveDynamic(matrix);
		L.add(0, 1);
		
		for(int i = 0; i < L.size(); i++){
			System.out.print(L.get(i) + " ");
			if(i < L.size()-1)
				System.out.print("-> ");
		}
		
		
	}

	private static void init(int[][] matrix) {

		matrix[0][0] = 0;
		matrix[0][1] = 3;
		matrix[0][2] = 4;
		matrix[0][3] = 7;
		matrix[0][4] = 100;
		
		matrix[1][0] = -1;
		matrix[1][1] = 0;
		matrix[1][2] = 1;
		matrix[1][3] = 4;
		matrix[1][4] = 0;
		
		matrix[2][0] = -1;
		matrix[2][1] = 1;
		matrix[2][2] = 0;
		matrix[2][3] = 3;
		matrix[2][4] = 0;
		
		matrix[3][0] = -1;
		matrix[3][1] = 4;
		matrix[3][2] = 3;
		matrix[3][3] = 0;
		matrix[3][4] = 0;
		
		matrix[4][0] = -1;
		matrix[4][1] = -1;
		matrix[4][2] = -1;
		matrix[4][3] = -1;
		matrix[4][4] = 0;
		
		
	}

}
