package ehc.bo.test;

import ehc.bo.impl.Company;
import ehc.bo.impl.CompanyDao;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.User;
import ehc.bo.impl.UserDao;
import ehc.bo.impl.UserLoginAndPasswordValidation;
import ehc.hibernate.HibernateUtil;
import ehc.util.Util;
import junit.framework.TestCase;

public class CreateUserValidLoginAndPassword extends TestCase {
	private IndividualDao individualDao;
	private UserDao userDao;
	private CompanyDao companyDao;

	private String personFirstName = "Milan";
	private String personLastName = "Sluka";
	private String newUserLogin = "milan";
	private String newUserPassword = "m123";

	protected void setUp() throws Exception {
		super.setUp();

		individualDao = IndividualDao.getInstance();
		userDao = UserDao.getInstance();
		companyDao = CompanyDao.getInstance();

		HibernateUtil.beginTransaction();

		Individual individual = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		User executor = userDao.findByName("admin");

		if (individual == null) {
			individual = new Individual(executor, personFirstName, personLastName);
		}

		HibernateUtil.save(individual);
		HibernateUtil.commitTransaction();

	}

	public void testApp() {
		HibernateUtil.beginTransaction();
		
		Login login = new Login();
		User executor = login.login("admin", "admin");

		Individual individual = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		Company company = companyDao.findByName("Company name");
	
		UserLoginAndPasswordValidation validation = new UserLoginAndPasswordValidation(newUserLogin, newUserPassword);
		
		if (validation.loginIsValid() && validation.passwordIsValid()) {
			String newUserPasswordCrypted = (new Util()).cryptWithMD5(newUserPassword);
			User newUser = new User(executor, newUserLogin, newUserPasswordCrypted, individual, company);
			HibernateUtil.saveOrUpdate(newUser);		
		}
		

		HibernateUtil.commitTransaction();
		
		//New user is trying log in
		
		
		HibernateUtil.beginTransaction();
		String newUserPasswordCrypted = (new Util()).cryptWithMD5(newUserPassword);
		User newUserLogged = login.login(newUserLogin, newUserPasswordCrypted);
		HibernateUtil.commitTransaction();
		
		assertTrue(newUserLogged.getName().equals(newUserLogin) && newUserLogged.getPassword().equals(newUserPasswordCrypted));	
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		HibernateUtil.beginTransaction();

		Company company = companyDao.findByName("Company name");
		Individual individual = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		User user = userDao.findByName(newUserLogin);

		company.getTargetRoles().remove(user);
		individual.getSourceRoles().remove(user);

		HibernateUtil.saveOrUpdate(company);
		HibernateUtil.saveOrUpdate(individual);

		HibernateUtil.commitTransaction();

	}

}
