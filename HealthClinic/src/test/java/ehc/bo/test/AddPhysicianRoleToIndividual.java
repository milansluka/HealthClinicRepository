package ehc.bo.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import ehc.bo.impl.Company;
import ehc.bo.impl.CompanyDao;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Physician;
import ehc.bo.impl.PhysicianType;
import ehc.bo.impl.ResourcePartyRole;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;

public class AddPhysicianRoleToIndividual extends RootTestCase {
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

		PhysicianType physicianType = new PhysicianType(executor);
		Physician physician = new Physician(executor, physicianType, source, target);
		HibernateUtil.save(physician);
		HibernateUtil.commitTransaction();

		HibernateUtil.beginTransaction();
		Individual individual = individualDao.findById(individualIds.get(0));
		Hibernate.initialize(individual.getReservableSourceRoles());
		HibernateUtil.commitTransaction();

		ResourcePartyRole persistedRole = individual.getReservableSourceRoles().get(0);

		assertTrue(persistedRole instanceof Physician);

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		for (long id : individualIds) {
			removeIndividual(id);
		}
	}

}
