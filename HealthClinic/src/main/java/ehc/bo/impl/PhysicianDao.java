package ehc.bo.impl;

public class PhysicianDao {
	private static PhysicianDao instance = new PhysicianDao();
	
	private PhysicianDao() {
	}

	public static PhysicianDao getInstance() {
		return instance;
	}
	
	

}
