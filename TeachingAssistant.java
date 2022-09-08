import java.util.Vector;

public class TeachingAssistant extends Employee implements Runnable, Payable {
	private String name;
	private final double salaryPerSec = 3;
	private double salary = 0;
	private static boolean[] listOfTestAnswers = new boolean[20];
	private double pError; 
	private Queue<Test> teachingAssistantQueue; 
	private Queue<Test> otherTeachingAssistantQueue;
	private Queue<Test> lecturerTestsQueue;
	private Vector<Payable> payableStuffMembers;
	private Data data;

	public TeachingAssistant(String name, double pError, Queue<Test> teachingAssistantQueue,
			Queue<Test> otherTeachingAssistantQueue, Queue<Test> lecturerTestsQueue, boolean[] testAnswers,
			Vector<Payable> payableStuffMembers, Data data) {
		this.name = name;
		this.pError = pError;
		this.teachingAssistantQueue = teachingAssistantQueue;
		this.otherTeachingAssistantQueue = otherTeachingAssistantQueue;
		this.lecturerTestsQueue = lecturerTestsQueue;
		this.listOfTestAnswers = testAnswers;
		payableStuffMembers.addElement(this);
		this.data = data;
	}

	public void run() {  // Runs TA actions
		while (data.testEnded == false) {  // As long as the test is running
			Test t = this.teachingAssistantQueue.extract();
			if (t == null)
				continue;
			teachingAssistantCheck(t);
		}
	}

	private void teachingAssistantCheck(Test t) {  // Checking a test
		simulateProcess();
		gradeTest(t);
		updateTestStatus(t);
		placeInNextQueue(t);
	}

	protected void simulateProcess() {  // Simulates the duration of the check + raises the salary
		double checkTime = (Math.random() * 2) + 1.5;
		try {
			Thread.sleep((long) checkTime * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.setSalary(checkTime);
	}

	private void gradeTest(Test t) {  // Grades the test
		if (!isFirstTACheck(t)) { // Grades will be given only on the second check
			for (int i = 0; i < 20; i++) {
				boolean studentAnswer = checkAnswer(t, i);
				gradeCalculation(t, studentAnswer);
			}
		}
	}

	private boolean checkAnswer(Test t, int questionNumber) {  // Checks a question
		if (t.getStudentAnswers(questionNumber) == this.listOfTestAnswers[questionNumber])
			return true;
		else
			return false;
	}

	private void gradeCalculation(Test t, boolean studentAnswer) {  // Updates the test grade
		double probability = Math.random();
		double pErrorTemp = this.pError;
		if (!isFirstTACheck(t))
			pErrorTemp = pErrorTemp / 2; 
		if (((probability <= pErrorTemp) && (!studentAnswer))  // student & teachingAssistant was wrong
				|| ((probability > pErrorTemp) && (studentAnswer))) // student & teachingAssistant was right
			t.setGradeWithoutFactor(t.getGradeWithoutFactor() + 5);
	}

	protected void updateTestStatus(Test t) {  // Updates status
		if (isFirstTACheck(t)) {
			t.setStatus("TA_Checked_First_Time");
			return;
		} else {
			t.setStatus("TA_Checked_Second_Time");
		}
	}

	private boolean isFirstTACheck(Test t) {  // Checks if it's the test's first check
		if (t.getStatus().equals("Proctor_Checked"))
			return true;
		return false;
	}

	private boolean checkedOnce(Test t) {  // Checks test for the first time
		if (t.getStatus().equals("TA_Checked_First_Time"))
			return true;
		return false;
	}

	protected void placeInNextQueue(Test t) {  // Places test on other TA queue / Lecturer queue
		if (checkedOnce(t)) {
			try {
				this.otherTeachingAssistantQueue.insert(t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		} else
			try {
				this.lecturerTestsQueue.insert(t);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	public double getSalary() {
		return this.salary;
	}

	private void setSalary(double workTimeInSeconds) {
		this.salary = this.salary + (double) (salaryPerSec * workTimeInSeconds);
	}
	
} // TeachingAssistant
