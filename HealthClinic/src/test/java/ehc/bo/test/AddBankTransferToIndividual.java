package ehc.bo.test;

import ehc.bo.impl.BankTransfer;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.PaymentChannel;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;

public class AddBankTransferToIndividual extends RootTestCase {
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
		    PaymentChannel paymentChannel = new BankTransfer(executor, individual, "012345", "01234567");
		    HibernateUtil.save(paymentChannel);	
			HibernateUtil.commitTransaction();
			
			HibernateUtil.beginTransaction();
			individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
			BankTransfer bankTransfer = (BankTransfer)individual.getPaymentChannels().get(0);
			assertNotNull(bankTransfer.getSortCode().equals("012345") && bankTransfer.getAccountNumber().equals("01234567"));
			HibernateUtil.commitTransaction();
		}

		protected void tearDown() throws Exception {
			super.tearDown();
			tearDownSystem();
		}

}
