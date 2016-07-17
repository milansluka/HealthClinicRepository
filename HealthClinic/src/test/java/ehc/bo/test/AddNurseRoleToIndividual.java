package ehc.bo.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import ehc.bo.impl.Company;
import ehc.bo.impl.CompanyDao;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Nurse;
import ehc.bo.impl.NurseType;
import ehc.bo.impl.ResourcePartyRole;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;

public class AddNurseRoleToIndividual extends RootTestCase {

	private List<Long> individualIds = new ArrayList<Long>();

	protected void setUp() throws Exception {
		super.setUp();

		long id = addIndividual("test Milan", "test Sluka");
		individualIds.add(id);
	}

	public void testApp() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		IndividualDao individualDao = IndividualDao.getInstance();
		Individual source = individualDao.findById(individualIds.get(0));
		CompanyDao companyDao = CompanyDao.getInstance();
		Company target = companyDao.findByName("Company name");

		NurseType nurseType = new NurseType(executor);
		Nurse nurse = new Nurse(executor, nurseType, source, target);
		HibernateUtil.save(nurse);
		HibernateUtil.commitTransaction();

		HibernateUtil.beginTransaction();
		Individual individual = individualDao.findById(individualIds.get(0));
		Hibernate.initialize(individual.getReservableSourceRoles());
		HibernateUtil.commitTransaction();
		
		ResourcePartyRole persistedRole = individual.getReservableSourceRoles().get(0);	
	
		assertTrue(persistedRole instanceof Nurse);

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		for (long id : individualIds) {
			removeIndividual(id);
		}
	}

}
