import java.util.Vector;

public class EDW extends Employee implements Runnable {
	String name;
	private BoundedQueue<Test> EDWQueue;
	private InformationSystem<Test> IS;
	private Data data;
	private static int numOfScans = 0;
	private static int numOfActiveEDW = 0;
	private int numOfStudents;

	public EDW(String name, int numOfStudents, BoundedQueue<Test> EDWQueue, InformationSystem<Test> IS, Data data) {
		this.name = name;
		this.numOfStudents = numOfStudents;
		this.EDWQueue = EDWQueue;
		this.data = data;
		this.IS = IS;
		numOfScans = 0;
		numOfActiveEDW = 0;
	}

	public void run() {
		while (!allTestsScanned()) { // As long as they didn't scanned all tests
			if (enoughActiveEDW()) {
				return;
			}
			numOfActiveEDW++;
			Test t = this.EDWQueue.extract();
			EDWProcess(t);
			numOfActiveEDW--;
		}
	}

	private boolean allTestsScanned() { // Checks if EDW scanned all tests
		return numOfScans == numOfStudents;
	}

	private void EDWProcess(Test t) {
		simulateProcess();
		updateTestStatus(t);
		scanTest(t);
		if (allTestsScanned()) { // After the active EDW scanned the last test
			data.endScan(); // Notifies the lecturer that they finished scanning
		}

	}

	protected void simulateProcess() { // Simulates EDW process
		try {
			Thread.sleep((long) 4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void updateTestStatus(Test t) {
		t.setStatus("Scanned");
		numOfScans++;
	}

	private void scanTest(Test t) { // Scans a test to the IS + printing
		placeInNextQueue(t);
		System.out.println("Exam Scanned for " + t.getID());
	}

	protected void placeInNextQueue(Test t) { // Inserts a test to the IS
		try {
			this.IS.insert(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean enoughActiveEDW() { // Checks if there are enough active EDW to finish the rest of the scans
		return numOfActiveEDW == numOfStudents - numOfScans;
	}

} // EDW
