
public class Test {
	private long id; 
	private String date;
	private int testRoom;
	private boolean [] studentAnswers = new boolean[20]; 
	private int gradeWithoutFactor;
	private int gradeWithFactor;
	private int finalGrade;
	private String status;
	public final int eachQuestionGrade = 5;
	
	public Test () {
		this.gradeWithoutFactor = 0;
		this.gradeWithFactor = 0;
		this.finalGrade = 0;
		this.status = "BeforeExam";
	}
	
	public String getStatus () {
		return this.status;
	}
	
	public boolean getStudentAnswers (int i) {
		return this.studentAnswers[i];
	}
	
	public int getGradeWithoutFactor() { 
		return this.gradeWithoutFactor;
	}
	
	public int getGradeWithFactor() { 
		return this.gradeWithFactor;
	}
	
	public long getID() { 
		return this.id;
	}
	
	public void setID (long studentID) {
		this.id = studentID;
	}
	
	public void setDate (String testDate) {
		this.date = testDate;
	}
	
	public void setStatus (String newStatus) {
		this.status = newStatus;
	}
	
	public void setRoom (int studentRoom) {
		this.testRoom = studentRoom;
	}
	
	public void setGradeWithoutFactor (int grade) {
		this.gradeWithoutFactor = grade;
	}
	
	public void setGradeWithFactor (int grade) {
		this.gradeWithFactor = grade;
	}
	
	public void setStudentAnswers (int i) {
		this.studentAnswers[i] = true;
	}

}
