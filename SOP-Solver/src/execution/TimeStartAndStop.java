package execution;

/**
 * Provides two methods. One for Starting the time and one for returning the
 * executed time according to the starting time.
 * 
 * @author Daniel Lucas
 *
 */
public class TimeStartAndStop {
	/**
	 * Stops the time and returns the execution time according to the startTime.
	 * 
	 * @param startTime
	 *            Start point of measuring time.
	 * @return The time from start to stop.
	 */
	public static long stopTime(long startTime) {
		long stopTime = System.currentTimeMillis();
		return stopTime - startTime;
	}

	/**
	 * Registers the start time and returns it.
	 * 
	 * @return The start time.
	 */
	public static long startTime() {
		long start = System.currentTimeMillis();
		return start;
	}
}
