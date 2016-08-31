package ehc.bo.test;

import java.util.List;

import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Physician;
import ehc.bo.impl.TreatmentGroup;
import ehc.bo.impl.TreatmentGroupDao;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;

public class AddProvisionFromTreatmentGroupForPhysician extends RootTestCase {
	private IndividualDao individualDao = IndividualDao.getInstance();
	private TreatmentGroupDao treatmentGroupDao = TreatmentGroupDao.getInstance();
	
	protected void setUp() throws Exception {
		super.setUp();
		
		if (!isSystemSet()) {
			setUpSystem();
		}
	}
	
	public void testApp() {
		Login login = new Login();
		HibernateUtil.beginTransaction();
		User executor = login.login("admin", "admin");
		Individual individual = individualDao.findByFirstAndLastName("Mária", "Petrášová");	
		Physician physician = (Physician)individual.getReservableSourceRoles().get(0);
		TreatmentGroup treatmentGroup = treatmentGroupDao.findByName("Odstránenie pigmentových škvŕn");
		physician.addProvision(executor, treatmentGroup, 0.5);
		HibernateUtil.saveOrUpdate(physician);	
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		individual = individualDao.findByFirstAndLastName("Mária", "Petrášová");	
		physician = (Physician)individual.getReservableSourceRoles().get(0);
		List<TreatmentType> treatmentTypes = treatmentGroup.getTreatmentTypes();
	
		assertTrue(physician.getProvisionFromTreatmentType(treatmentTypes.get(0)) == 0.5 &&
				physician.getProvisionFromTreatmentType(treatmentTypes.get(1)) == 0.5);
		
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
