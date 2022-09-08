
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.io.*;

public class Data {
	// Collections ----------------------------------------
	public Vector<Student> students = new Vector<Student>();
	private Vector<Proctor> proctors;
	private Vector<TeachingAssistant> teachingAssistants;
	private Vector<Lecturer> lecturer;
	private Vector<ExerciseChecker> exerciseCheckers;
	private Vector<IEMSecretary> IEMSecretaries;
	private Vector<EDW> examDepartmentWorkers;
	private Vector<Thread> threads;
	private Vector<Payable> payableStuffMembers = new Vector<Payable>();
	private HashMap<Long, Vector<Integer>> assignmentsGradeMap = new HashMap();

	// Queues ----------------------------------------------
	public Queue<Student> proctorQueue;
	public Queue<Test> firstTeachingAssistantQueue;
	public Queue<Test> secondTeachingAssistantQueue;
	public Queue<Test> lecturerTestsQueue;
	public Queue<Test> exerciseCheckerQueue;
	public Queue<Test> IEMSecretaryQueue;
	public BoundedQueue<Test> EDWQueue;
	public InformationSystem<Test> informationSystem;

	// Number of workers ----------------------------------
	private int numOfProctors;
	private int numOfTeachingAssistant;
	private int numOfLecturers;
	private int numOfExerciseCheckers;
	private int numOfIEMSecretary;
	private int numOfEDW;

	// More fields ----------------------------------------
	public boolean[] listOfTestAnswers = new boolean[20];
	public boolean scanEnded = false;
	public boolean testEnded = false;

	public Data(String importStudentList, double firstPError, double secondPError, int numOfEDW) { // Builder
		initializeFields();
		initializeQueues();
		getDataOfStudent(importStudentList);
		initNumberOfPeople(numOfEDW);
		fillListOfTestAnswers();
		createThreads(firstPError, secondPError);
		startThreads();
	}

	private void getDataOfStudent(String importStudentList) { // Reads input file
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(importStudentList);
			br = new BufferedReader(fr);
			br.readLine();
			String line;
			while ((line = br.readLine()) != null) {
				fillStudentData(line);
			}
			initiateHashMap(this.students);
			br.close();
		}

