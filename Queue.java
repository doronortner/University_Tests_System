import java.util.Vector;

public class Queue<T> {
	public Vector<T> queue;

	public Queue() {
		this.queue = new Vector<T>();
	}

	public synchronized void insert(T item) throws InterruptedException {  // Inserts an element to queue
		this.queue.add(item);
		this.notifyAll();
	}

	public synchronized T extract() {   // Extracts an element from queue
		while (this.queue.isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		T t = queue.elementAt(0);
		this.queue.remove(t);
		return t;
	}
	
} // Queue
