import java.util.Vector;

public class Lecturer extends Employee implements Runnable {

	private String name;
	private Vector<Long> bestStudentsID = new Vector<Long>();
	private Vector<Test> tests = new Vector<Test>();
	private Queue<Test> lecturerTestsQueue;
	private Queue<Test> exerciseCheckerQueue;
	private Vector<Student> students;
	private Vector<Payable> payableStuffMembers; 											
	private Data data; 
	private int numOfChecks = 0;

	public Lecturer(String name, Queue<Test> lecturerTestsQueue, Queue<Test> exerciseCheckerQueue,
			Vector<Student> students, Vector<Payable> payableStuffMembers, Data data) { 
		this.name = name;
		this.lecturerTestsQueue = lecturerTestsQueue;
		this.exerciseCheckerQueue = exerciseCheckerQueue;
		this.students = students;
		this.payableStuffMembers = payableStuffMembers;
		this.data = data;
	}

	public void run() {  // Runs lecturer actions
		while (data.scanEnded == false) {  // As long as the test is running
			while (!checkedAllStudents()) {
				Test t = this.lecturerTestsQueue.extract();
				giveFactor(t);
				placeInNextQueue(t);
			}
		}
		data.endTest();
		try {
			notifyStuffMembers();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		printTestDetails();
	}

	private boolean checkedAllStudents() {  // Checks if lecturer checked all students
		return students.size() == numOfChecks;
	}

	private void giveFactor(Test t) {  // Gives a factor to the test
		simulateProcess();
		updateGradeWithFactor(t);
		addToTestsVector(t);
		updateTestStatus(t);
		addToBestStudents(t);
	}

	protected void simulateProcess() {  // Simulates the check 
		try {
			Thread.sleep((long) 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void updateGradeWithFactor(Test t) { // Updates the grades with the factor
		if ((t.getGradeWithoutFactor() >= 50) && (t.getGradeWithoutFactor() <= 55)) { // 50-55 -> 56
			t.setGradeWithFactor(56);
			return;
		}
		if (t.getGradeWithoutFactor() > 56) {
			if (t.getGradeWithoutFactor() >= 95) { // 95+ -> 100
				t.setGradeWithFactor(100);
			}

			else {
				t.setGradeWithFactor(t.getGradeWithoutFactor() + 5); // <56 -> Grade with factor + 5
			}
		} else {
			t.setGradeWithFactor(t.getGradeWithoutFactor()); // else -> Grade with factor = grade without factor
		}
	}

	private void addToTestsVector(Test t) {  // Adds each test to tests vector
		tests.add(t);
	}

	protected void updateTestStatus(Test t) {  // Updates status
		t.setStatus("Checked_By_Lecturer");
		numOfChecks++;
	}

	private void addToBestStudents(Test t) { // Adds a student to best students vector
		if (t.getGradeWithFactor() > 95)
			this.bestStudentsID.add(t.getID());
	}

	protected void placeInNextQueue(Test t) {  // Inserts a test to EC queue
		try {
			this.exerciseCheckerQueue.insert(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void printTestDetails() {  // Prints test results
		System.out.println("Test is over! Grades are published, and here are the results:");
		System.out.println("Number of students who did the test: " + students.size());
		System.out.println("Average of tests without factor: " + withoutFactorAverage());
		System.out.println("Average of tests with factor: " + withFactorAverage());
		System.out.println("Best students ID's are: ");
		printBestStudentsID();
		System.out.println("Total salary of teaching assistants and the exercise checker is: " + calculateTotalSalary());
	}

	private double withoutFactorAverage() {  // Calculates average without factor
		double counter = 0;
		for (int i = 0; i < tests.size(); i++) {
			counter = counter + tests.elementAt(i).getGradeWithoutFactor();
		}
		return counter / tests.size();
	}

	private double withFactorAverage() {  // Calculates average with factor
		double counter = 0;
		for (int i = 0; i < tests.size(); i++) {
			counter = counter + tests.elementAt(i).getGradeWithFactor();
		}
		return counter / tests.size();
	}

	private void printBestStudentsID() {  // Prints best students
		for (int i = 0; i < bestStudentsID.size(); i++) {
			System.out.print(bestStudentsID.elementAt(i) + ", ");
			System.out.println();
		}
	}

	private double calculateTotalSalary() {  // Calculates the total salary of TA's and EC
		double counter = 0;
		for (int i = 0; i < payableStuffMembers.size(); i++) {
			counter = counter + payableStuffMembers.elementAt(i).getSalary();
		}
		return counter;
	}

	private void notifyStuffMembers() throws InterruptedException { // Sends a message to stuff members that should stop, by inserting a "null" test to their queue
		Test t = null;
		this.data.firstTeachingAssistantQueue.insert(t);
		this.data.secondTeachingAssistantQueue.insert(t);
		this.data.exerciseCheckerQueue.insert(t);
		this.data.IEMSecretaryQueue.insert(t); // For both secretaries
		this.data.IEMSecretaryQueue.insert(t);
	}
	
} // Lecturer
