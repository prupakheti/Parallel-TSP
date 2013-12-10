package graph;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A Directed weighted graph class.
 * @author prupakheti
 *
 */
public class DirectedWeightedGraph {
	/**
	 * total number of vertices in the graph
	 */
	private int V;
	
	/**
	 * adjacency list. adj[i] is the list of directed edges that goes out from vertex i.
	 */
	List<DirectedEdge>[] adj;
	
	/**
	 * this is adjacency matrix. adjMat[i][j] represents the weight or the cost of direct path
	 * between vertex i and j. A zero or negative value indicates there is no edge between 
	 * vertex i and j.
	 */
	int [][] adjMat;
	
	/**
	 * the global minimum edge weight in the graph.
	 */
	int minWt;
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @param V Total number of vertices in the graph
	 */
	public DirectedWeightedGraph(int V){
		this.V = V;
		this.adj = new List[V];
		for( int i = 0; i < V; ++i ){
			this.adj[i] = new LinkedList<DirectedEdge>();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public DirectedWeightedGraph(int [][]adjMat, int V) {
		this.V = V;
		this.adj = new List[V];
		this.adjMat = adjMat;
		this.minWt = Integer.MAX_VALUE;
		for( int i = 0; i < V; ++i ){
			this.adj[i] = new LinkedList<DirectedEdge>();
		}
		for( int i = 0; i < V; ++i ){
			for( int j = 0; j < V; ++j){
				if( i != j && adjMat[i][j] > 0)	{
					if(adjMat[i][j] < minWt ) {
						minWt = adjMat[i][j];
					}
					this.adj[i].add(new DirectedEdge(i, j, adjMat[i][j]));
				}
			}
		}
				
		for( int i = 0 ; i < V ; ++i ){
			Collections.sort(adj[i]);
		}
	}
	
	public int V(){
		return V;
	}
	
	public List<DirectedEdge> getAdj( int i ) throws Exception{
		if( i < 0 || i >= V ) throw new Exception(" invalid vertex index ");
		return adj[i];
	}
	
	public void addAdj(int i , DirectedEdge edge ) throws Exception{
		if( i < 0 || i >= V ) throw new Exception(" invalid vertex index ");
		this.adj[i].add(edge);
	}
	
	public int getCost(int i, int j){
		return adjMat[i][j];
	}
	
	public int getMinWt(){
		return minWt;
	}
	
	@Override
	public String toString(){
		String str ="";
		int i =0;
		for(List<DirectedEdge> l : adj){
			str += i + " : ";
			for(DirectedEdge e : l){
				str += e.getWeight() + ",";
			}
			str+="\n";
			++i;
		}
		return str;
	}
	
}
