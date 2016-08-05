package ehc.bo.test;

import ehc.bo.impl.CreditCard;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.PaymentChannel;
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
	    PaymentChannel paymentChannel = new CreditCard(executor, individual, "0123456789", DateUtil.date(2018, 9, 9));
	    HibernateUtil.save(paymentChannel);	
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
		CreditCard creditCard = (CreditCard)individual.getPaymentChannels().get(0);
		assertNotNull(creditCard.getCardNumber().equals("0123456789") && creditCard.getCardExpiry().equals(DateUtil.date(2018, 9, 9)));
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
