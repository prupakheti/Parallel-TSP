package replicatedworkers;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * A Centralized work pool implementation. This implementaion of work pool has a single pool of 
 * blocking queue that all the worker thread share.
 * @author prupakheti
 *
 * @param <WorkType>
 */
public class CentralizedWorkPool<WorkType extends Object> implements WorkPool<WorkType>{
	/**
	 * A pool of work queue 
	 */
	private BlockingQueue<WorkType> pool;
	
	/**
	 * Total number of workers for this work pool
	 */
	private int numworkers;
	
	/**
	 * A lock for mutual exclusion of count update
	 */
	private Lock lock;
	
	/**
	 * A negative count indicated how many workers are waiting for a new work in work pool.
	 * A positive count indicates the total number of work available in the pool. When count 
	 * negative magnitude equals to the total number of worker, that is a clear indication of 
	 * termination
	 */
	int count;
	
	/**
	 * A dummy work that is added in the pool when actual work are over. Worker when reads this dummy
	 * work, get the information that no more work are present in the work pool and should
	 * terminate
	 */
	WorkType dummy;

	public CentralizedWorkPool(int n, WorkType dummy) throws ReplicatedWorkersException{
		if(dummy == null ) throw new ReplicatedWorkersException(" dummy can not be null ");
		this.dummy = dummy;
		this.numworkers = n;
		count = 0;
		this.lock = new ReentrantLock();
		this.pool = new LinkedBlockingQueue<WorkType>();
	}
	
	public CentralizedWorkPool(int n, WorkType dummy, Comparator<WorkType> c) throws ReplicatedWorkersException{
		if(dummy == null ) throw new ReplicatedWorkersException(" dummy can not be null ");
		this.dummy = dummy;
		this.numworkers = n;
		count = 0;
		this.lock = new ReentrantLock();
		this.pool = new PriorityBlockingQueue<WorkType>(10000, c);
	}
	
	
	@Override
	public WorkType getWork(int me) throws ReplicatedWorkersException {
		int workcount;
		
		// Mutual exclusion for updating count
		lock.lock();
			workcount = count - 1;
			count = workcount;
		lock.unlock();
		
		if( workcount == - numworkers ){
			for( int i = 0; i < numworkers; ++i){
				try{
					pool.put(dummy);					
				}
				catch(Exception e){
					throw new ReplicatedWorkersException(e.toString());
				}
			}
			
		}		
		WorkType t = null;
		try{
			
			// taking the next avaliable work from the work pool
			t = pool.take();		
		}
		catch(InterruptedException e){
			System.out.println(e.toString());
		}
		return t;
	}

	@Override
	public void putWork(int me, WorkType item) throws ReplicatedWorkersException{	
		
		// Mutual exclusion for updating count
		lock.lock();
			count++;
		lock.unlock();
		try{
			// putting work in the work pool queue
			pool.put(item);			
		}
		catch(Exception e){
			throw new ReplicatedWorkersException(e.toString());
		}
	
	}
}
