import java.util.*;

public class Student implements Runnable {
	private long id;
	private String name;
	private int testRoom;
	private double CorrectAnsProbability;
	private double questionAnswerTime;
	private Vector<Integer> HWGrades;
	private Test test;
	private static boolean[] listOfTestAnswers = new boolean[20];
	private Queue<Student> proctorQueue;
	private InformationSystem<Test> IS;

	public Student(long id, String name, int testRoom, double CorrectAnsProbability, double questionAnswerTime,
			Vector<Integer> HWGrades, Queue<Student> proctorQueue, boolean[] testAnswers, InformationSystem<Test> IS) {
		this.id = id;
		this.name = name;
		this.testRoom = testRoom;
		this.CorrectAnsProbability = CorrectAnsProbability;
		this.questionAnswerTime = questionAnswerTime;
		this.HWGrades = HWGrades;
		this.test = new Test();
		this.proctorQueue = proctorQueue;
		this.listOfTestAnswers = testAnswers;
		this.IS = IS;
	}

	public void run() { // Run
		solveTest();
		try {
			proctorQueue.insert(this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		checkScannedTest();
	}

	private void solveTest() { // Students solves test
		fillStudentInfo();
		fillAnswers();
	}

	private void fillStudentInfo() { // Fills info in test
		this.test.setID(this.id);
		this.test.setDate("1st Of January, 2022");
	}

	private void fillAnswers() { // Simulates answering a question
		for (int i = 0; i < 20; i++) {
			try {
				Thread.sleep((long) (this.questionAnswerTime * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			updateAnswer(i);
		}

	}

	private void updateAnswer(int questionNumber) { // updates student's answer
		double random = Math.random();
		boolean correctAnswer = listOfTestAnswers[questionNumber];
		if ((random <= this.CorrectAnsProbability && correctAnswer == true)
				|| (random > this.CorrectAnsProbability && correctAnswer == false))
			this.test.setStudentAnswers(questionNumber);
	}

	private void checkScannedTest() { // Checks if his test is in the IS
		Test t;
		do {
			simulateSearch();
			t = this.IS.extract(this.id);
		} while (t == null);
	}

	private void simulateSearch() { // Simulates searching test on IS
		try {
			Thread.sleep((long) 2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Getters

	public int getRoom() {
		return this.testRoom;
	}

	public Test getStudentTest() {
		return this.test;
	}

	public long getId() {
		return this.id;
	}

	public Vector<Integer> getHWGrades() {
		return this.HWGrades;
	}
}