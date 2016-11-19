package ehc.bo.test;

import java.util.ArrayList;
import java.util.List;

import ehc.bo.impl.Company;
import ehc.bo.impl.CompanyDao;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Physician;
import ehc.bo.impl.PhysicianType;
import ehc.bo.impl.Skill;
import ehc.bo.impl.SkillDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import junit.framework.TestCase;

public class AddTheSameSkillToMorePhysicians extends TestCase {
	private IndividualDao individualDao = IndividualDao.getInstance();
	private SkillDao skillDao = SkillDao.getInstance();
	private CompanyDao companyDao = CompanyDao.getInstance();
	private List<Long> individualIds = new ArrayList<Long>();

	protected void setUp() throws Exception {
		super.setUp();
		Login login = new Login();

		HibernateUtil.beginTransaction();
		User executor = login.login("admin", "admin");
		Individual individual = new Individual(executor, "Milan", "Sluka");
		long individualId = (Long)HibernateUtil.save(individual);
		individualIds.add(individualId);
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		executor = login.login("admin", "admin");
		individual = new Individual(executor, "Jozko", "Mrkvicka");
		individualId = (Long)HibernateUtil.save(individual);
		individualIds.add(individualId);
		HibernateUtil.commitTransaction();
	}

	public void testApp() {
		Login login = new Login();

		HibernateUtil.beginTransaction();
		User executor = login.login("admin", "admin");
		Individual individual = individualDao.findByFirstAndLastName("Milan", "Sluka");

		Company company = companyDao.findByName("Company name");
		PhysicianType physicianType = new PhysicianType(executor);

		Skill skill = skillDao.findByName("some test skill 1");
		if (skill == null) {
			skill = new Skill(executor, "some test skill 1");
		}
		physicianType.addSkill(skill);
		Physician physician = new Physician(executor, physicianType, individual, company);
		HibernateUtil.save(physician);
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		User executor2 = login.login("admin", "admin");
		Individual individual2 = individualDao.findByFirstAndLastName("Milan", "Sluka");

		Company company2 = companyDao.findByName("Company name");
		PhysicianType physicianType2 = new PhysicianType(executor2);

		Skill skill2 = skillDao.findByName("some test skill 1");
		if (skill2 == null) {
			skill2 = new Skill(executor, "some test skill 1");
		}
		physicianType2.addSkill(skill2);
		Physician physician2 = new Physician(executor, physicianType2, individual2, company2);
		HibernateUtil.save(physician2);
		HibernateUtil.commitTransaction();

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		HibernateUtil.beginTransaction();
		for (long id : individualIds) {
			Individual individual = individualDao.findById(id);
			HibernateUtil.delete(individual);		
		}
		
		List<Skill> skills = skillDao.getAll();
		for (Skill skill : skills) {
			HibernateUtil.delete(skill);
		}
		
		HibernateUtil.commitTransaction();
		
		
	}
}
