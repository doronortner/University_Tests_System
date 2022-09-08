import java.util.HashMap;
import java.util.Vector;

public class ExerciseChecker extends Employee implements Runnable, Payable {
	private String name;
	private double salary;
	private final int salaryPerSec = 1;
	private HashMap<Long, Vector<Integer>> assignmentsGradeMap;
	private Queue<Test> exerciseCheckerQueue;
	private Queue<Test> IEMSecretaryQueue;
	private Vector<Payable> payableStuffMembers;
	private Data data;

	public ExerciseChecker(String name, HashMap<Long, Vector<Integer>> assignmentsGradeMap,
			Queue<Test> exerciseCheckerQueue, Queue<Test> IEMSecretaryQueue, Vector<Payable> payableStuffMembers,
			Data data) {
		this.name = name;
		this.salary = 0;
		this.assignmentsGradeMap = assignmentsGradeMap;
		this.exerciseCheckerQueue = exerciseCheckerQueue;
		this.IEMSecretaryQueue = IEMSecretaryQueue;
		payableStuffMembers.addElement(this);
		this.data = data;
	}

	public void run() {
		while (data.testEnded == false) { // As long as the test is running
			Test t = exerciseCheckerQueue.extract();
			if (t == null)
				continue;
			calculateFinalGrade(t);
		}
	}

	private void calculateFinalGrade(Test t) { // Calculates the final grade including HW grades
		simulateProcess();
		calculateAssignmentsGrade(t);
		updateTestStatus(t);
		placeInNextQueue(t);
	}

	protected void simulateProcess() { // Simulates EC process
		double checkTime = Math.random() + 2;
		try {
			Thread.sleep((long) checkTime * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.setSalary(checkTime);
	}

	private void calculateAssignmentsGrade(Test t) { // Calculates student's assignments grade
		Vector<Integer> studentHWGrades = getHWGradesById(t.getID());
		int assignmentGrade = (int) (0.02 * studentHWGrades.get(0) + 0.04 * studentHWGrades.get(1)
				+ 0.06 * studentHWGrades.get(2) + 0.08 * studentHWGrades.get(3));
		t.setGradeWithFactor((int) (t.getGradeWithFactor() * 0.8 + assignmentGrade));
	}

	private Vector<Integer> getHWGradesById(long id) { // Returns student's HW grades by his ID
		return this.assignmentsGradeMap.get(id);
	}

	protected void updateTestStatus(Test t) { // Updates status
		t.setStatus("Calculated_With_HW");
	}

	protected void placeInNextQueue(Test t) { // Places a test on IEM queue
		try {
			this.IEMSecretaryQueue.insert(t);
		} catch (InterruptedException e) {

		}
	}

	public double getSalary() {
		return this.salary;
	}

	private void setSalary(double workTimeInSeconds) {
		this.salary = this.salary + (double) (salaryPerSec * workTimeInSeconds);
	}

} // EC
