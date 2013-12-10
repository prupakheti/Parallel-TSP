package paralleltsp;

import java.util.LinkedList;
import java.util.List;
import graph.DirectedEdge;
import graph.DirectedWeightedGraph;

/**
 * This class encapsulate all the necessary informations required for TSP tour
 * @author prupakheti
 *
 */
public class TSPTour {
	/*
	 * this is an array of tour done so far. if tour[i] is 0 or negative than it indicates that
	 * city i has not been visited. if tour[i] is a positive value say x, that it indicates i has been 
	 * visited and next city visited after i is x. each partial tour we try to make cyclic so that 
	 * the next city after the last visited city is 1 ( i.e. the begnning )
	 */
	private byte []tour;
	/*
	 * the city that was last visited
	 */
	private byte lastvisited;
	
	/*
	 * the directed weighted graph where we are doing TSP tour
	 */
	private DirectedWeightedGraph G;
	/*
	 * the total number of visited city so far
	 */
	private byte totalvisited;
	
	/*
	 * the cost of the tour so far
	 */
	private int cost;
	
	/*
	 * a flag to indicate if this is a complete
	 */
	private boolean complete;
	

	/**
	 * 
	 * @param G Directed Weighted Graph where we are doing the TSP tour
	 */
	public TSPTour(DirectedWeightedGraph G){
		this.G = G;
		int V = G.V();
		tour = new byte[V+2];
		totalvisited = 1;
		lastvisited = 1;
		tour[1] = 1;
		cost = 0;
		complete = false;
	}
	
	/**
	 * 
	 * @param t A tour which we are cloning
	 */
	public TSPTour(TSPTour t){
		tour = new byte[t.tour.length];
		G = t.G;
		for( int i =0; i < tour.length; ++i){
			tour[i] = t.tour[i];
		}
		totalvisited = t.totalvisited;
		lastvisited = t.lastvisited;	
		cost = t.cost;
		complete = t.complete;
	}
	
	/**
	 * 
	 * @param city the next city which we are visiting
	 */
	private void visit(int city){
		city = city + 1;
		tour[lastvisited] = (byte)city;
		tour[city] = 1;
		int wt = G.getCost(lastvisited - 1, city -1 );
		cost = cost + wt;
		lastvisited = (byte)city;
		totalvisited++;		
	}
	
	/**
	 * 
	 * @return The cost of tour 
	 */
	public int getCost(){
		return cost;
	}
	
	/**
	 * 
	 * @return Returns the heuristic ( optimistic cost ) that does not over estimate the 
	 * actual cost
	 */
	public int getOptimisticCost(){
		if(complete) return cost;
		else{
			return cost + ( G.V() - totalvisited + 1) * G.getMinWt();
		}
	}
	
	/**
	 * 
	 * @param city The city that we are interested if it is visited or not
	 * @return returns true if city is visited, false otherwise
	 */
	private boolean isVisited(int city){
		city = city + 1;
		return tour[city]!=0;
	}
	/**
	 * 
	 * @return Returns true if the tour is complete, false otherwise
	 */
	public boolean isComplete(){
		return complete;
	}
	
	/**
	 * 
	 * @return A list of tours that can be spawned from the current tour
	 * @throws Exception
	 */
	public List<TSPTour> nextTours() throws Exception{
		if(complete) return null;
		List<TSPTour> tours = new LinkedList<TSPTour>();
		if(totalvisited == G.V()){
			if(!complete){
				int wt = G.getCost(lastvisited - 1, 0);
				if(wt > 0){
					TSPTour t = new TSPTour(this);
					t.cost += wt;
					t.complete = true;					
					tours.add(t);					
				}
				else{
					return null;
				}				
			}
		}
		else{
			for(DirectedEdge edge : G.getAdj(lastvisited - 1)){
				int other = edge.other(lastvisited - 1) ;
				int wt = edge.getWeight();
				if(!isVisited(other) && wt > 0 ){
					TSPTour t = new TSPTour(this);
					t.visit(other);
					tours.add(t);
				}
			}
		}		
		return tours;
	}
	
	@Override
	public String toString(){
		String str = "Tour : ";
		int curr = 1;
		do{
			str += (curr - 1) + " ,";
			curr = tour[curr];
		}while(curr!=1);
		str+=(curr - 1);
		return str + "(  Cost is : "+ cost +" ) ";
		
	}
	
	
}
