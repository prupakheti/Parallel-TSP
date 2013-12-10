package driver;


import graph.DirectedWeightedGraph;
import graphreader.*;

import java.util.Scanner;

import paralleltsp.TSPDispatcher;
import paralleltsp.TSPTour;

public class TSPDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Scanner sc = new Scanner(System.in);
		GraphReader graphReader = new MatrixReader();
		int [][]adj = graphReader.createAdjMatrix(sc);
		DirectedWeightedGraph G = new DirectedWeightedGraph(adj, graphReader.getSize());			
		int cores = Runtime.getRuntime().availableProcessors();	
		TSPDispatcher tspDispatcher = new TSPDispatcher(G, cores);
		TSPTour t = tspDispatcher.getOptTour();
		System.out.println("Optimal : "+ t);
	}

}
