package convertSOPFileToArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Offers a method, that converts a given .SOP file into an array. Currently
 * only supports TSPLIB.
 * 
 * @author D. LUCAS
 *
 */
public class parser {
	/* README!:
	 * Wenn man eine eigene Testinstanz schreiben moechte, sie die Felder
	 * DIMENSION: und EDGE_WEIGHT_SECTION mit anhaengendem Zahlenwert noetig.
	 * DIMENSION muss hier der Groesse der Testinstanz entsprechen. Die
	 * Reihenfolge ist zu beachten! Die eigentliche Testinstanz ist dann als
	 * Matrix einzufuegen.
	 * 
	 */

	/**
	 * 
	 * @param pathString
	 *            A path as a String to a .SOP File.
	 * @return The matrix, that was given in the .SOP file in form of a
	 *         two-dimensional array. null, if reading the file failed.
	 * @throws FileNotFoundException
	 *             If the file is not found.
	 */
	public static int[][] parse(String pathString) {
		/* INITIALIZATION */
		File file = new File(pathString);
		int[][] returnArray = null;
		int dimension;

		try {
			Scanner s = new Scanner(file);

			/* Find the dimension in the file so we can initiate the array. */
			while (!s.hasNext("DIMENSION:")) {
				s.next();
			}
			s.next();

			// s.findInLine("DIMENSION:");
			if (s.hasNextInt()) {
				dimension = s.nextInt();
				returnArray = new int[dimension][dimension];
			} else {
				s.close();
				return null;
			}

			// Find the beginning of the matrix. The info text always ends with
			// the EDGE_WEIGHT_SECTION parameter, followed by the corresponding
			// integer.
			while (!s.hasNext("EDGE_WEIGHT_SECTION")) {
				s.next();
			}
			// skip EDGE_WEIGHT_SECTION
			s.next();
			// skip the corresponding integer
			s.next();

			// next should be the matrix that is needed. Read the matrix as long
			// as the array has space left. Array should be exactly as big as
			// there are numbers to be stored.
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					returnArray[i][j] = Integer.parseInt(s.next());
				}
			}

			// close the Scanner.
			s.close();
			// Catch the exception thrown by initiating the Scanner class.
		} catch (FileNotFoundException exc) {
			exc.printStackTrace();
		}

		return returnArray;
	}
}
