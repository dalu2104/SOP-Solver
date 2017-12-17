package simulatedAnnealing;

import java.util.List;

/**
 * Class that offers instances to save a specific execution time for a specific
 * solution.
 * 
 * @author Daniel Lucas
 *
 */
public class ExecutionTimeAndSolution {
	private long timeForExecution;
	private List<Integer> solution;

	/**
	 * Default Constructor.
	 */
	public ExecutionTimeAndSolution() {
	}

	/**
	 * Private constructor.
	 */
	@SuppressWarnings("unused")
	private ExecutionTimeAndSolution(long timeForExecution, List<Integer> solution) {

	}

	////////////////////////// GET & SET

	/**
	 * Gets the time it took for this solution to be determined.
	 * 
	 * @return Execution time in ms.
	 */
	public long getTimeForExecution() {
		return timeForExecution;
	}

	/**
	 * Sets the time it took for this solution to be determined.
	 * 
	 * @param timeForExecution
	 *            Execution time in ms.
	 */
	public void setTimeForExecution(long timeForExecution) {
		this.timeForExecution = timeForExecution;
	}

	/**
	 * Gets the solution.
	 * 
	 * @return The solution as a list of Integers.
	 */
	public List<Integer> getSolution() {
		return solution;
	}

	/**
	 * Sets the solution.
	 * 
	 * @param solution
	 *            The solution as a list of integers.
	 */
	public void setSolution(List<Integer> solution) {
		this.solution = solution;
	}
}
