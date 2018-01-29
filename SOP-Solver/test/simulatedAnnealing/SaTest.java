package simulatedAnnealing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import convertSOPFileToArray.Parser;
import execution.Exe;
import simulatedAnnealing.Sa;

public class SaTest {
	// Path to test instances we will be testing.
	private String pathToTestInstances;
	// matrix we work with
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

	@Test
	public void noPathTest() {
		matrix = parse(pathToTestInstances + "zNoPath.sop");
		assertNull(Sa.simulatedAnnealing(matrix, true).getSolution());
	}

	@Test
	public void nullTest() {
		matrix = parse(pathToTestInstances + "zNull.sop");
		assertEquals(0, calculate(matrix, Sa.simulatedAnnealing(matrix, true).getSolution()));
	}

  	@Test
	public void onlyOnePathTest() {
		matrix = parse(pathToTestInstances + "zOnlyOnePath.sop");
		assertEquals(6, calculate(matrix, Sa.simulatedAnnealing(matrix, true).getSolution()));
	}

	@Test
	public void onlyStartStopTest() {
		matrix = parse(pathToTestInstances + "zOnlyStartStop.sop");
		assertEquals(5, calculate(matrix, Sa.simulatedAnnealing(matrix, true).getSolution()));
	}

	@Test
	public void startStopDistancesTest() {
		matrix = parse(pathToTestInstances + "zStartStopDistances.sop");
		assertEquals(3, calculate(matrix, Sa.simulatedAnnealing(matrix, true).getSolution()));
	}
	
	@Test
	public void oneNodeExlStartStopTest() {
		matrix = parse(pathToTestInstances + "zOneNodeExlStartStop.sop");
		assertEquals(3, calculate(matrix, Sa.simulatedAnnealing(matrix, true).getSolution()));
	}
	
	@Test
	public void sopInstancesTest() {
		matrix = parse(pathToTestInstances + "esc07.sop");
		assertNotNull(Sa.simulatedAnnealing(matrix, true));

		matrix = parse(pathToTestInstances + "br17.10.sop");
		assertNotNull(Sa.simulatedAnnealing(matrix, true));
	}

	//////////////////////////HELPING METHODS
	// Calls the Parser that the parser that the main-class uses.
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
	private int calculate(int[][] matrix, List<Integer> solutionPath) {
		if (solutionPath != null) {
			return Exe.calculateCost(matrix, solutionPath);
		}
		return -1;
	}

}

