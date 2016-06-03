package ehc.bo.test;

import ehc.bo.impl.Company;
import ehc.bo.impl.CompanyDao;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.User;
import ehc.bo.impl.UserDao;
import ehc.hibernate.HibernateUtil;
import junit.framework.TestCase;

public class UserTest extends TestCase {

	private void createDefaultUser() {	
		String login = "admin";
		String password = "admin123";
		
		UserDao userDao = UserDao.getInstance();	
		User user = userDao.findByLogin(login);
		
		if (user == null) {
			user = new User(null, login, password);	
			user.setCreatedBy(user);	

			CompanyDao companyDao = CompanyDao.getInstance();			
			Company company = companyDao.findByName(HealthPoint.OWNER_COMPANY_NAME);
			
			if (company == null) {
				company = new Company(user, HealthPoint.OWNER_COMPANY_NAME);		
			}
			
			company.AddSourceRole(user);
			company.AddTargetRole(user);
			
			HibernateUtil.beginTransaction();	
			HibernateUtil.saveOrUpdate(company);
			HibernateUtil.commitTransaction();	
		}

		user = userDao.findByLogin(login);
		
		assertTrue(user.getLogin().equals(login) && user.getPassword().equals(password));	
	}

	private void addUserRoleToIndividual() {
		String userLogin = "admin";
		String password = "admin123";
		
		String newUserLogin = "milan";
		String newUserPassword = "mmm123";
		
		String individualFirstName = "Milan";
		String individualLastName  = "Sluka";
		
		Login login = new Login();
		User user = login.login(userLogin, password);
		
		assertNotNull(user);
				
		if (user != null) {
			IndividualDao individualDao = IndividualDao.getInstance();
			
			Individual individual = individualDao.findByFirstAndLastName(individualFirstName, individualLastName);
			
			if (individual == null) {
				individual = createIndividual(individualFirstName, individualLastName);
			}
			
			if (individual != null) {
				
				UserDao userDao = UserDao.getInstance();
				User oldUser = userDao.findByLogin(newUserLogin);
				
				if (oldUser != null) {
					HibernateUtil.beginTransaction();	
					HibernateUtil.delete(oldUser);
					HibernateUtil.commitTransaction();				
				}
							
				User newUser = new User(user, "milan", "mmm123");
				individual.AddSourceRole(newUser);
				individual.AddTargetRole(newUser);
				
				HibernateUtil.beginTransaction();	
				HibernateUtil.saveOrUpdate(individual);
				HibernateUtil.commitTransaction();			
			}
			
		} 
	}
	
	public Individual createIndividual(String individualFirstName, String individualLastName) {
		String userLogin = "admin";
		String password = "admin123";
		
		Login login = new Login();
		User user = login.login(userLogin, password);
		
		assertNotNull(user);
		
		//if login was successful
		if (user != null) {
			Individual person = new Individual(user, individualFirstName, individualLastName, "101010");
			
			HibernateUtil.beginTransaction();	
			HibernateUtil.save(person);
			HibernateUtil.commitTransaction();
			
			return person;
			
		} 
		
		return null;
		
	}

	public void testApp() {
		createDefaultUser();
		addUserRoleToIndividual();
	}
}
