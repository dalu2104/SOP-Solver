package simulatedAnnealing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import convertSOPFileToArray.Parser;
import execution.Exe;
import simulatedAnnealing.Utility;

public class UtilityTest {
	// Path to test instances we will be testing.
	private String pathToTestInstances;
	// Matrix that will be used.
	private int[][] matrix;

	@Before
	public void initialize() {
		// Transforms the path to this project and redirects it to the folder
		// with the test instances.
		String path = new File("").getAbsolutePath();
		// Splits string by file separators according to the OS.
		String pattern = Pattern.quote(System.getProperty("file.separator"));
		String[] segments = path.split(pattern);
		// Replace "SOP-Solver" with "SOP-Instanzen".
		segments[segments.length - 1] = "SOP-Instanzen";
		// Fill path with corrected input
		path = "";
		for (String i : segments) {
			path += i;
			path += File.separator;
		}
		// path is done and now directs to the "SOP-Instanzen" folder.
		pathToTestInstances = path;
	}

	// Method switchTwo.
	@Test
	public void switchTwoTest() {
		List<Integer> testList = new ArrayList<Integer>();
		testList.add(1);
		testList.add(2);

		Utility.switchTwo(testList, 0, 1);

		assertEquals(2, testList.get(0).intValue());
		assertEquals(1, testList.get(1).intValue());
	}

	// Method isValid.
	@Test
	public void isValidTest() {
		matrix = parse(pathToTestInstances + "esc07.sop");
		List<Integer> listTrue = new ArrayList<Integer>();
		List<Integer> listFalse = new ArrayList<Integer>();
		// creating a false and a true solution according to this test instance
		// esc07.sop.
		// true first.
		listTrue.add(1);
		listTrue.add(4);
		listTrue.add(3);
		listTrue.add(2);
		listTrue.add(7);
		listTrue.add(6);
		listTrue.add(5);
		// false. 8 cannot come before 2.
		listFalse.add(7);
		listFalse.add(1);
		listFalse.add(4);
		listFalse.add(3);
		listFalse.add(2);
		listFalse.add(6);
		listFalse.add(5);

		// check
		assertTrue(Utility.isValid(listTrue, matrix));
		assertFalse(Utility.isValid(listFalse, matrix));
	}

	// Method cost.
	@Test
	public void costTest() {
		matrix = parse(pathToTestInstances + "esc07.sop");
		List<Integer> listCost = new ArrayList<Integer>();
		listCost.add(1);
		listCost.add(4);
		listCost.add(3);
		listCost.add(2);
		listCost.add(7);
		listCost.add(6);
		listCost.add(5);

		assertEquals(calculate(matrix, listCost), Utility.cost(listCost, matrix));
	}

	// Method copyList.
	@Test
	public void copyListTest() {
		List<Integer> listToCopy = new ArrayList<Integer>();
		List<Integer> listToCopyTo = new ArrayList<Integer>();

		listToCopy.add(1);
		listToCopyTo = Utility.copyList(listToCopy);

		assertEquals(listToCopy.get(0), listToCopyTo.get(0));
	}

	/////////////////////////////////////////////// HELPING METHODS
	// Calls the Parser that the parser that the main-class uses.
	/** Parses given file and returns it as a matrix. */
	private int[][] parse(String file) {
		int[][] matrix;
		try {
			matrix = Parser.parse(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file);
			return null;
		}
		return matrix;
	}

	// Calls the calculateCost method from the main-class.
	/** Calculates cost according to solution and matrix. */
	private int calculate(int[][] matrix, List<Integer> solutionPath) {
		if (solutionPath != null) {
			return Exe.calculateCost(matrix, solutionPath);
		}
		return -1;
	}

}
