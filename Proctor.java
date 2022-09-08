import java.util.Vector;

public class Proctor extends Employee implements Runnable {
	private String name;
	private int age;
	private int numOfStudents;
	private Queue<Student> proctorQueue;
	private Queue<Test> firstTeachingAssistantQueue;
	private Queue<Test> secondTeachingAssistantQueue;

	// Counter fields
	private static int numOfChecks = 0;
	private static int numOfProctors = 0;
	private static int numOfWorkingProctors = 0;

	public Proctor(String name, int age, int numOfStudents, Queue<Student> proctorQueue,
			Queue<Test> firstTeachingAssistantQueue, Queue<Test> secondTeachingAssistantQueue) {
		this.name = name;
		this.age = age;
		this.numOfStudents = numOfStudents;
		this.proctorQueue = proctorQueue;
		this.firstTeachingAssistantQueue = firstTeachingAssistantQueue;
		this.secondTeachingAssistantQueue = secondTeachingAssistantQueue;
		numOfChecks = 0; 
		numOfWorkingProctors = 0; 
		numOfProctors++;
	}

	public void run() {
		while (!checkedAllStudents()) { // As long as proctors didn't check all students
			if (enoughWorkingProctors()) {
				return;
			}
			numOfWorkingProctors++;
			numOfChecks++;
			Student s = proctorQueue.extract();
			receiveTest(s);
			numOfWorkingProctors--;
		}
	}

	private boolean checkedAllStudents() {  // Checks if proctors checked all students
		return numOfChecks == numOfStudents;
	}

	private boolean enoughWorkingProctors() { // Checks if there are enough proctors who are working in order to finish all the checks
		return numOfWorkingProctors == numOfStudents - numOfChecks;
	}

	private void receiveTest(Student s) { // Proctor recieves a test
		simulateProcess();
		proctorCheck(s);
		placeInNextQueue(s.getStudentTest());
	}

	protected void simulateProcess() {  // Simulates proctor process
		try {
			Thread.sleep((long) (Math.random() * 3 * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void proctorCheck(Student s) { // Proctor checks a test
		updateTestRoom(s);
		updateTestStatus(s.getStudentTest());
	}

	private void updateTestRoom(Student s) { 
		s.getStudentTest().setRoom(s.getRoom());
	}

	protected void updateTestStatus(Test t) {
		t.setStatus("Proctor_Checked");
	}

	protected void placeInNextQueue(Test t) { // Places a test on shorter TA queue
		if (secondTeachingAssistantQueue.queue.size() < firstTeachingAssistantQueue.queue.size())
			try {
				secondTeachingAssistantQueue.insert(t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		else {
			if (Math.random() <= 0.5)
				try {
					firstTeachingAssistantQueue.insert(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			else {
				try {
					secondTeachingAssistantQueue.insert(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

} // Proctor
