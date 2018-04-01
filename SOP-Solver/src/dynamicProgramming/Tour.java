package dynamicProgramming;

import java.util.LinkedList;

public class Tour {

	// A Tour has 3 attributes: 
	private int costs; // the costs of a tour 
	private double binVal; // the representing value for the set of vertices in the tour
	private LinkedList<Integer> tour; // a list of vertices representing the tour

	// constructor:
	
	public Tour() {
		this.costs = 0;
		this.binVal = 0;
		this.tour = new LinkedList<Integer>();
	}

	public Tour(int costs, double binVal, int i) {
		this.costs = costs;
		this.binVal = binVal;
		LinkedList<Integer> l = new LinkedList<Integer>();
		l.add(i);
		this.tour = l;
	}
	
	public Tour(int costs, double binVal, LinkedList<Integer> l) {
		this.costs = costs;
		this.binVal = binVal;
		LinkedList<Integer> l2 = new LinkedList<Integer>();
		l2.addAll(l);
		this.tour = l2;
	}

	/**
	 * Method add a vertex to a tour of an Object of Tour.
	 * @param vertex The added vertex.
	 */
	public void add(int vertex) {
		this.tour.add(vertex);
	}
	/**
	 * Method to check if a tour of an Object of Tour contains a vertex.
	 * @param vertex The vertex which could be in the tour.
	 * @return True if a vertex is already part of a tour, false if not.
	 */
	public boolean isIn(int vertex) {
		if (this.tour.contains(vertex))
			return true;
		else
			return false;
	}
	//get methods
		
	public LinkedList<Integer> getTour(){
		return this.tour;
	}
	public int getCosts() {

		return this.costs;
	}
	public double getBinVal() {

		return this.binVal;
	}
	
	// set methods
	
	public void setCosts(int i) {
		this.costs = i;
	}

	public void setBinVal(long i) {
		this.binVal = i;
	}

	public void setTour(LinkedList<Integer> t) {
		this.tour = t;
		
	}

}
