
public class BoundedQueue<T> extends Queue<T> {
	private int maxSize;

	public BoundedQueue() {
		super();
		this.maxSize = 10;
	}

	public synchronized void insert(T item) { // Inserts an element to the queue
		while (this.queue.size() >= this.maxSize) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.queue.add(item);
		notifyAll();
	}

	public synchronized T extract() { // Extracts an element from the queue
		while (this.queue.isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		T t = queue.elementAt(0);
		this.queue.remove(t);
		this.notifyAll();
		return t;
	}

} // BoundedQueue