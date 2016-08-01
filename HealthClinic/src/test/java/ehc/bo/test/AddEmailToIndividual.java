package ehc.bo.test;

import ehc.bo.impl.Email;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class AddEmailToIndividual extends RootTestCase {
    private IndividualDao individualDao = IndividualDao.getInstance();
	
	protected void setUp() throws Exception {
		super.setUp();
		
		if (!isSystemSet()) {
			setUpSystem();
		}
	}
	
	public void testApp() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Individual individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
	    Email email = new Email(executor, "janko.mrkvicka@gmail.com", DateUtil.now(), individual);
	    long emailId = (long)HibernateUtil.save(email);	
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		email = HibernateUtil.get(Email.class, emailId);
		assertTrue(email.getEmailAddress().equals("janko.mrkvicka@gmail.com"));	
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
