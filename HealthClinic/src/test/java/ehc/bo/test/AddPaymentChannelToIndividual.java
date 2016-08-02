package ehc.bo.test;

import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.PaymentChannel;
import ehc.bo.impl.Phone;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class AddPaymentChannelToIndividual extends RootTestCase {
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
	    PaymentChannel paymentChannel = new PaymentChannel(executor, individual);
	    HibernateUtil.save(paymentChannel);	
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
		paymentChannel = individual.getPaymentChannels().get(0);
		assertNotNull(paymentChannel);
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
