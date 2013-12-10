package graph;

/**
 * The is an edge class for a directed weighted graph. 
 * @author prupakheti
 *
 */
public class DirectedEdge implements Comparable<DirectedEdge>{
	public int from;
	public int to;
	public int weight;
	public DirectedEdge(int from, int to, int weight){
		this.from = from;
		this.to = to;
		this.weight = weight;
	}
	
	public int either(){
		return from;
	}
	
	public int other(int that){
		if( that == from ) return to;
		else if( that == to ) return from;
		else return -1;
	}
	
	public int getWeight(){
		return weight;
	}

	@Override
	public int compareTo(DirectedEdge that) {
		if( this.weight < that.weight )return -1;
		else if( this.weight > that.weight ) return 1;
		return 0;
	}
	@Override
	public String toString(){
		return "From :"+from +" To :"+to+" Wt :"+weight;
	}
	
	
	
}
