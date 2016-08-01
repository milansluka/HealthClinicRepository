package ehc.bo.test;

import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Phone;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class AddPhoneNumberToIndividual extends RootTestCase {
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
	    Phone phone = new Phone(executor, "0910456789", DateUtil.now(), individual);
	    long phoneId = (long)HibernateUtil.save(phone);	
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		phone = HibernateUtil.get(Phone.class, phoneId);
		assertTrue(phone.getPhoneNumber().equals("0910456789"));	
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
