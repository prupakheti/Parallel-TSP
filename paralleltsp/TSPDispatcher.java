package paralleltsp;

import graph.DirectedWeightedGraph;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import replicatedworkers.*;

public class TSPDispatcher {
	/**
	 * A work pool of partial tours that can take new partial tours and give existing
	 * partial tours for workers
	 */		
	private WorkPool<TSPTour> workPool;
	
	/**
	 * A lock for mutual exclusion that workers use to update global optimal tour
	 */
	private Lock lock;
	
	/**
	 * A delineater that indicated there are no more work in the work pool
	 */
	private TSPTour dummy;
	
	/**
	 * the global optimal tour found so far
	 */
	private TSPTour optimalTour;
	
	/**
	 * the total number of worker thread
	 */
	private int totalWorkers;
	
	/**
	 * the directed weighted graph where we are doing the TSP tour
	 */
	private DirectedWeightedGraph G;
	
	/**
	 * A barrier that blocks all the worker so that they exit simultaneously agreeing upon 
	 * the global optimal tour they computed.
	 */
	private CyclicBarrier barrier;
	
	/**
	 * 
	 * @param G The directed weighted graph where we are doing the TSP tour	
	 * @param n the total number of worker threads we want to spawn 
	 * @throws Exception
	 */
	public TSPDispatcher(DirectedWeightedGraph G, int n) throws Exception{
		this.G = G;
		this.lock = new ReentrantLock();
		this.dummy = new TSPTour(G);
		this.totalWorkers = n;
		this.barrier = new CyclicBarrier(n + 1);		
		this.workPool = new CentralizedWorkPool<TSPTour>(totalWorkers, dummy, new Comparator<TSPTour>() {

			@Override
			public int compare(TSPTour o1, TSPTour o2) {
				if(o1.getCost() < o2.getCost() ) return -1;
				else if( o1.getCost() > o2.getCost() ) return 1;
				return 0;
			}
		});
		TSPTour t = new TSPTour(G);	
		this.workPool.putWork(0, t);
		
	}
	
	/**
	 * A worker class that loops to process a tour present in work pool and and adds new tour
	 * in the work pool after processing.
	 * @author prupakheti
	 *
	 */
	private class Worker implements Runnable{
		int id;
		public Worker(int id){
			this.id = id;
		}
		
		@Override
		public void run() {
			try{
				TSPTour curr = workPool.getWork(id);					
				while( curr != dummy ){
					if (curr.isComplete()){								
						if( optimalTour == null ){
							optimalTour = curr;							
						}
						else if(  optimalTour.getCost() > curr.getCost()  ){
							// Mutual exclusion
							lock.lock();
								optimalTour = curr;								
							lock.unlock();
						}
					}
					else{
						List<TSPTour> tourList = curr.nextTours();								
						for( TSPTour t : tourList ){
							// putting work in the work pool based on heuristic
							if( optimalTour == null || (t.getOptimisticCost() < optimalTour.getCost())){
								workPool.putWork(id, t);
							}
						}						
					}
					curr = workPool.getWork(id);
				}
				// waiting for all thread to exit together
				barrier.await();				
			}
			catch (Exception e) {
				System.out.println(e.toString());
			}
			
		}
		
	}
	
	/**
	 * Dispatches all the worker
	 */
	private void dispatch(){
		Thread []threads = new Thread[totalWorkers];
		int i = 0;
		for(Thread t : threads){
			t = new Thread(new Worker(i++));
			t.start();
		}
		
	}
	
	public TSPTour getOptTour() throws Exception{
		dispatch();
		// waiting for all thread to exit
		barrier.await();
		
		return optimalTour;
	}
	
}
