
public class IEMSecretary extends Employee implements Runnable {
	private String name;
	private boolean properAcademicStateSecretary;
	private Queue<Test> IEMSecretaryQueue;
	private BoundedQueue<Test> EDWQueue;
	private Data data;

	public IEMSecretary(String name, boolean whichState, Queue<Test> IEMSecretaryQueue, BoundedQueue<Test> EDWQueue,
			Data data) {
		this.name = name;
		this.properAcademicStateSecretary = whichState; // T - proper, F - not proper
		this.IEMSecretaryQueue = IEMSecretaryQueue;
		this.EDWQueue = EDWQueue;
		this.data = data;
	}

	public void run() {
		while (data.testEnded == false) { // As long as the test is running
			Test t = this.IEMSecretaryQueue.extract();
			if (t == null)
				continue;
			if (isSuitableSecretary(t) == true) { // If it's the suitable secretary for the test
				secretaryProcess(t);
			} else {
				try {
					this.IEMSecretaryQueue.insert(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean isSuitableSecretary(Test t) { // Checks if it's the suitable secretary for the test
		if (this.properAcademicStateSecretary == true && t.getGradeWithFactor() >= 70)
			return true;
		if (this.properAcademicStateSecretary == false && t.getGradeWithFactor() < 70)
			return true;
		return false;
	}

	private void secretaryProcess(Test t) { // Secretary process
		simulateProcess();
		updateTestStatus(t);
		placeInNextQueue(t);
		printTestDetails(t);
	}

	protected void updateTestStatus(Test t) { // Updates status
		t.setStatus("Entered_To_DB");
	}

	protected void placeInNextQueue(Test t) { // Places test on EDW queue
		this.EDWQueue.insert(t);
	}

	private void printTestDetails(Test t) { // Prints student's details
		System.out.println("Student's ID is: " + t.getID() + " , his final grade is: " + t.getGradeWithFactor());
	}

	protected void simulateProcess() { // Simulates secretary process by it's type
		int workTime;
		if (this.properAcademicStateSecretary == true)
			workTime = 2;
		else
			workTime = 3;
		try {
			Thread.sleep((long) 1000 * workTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

} // IEMSecretary
