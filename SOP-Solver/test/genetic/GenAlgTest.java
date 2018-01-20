package genetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import convertSOPFileToArray.parser;
import execution.Exe;
import genetic.StartGenAlg;

public class GenAlgTest {
	// Path to test instances we will be testing.
	private String pathToTestInstances;

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
	
	@Test(expected = FileNotFoundException.class)
	public void noRealFileTest(){
		
		int[][] matrix;
		matrix = parse(pathToTestInstances + "noRealFile.sop");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void dimensionTooBigTest(){
		
		int[][] matrix;
		matrix = parse(pathToTestInstances + "zDimensionTooBig.sop");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void dimensionTooSmallTest(){
		
		int[][] matrix;
		matrix = parse(pathToTestInstances + "zDimensionTooSmall.sop");
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyTest() {

		int[][] matrix;
		matrix = parse(pathToTestInstances + "zEmpty.sop");
	}

	@Test(expected = IllegalArgumentException.class)
	public void noMatrixTest() {

		int[][] matrix;
		matrix = parse(pathToTestInstances + "zNoMatrix.sop");
	}

	@Test
	public void noPathTest() {

		int[][] matrix;
		matrix = parse(pathToTestInstances + "zNoPath.sop");
		assertNull(StartGenAlg.runAlg(matrix));
	}

	@Test
	public void nullTest() {
		int[][] matrix;
		matrix = parse(pathToTestInstances + "zNull.sop");
		assertEquals(0, calculate(matrix, StartGenAlg.runAlg(matrix)));
	}
	
	@Test
	public void oneNodeExlStartStopTest() {
		int[][] matrix;
		matrix = parse(pathToTestInstances + "zOneNodeExlStartStop.sop");
		assertEquals(3, calculate(matrix, StartGenAlg.runAlg(matrix)));
	}
	
	@Test
	public void oneNodeInclStartStopTest() {
		int[][] matrix;
		matrix = parse(pathToTestInstances + "zOneNodeInclStartStop.sop");
		assertNull(StartGenAlg.runAlg(matrix));
	}

	@Test
	public void onlyOnePathTest() {
		int[][] matrix;
		matrix = parse(pathToTestInstances + "zOnlyOnePath.sop");
		assertEquals(6, calculate(matrix, StartGenAlg.runAlg(matrix)));
	}

	@Test
	public void onlyStartStopTest() {
		int[][] matrix;
		matrix = parse(pathToTestInstances + "zOnlyStartStop.sop");
		assertEquals(5, calculate(matrix, StartGenAlg.runAlg(matrix)));
	}

	@Test
	public void startStopDistancesTest() {
		int[][] matrix;
		matrix = parse(pathToTestInstances + "zStartStopDistances.sop");
		assertEquals(3, calculate(matrix, StartGenAlg.runAlg(matrix)));
	}

	@Test
	public void sopInstancesTest() {
		int[][] matrix;

		matrix = parse(pathToTestInstances + "esc07.sop");
		assertNotNull(StartGenAlg.runAlg(matrix));

		matrix = parse(pathToTestInstances + "br17.10.sop");
		assertNotNull(StartGenAlg.runAlg(matrix));
	}

	// Calls the Parser that the parser that the main-class uses.
	private int[][] parse(String file) {
		int[][] matrix;
		try {
			matrix = parser.parse(file);
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
