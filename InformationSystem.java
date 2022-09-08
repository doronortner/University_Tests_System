
public class InformationSystem<T> extends Queue<T> {
	public InformationSystem() {
		super();
	}

	public synchronized T extract(Long studentID) { // Extracts a test from IS by student's ID if it was scanned
		while (this.queue.isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		T t = null;
		if (isTestScanned(studentID)) {
			t = queue.elementAt(findTest(studentID));
			this.queue.remove(t);
		}
		this.notifyAll(); // When a students extracts a test, it notifies EDW and students who can try to
							// enter the IS
		return t;
	}

	private boolean isTestScanned(Long studentID) { // Checks if the test is on the IS
		for (int i = 0; i < this.queue.size(); i++) {
			if (((Test) this.queue.elementAt(i)).getID() == studentID)
				return true;
		}
		return false;
	}

	private int findTest(Long studentID) { // Finds test index on the queue
		int index = 0;
		for (int i = 0; i < this.queue.size(); i++) {
			if (((Test) this.queue.elementAt(i)).getID() == studentID)
				index = i;
		}
		return index;
	}

}
