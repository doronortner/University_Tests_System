
abstract class Employee implements Runnable {
	protected String name;

	public Employee() {
	}

	abstract protected void simulateProcess();

	abstract protected void updateTestStatus(Test t);

	abstract protected void placeInNextQueue(Test t);
}