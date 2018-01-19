package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import convertSOPFileToArray.parser;
import execution.Exe;
import genetic.StartGenAlg;

public class GenAlgTest {
	
	private String privatePath = "C:\\Users\\thore_000\\Uni\\5. Semester\\SOP-Projekt\\";
	
	private String projectPath = "SOP-Solver\\SOP-Instanzen\\";
	
	@Test
	public void noPathTest(){
		String path = privatePath + projectPath;
		int[][] matrix;
		matrix = parse(path + "zNoPath.sop");
		assertNull(StartGenAlg.runAlg(matrix));
	}
	
	@Test
	public void nullTest(){
		String path = privatePath + projectPath;
		int[][] matrix;
		matrix = parse(path + "zNull.sop");
		assertEquals(0, calculate(matrix, StartGenAlg.runAlg(matrix)));
	}
	
	@Test
	public void oneNodeTest(){
		String path = privatePath + projectPath;
		int[][] matrix;
		matrix = parse(path + "zOneNode.sop");
		assertEquals(3, calculate(matrix, StartGenAlg.runAlg(matrix)));
	}
	
	@Test
	public void onlyOnePathTest(){
		String path = privatePath + projectPath;
		int[][] matrix;
		matrix = parse(path + "zOnlyOnePath.sop");
		assertEquals(6, calculate(matrix, StartGenAlg.runAlg(matrix)));
	}
	
	@Test
	public void onlyStartStopTest(){
		String path = privatePath + projectPath;
		int[][] matrix;
		matrix = parse(path + "zOnlyStartStop.sop");
		assertEquals(0, calculate(matrix, StartGenAlg.runAlg(matrix)));
	}
	
	@Test
	public void startStopDistancesTest(){
		String path = privatePath + projectPath;
		int[][] matrix;
		matrix = parse(path + "zStartStopDistances.sop");
		assertEquals(3, calculate(matrix, StartGenAlg.runAlg(matrix)));
	}
	
	@Test
	public void sopInstancesTest(){
		String path = privatePath + projectPath;
		int[][] matrix;
		
		matrix = parse(path + "esc07.sop");
		assertNotNull(StartGenAlg.runAlg(matrix));
		
		matrix = parse(path + "br17.10.sop");
		assertNotNull(StartGenAlg.runAlg(matrix));
	}
	
	//Calls the Parser that the parser that the main-class uses.
	private int[][] parse(String file){
		int[][] matrix;
		try {
			matrix = parser.parse(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file);
			return null;
		}
		return matrix;
	}
	
	//Calls the calculateCost method from the main-class.
	private int calculate(int[][] matrix, List<Integer> solutionPath){
		if(solutionPath != null){
			return Exe.calculateCost(matrix, solutionPath);
		}
		return -1;
	}

}
