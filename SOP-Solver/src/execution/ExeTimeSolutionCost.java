package execution;

import java.util.List;

/**
 * Class that offers instances to save a specific execution time for a specific
 * solution.
 * 
 * @author Daniel Lucas
 *
 */
public class ExeTimeSolutionCost {
	private long timeForExecution;
	private List<Integer> solution;
	private int cost;

	/**
	 * Default Constructor.
	 */
	public ExeTimeSolutionCost() {
	}

	/**
	 * Private constructor.
	 */
	@SuppressWarnings("unused")
	private ExeTimeSolutionCost(long timeForExecution, List<Integer> solution, int cost) {

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

	/**
	 * Gets the cost for the solution in this object.
	 * 
	 * @return Cost of the tour saved in solution.
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Sets cost for this solution in this specified object.
	 * 
	 * @param cost
	 *            The cost of the solution.
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
}
