package replicatedworkers;

/**
 * Work pool interface for replicated workers
 * @author prupakheti
 *
 * @param <WorkType>
 */
public interface WorkPool<WorkType extends Object> {	
	/**
	 * 
	 * @param me The thread id of the worker thread or can be any id 
	 * @return The next available work in the pool
	 * @throws ReplicatedWorkersException
	 */
	WorkType getWork(int me) throws ReplicatedWorkersException;
	
	/**
	 * 
	 * @param me The thread id of the worker thread or can be any id 
	 * @param item The work to be processed later by a worker
	 * @throws ReplicatedWorkersException
	 */
	void putWork(int me, WorkType item) throws ReplicatedWorkersException;	
}
