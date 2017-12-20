package validSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Add simple algorithm that solves an SOP problem in indefinite runtime.
 * 
 * @author D. LUCAS
 *
 */
public class Simple {

	/**
	 * Implementation of the algorithm specified in README.txt.
	 * 
	 * @param A
	 *            given Matrix that contains the graph and the corresponding
	 *            distances.
	 */
	public static List<Integer> firstIdea(int[][] A) {
		// Initialization of needed variables. Array.length works, because we
		// can assume that it is a quadratic array.
		int CurNumDeps = -1;
		int vertMaxDeps = -1;
		List<Integer> B = new ArrayList<Integer>();
		/*
		 * Number of vertexes that depend on a vertex are saved in this array.
		 * Vertexes that have most dependent other vertexes will be added first
		 * to the Tour. If two vertexes have the same amount of dependencies we
		 * check if they depend on each other and the vertex that needs to come
		 * first will be taken first into the tour. If they don't, we will just
		 * take the first on that is found in the matrix.
		 */
		int[] depsPerVert = new int[A.length];
		for (int i = 0; i < depsPerVert.length; i++) {
			if ((i == 0) || (i == depsPerVert.length - 1)) {
				depsPerVert[i] = -1;
			} else {
				depsPerVert[i] = 0;
			}
		}

		/*
		 * Counting the number of vertexes that need to come after a vertex i.
		 * We use the definition, that if A[i][j] =-1 then j must come before i.
		 * So we count per column, how many vertexes depend on a vertex.
		 * Starting at 1 and Ending at n-2 because 0 is the Start and n-1 is the
		 * end vertex.
		 */
		for (int v = 1; v < A.length - 1; v++) {
			for (int i = 1; i < A.length - 1; i++) {
				if (A[i][v] == -1) {
					depsPerVert[v]++;
				}
			}
		}

		/*
		 * Searching for the vertexes with the most vertexes that depend on it,
		 * as described above. There are n-Start-End vertexes we need to add to
		 * the tour so A.length-2.
		 */
		while (B.size() < A.length - 2) {
			for (int v = 1; v < A.length - 1; v++) {
				if (depsPerVert[v] > CurNumDeps) {
					CurNumDeps = depsPerVert[v];
					vertMaxDeps = v;
				} else {
					if (CurNumDeps != -1) {
						if (depsPerVert[v] == CurNumDeps) {
							if (A[vertMaxDeps][v] == -1) {
								vertMaxDeps = v;
							}
						}
					}
				}
			}
			// add vertex with max dependencies to list and decrement accordingly.
			B.add(vertMaxDeps);
			depsPerVert[vertMaxDeps] = -1;
			CurNumDeps = -1;
			vertMaxDeps = -1;
		}
		return B;
	}
}
