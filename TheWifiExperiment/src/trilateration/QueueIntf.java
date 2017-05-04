package trilateration;

public interface QueueIntf {
	int size();
	boolean isEmpty();
	void enqueue(int e); 
	int dequeue(); 
	int first();
}
