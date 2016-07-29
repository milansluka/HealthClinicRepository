package ehc.bo.test;

import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Physician;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;

public class SetProvisionOfTreatmentTypeForPhysician extends RootTestCase {
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
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
		Individual individual = individualDao.findByFirstAndLastName("Mária", "Petrášová");
		TreatmentType treatmentType = treatmentTypeDao.findByName("Odstraňovanie pigmentov chrbát");
		Physician physician = (Physician)individual.getReservableSourceRoles().get(0);
		physician.addProvision(executor, treatmentType, 0.5);
		HibernateUtil.saveOrUpdate(physician);
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		individual = individualDao.findByFirstAndLastName("Mária", "Petrášová");
		physician = (Physician)individual.getReservableSourceRoles().get(0);
		assertTrue(physician.getTreatmentTypeProvisions().get(0).getProvisionAmount() == 0.5 &&
				physician.getTreatmentTypeProvisions().get(0).getTreatmentType().getName().equals("Odstraňovanie pigmentov chrbát"));
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
