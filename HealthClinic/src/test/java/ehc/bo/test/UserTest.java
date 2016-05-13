package ehc.bo.test;

import org.hibernate.Session;

import ehc.bo.impl.Individual;
import ehc.bo.impl.Login;
import ehc.bo.impl.User;
import ehc.bo.impl.UserValidation;
import ehc.hibernate.HibernateUtil;
import junit.framework.TestCase;

public class UserTest extends TestCase {
	
	private void addSuperUser() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Login login = new Login(session);
		User loggedUser = login.login("superuser", "12345");
		HibernateUtil.closeCurrentSession();
		
		
		if (loggedUser == null) {
			Individual individual = new Individual(null, "Super user", "Super user", "000");
			
			User user = new User(null, "superuser", "12345");
			
			individual.AddTargetRole(user);
			individual.AddSourceRole(user);
			
	        HibernateUtil.beginTransaction();       
	        HibernateUtil.save(individual);       
	        HibernateUtil.commitTransaction();			
		}	
		
		session = HibernateUtil.getSessionFactory().openSession();
		
		login = new Login(session);
		loggedUser = login.login("superuser", "12345");
		
		assertNotNull(loggedUser);
		
		HibernateUtil.closeCurrentSession();
		
		
	}
	
	//add admin
	private void addUser() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Login login = new Login(session);
		User loggedUser = login.login("superuser", "12345");
		HibernateUtil.closeCurrentSession();
		
		if (loggedUser != null) {
			Individual individual = new Individual(loggedUser, "Milan", "Sluka", "1111");
			User user = new User(loggedUser, "admin2", "admin123");
			
			session = HibernateUtil.getSessionFactory().openSession();
			
			UserValidation validation = new UserValidation(user);
		
			if (validation.loginIsValid(session)) {
				individual.AddTargetRole(user);
				individual.AddSourceRole(user);
				
				HibernateUtil.beginTransaction();
				HibernateUtil.save(individual);
				HibernateUtil.commitTransaction();
				
			}
			
			HibernateUtil.closeCurrentSession();					
		
		}
		
		session = HibernateUtil.getSessionFactory().openSession();
		login = new Login(session);
		loggedUser = login.login("admin", "admin123");
		HibernateUtil.closeCurrentSession();
		
		assertNotNull(loggedUser);
		
		if (loggedUser != null) {
			assertTrue(loggedUser.getLogin().equals("admin") && loggedUser.getPassword().equals("admin123"));
		}
	
	}

	public void testApp() {
		addSuperUser();
		addUser();
	}
}