		catch (FileNotFoundException exception) {
			System.out.println("The file " + importStudentList + " was not found.");
		} catch (IOException exception) {
			System.out.println(exception);
		}
	}

	private void fillStudentData(String line) { // Creates students by the input file data
		String[] studentData = line.split("\t", 10);
		long id = Long.parseLong(studentData[0]);
		String name = studentData[1];
		int testRoom = Integer.parseInt(studentData[2]);
		double questionAnswerTime = Double.parseDouble(studentData[3]);
		double CorrectAnsProbability = Double.parseDouble(studentData[4]);
		Vector<Integer> HWGrades = new Vector<Integer>();
		for (int i = 0; i < 4; i++)
			HWGrades.add(Integer.parseInt(studentData[5 + i]));

		Student s = new Student(id, name, testRoom, CorrectAnsProbability, questionAnswerTime, HWGrades, proctorQueue,
				listOfTestAnswers, informationSystem);
		students.addElement(s);
	}

	private void initiateHashMap(Vector<Student> students) { // Creates a HashMap for each student, of his ID + HW
																// grades
		for (int i = 0; i < this.students.size(); i++) {
			assignmentsGradeMap.put(students.get(i).getId(), students.get(i).getHWGrades());
		}
	}

	private void initNumberOfPeople(int EDW) {
		this.numOfProctors = 3;
		this.numOfTeachingAssistant = 2;
		this.numOfLecturers = 1;
		this.numOfExerciseCheckers = 1;
		this.numOfIEMSecretary = 2;
		this.numOfEDW = EDW;
	}

	private void fillListOfTestAnswers() { // Fills the correct answers array randomly
		for (int i = 0; i < 20; i++) {
			if (Math.random() <= 0.5)
				listOfTestAnswers[i] = false;
			else
				listOfTestAnswers[i] = true;
		}
	}

	private void initializeFields() {
		students = new Vector<Student>();
		proctors = new Vector<Proctor>();
		teachingAssistants = new Vector<TeachingAssistant>();
		lecturer = new Vector<Lecturer>();
		exerciseCheckers = new Vector<ExerciseChecker>();
		IEMSecretaries = new Vector<IEMSecretary>();
		examDepartmentWorkers = new Vector<EDW>();
		payableStuffMembers = new Vector<Payable>();
		threads = new Vector<Thread>();
		informationSystem = new InformationSystem<Test>();
		assignmentsGradeMap = new HashMap();
	}

	private void initializeQueues() {
		proctorQueue = new Queue<Student>();
		firstTeachingAssistantQueue = new Queue<Test>();
		secondTeachingAssistantQueue = new Queue<Test>();
		lecturerTestsQueue = new Queue<Test>();
		exerciseCheckerQueue = new Queue<Test>();
		IEMSecretaryQueue = new Queue<Test>();
		EDWQueue = new BoundedQueue<Test>();
	}

	private void createThreads(double firstTAp, double secondTAp) { // Creates each person, and inserts into a thread
		createStudents();
		createProctor();
		createTeachingAssistant(firstTAp, secondTAp);
		createLecturers();
		createExerciseCheckers();
		createIEMSecretaries();
		createExamDepartmentWorkers();
	}

	private void createStudents() {
		for (int i = 0; i < students.size(); i++) {
			Thread s = new Thread(students.get(i));
			threads.add(s);
		}
	}

	private void createProctor() {
		for (int i = 0; i < numOfProctors; i++) {
			Proctor p = new Proctor("proctor" + i, 40 + i, students.size(), proctorQueue, firstTeachingAssistantQueue,
					secondTeachingAssistantQueue);
			proctors.add(p);
			Thread thread = new Thread(p);
			threads.add(thread);
		}
	}

	private void createTeachingAssistant(double firstTAp, double secondTAp) {
		TeachingAssistant t1 = new TeachingAssistant("teachingAssistant" + 1, firstTAp, firstTeachingAssistantQueue, // Changed
				secondTeachingAssistantQueue, lecturerTestsQueue, listOfTestAnswers, payableStuffMembers, this);
		TeachingAssistant t2 = new TeachingAssistant("teachingAssistant" + 2, secondTAp, secondTeachingAssistantQueue,
				firstTeachingAssistantQueue, lecturerTestsQueue, listOfTestAnswers, payableStuffMembers, this);

		Thread thread1 = new Thread(t1);
		Thread thread2 = new Thread(t2);
		threads.add(thread1);
		threads.add(thread2);
	}

	private void createLecturers() {
		for (int i = 0; i < numOfLecturers; i++) {
			Lecturer l = new Lecturer("lecturer" + i, lecturerTestsQueue, exerciseCheckerQueue, students,
					payableStuffMembers, this);
			Thread thread = new Thread(l);
			threads.add(thread);

		}
	}

	private void createExerciseCheckers() {
		ExerciseChecker ex = new ExerciseChecker("ex" + 1, assignmentsGradeMap, exerciseCheckerQueue, IEMSecretaryQueue,
				payableStuffMembers, this);
		exerciseCheckers.add(ex);
		Thread thread = new Thread(ex);
		threads.add(thread);
	}

	private void createIEMSecretaries() {
		boolean whichState = false;
		for (int i = 0; i < numOfIEMSecretary; i++) {
			IEMSecretary ie = new IEMSecretary("IEMSecretary" + i, whichState, IEMSecretaryQueue, EDWQueue, this);
			Thread thread = new Thread(ie);
			threads.add(thread);
			whichState = true;
		}
	}

	private void createExamDepartmentWorkers() {
		for (int i = 0; i < numOfEDW; i++) {
			EDW edw = new EDW("edw" + i, students.size(), EDWQueue, informationSystem, this);
			Thread thread = new Thread(edw);
			threads.add(thread);
		}
	}

	private void startThreads() { // Starts all threads
		for (int i = 0; i < threads.size(); i++) {
			threads.elementAt(i).start();
		}
	}

	public void endScan() { // Ends the scan of tests made by EDW
		this.scanEnded = true;
	}

	public void endTest() { // Ends the test -> informs stuff members
		this.testEnded = true;
	}

} // Data
