package ehc.bo.test;

import ehc.bo.impl.Day;
import ehc.bo.impl.Login;
import ehc.bo.impl.User;
import ehc.bo.impl.WorkTime;
import ehc.hibernate.HibernateUtil;
import junit.framework.TestCase;

public class CreateWorkTime extends TestCase {
	private long workTimeId;
	
	protected void setUp() throws Exception {
		super.setUp();
	}
		
	public void testApp() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		WorkTime workTime = new WorkTime(executor);
		workTime.addDay(new Day(executor, "Nedeľa", 7, 0, 18, 0));
		workTime.addDay(new Day(executor, "Pondelok", 7, 0, 18, 0));
		workTime.addDay(new Day(executor, "Utorok", 8, 30, 18, 0));
		workTime.addDay(new Day(executor, "Streda", 9, 0, 14, 0));
		workTime.addDay(new Day(executor, "Štvrtok", 7, 30, 18, 0));
		workTime.addDay(new Day(executor, "Piatok", 7, 0, 18, 0));
		workTime.addDay(new Day(executor, "Sobota", 7, 0, 18, 0));
		workTimeId = (Long)HibernateUtil.save(workTime);
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		HibernateUtil.beginTransaction();
		WorkTime workTime = HibernateUtil.get(WorkTime.class, workTimeId);
		HibernateUtil.delete(workTime);
		HibernateUtil.commitTransaction();
	}

}
