package ehc.bo.test;

import ehc.bo.impl.Login;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import junit.framework.TestCase;

public class UserLoginCorrectLoginAndPassword extends TestCase {
	public void testApp() {
		String userLogin = "admin";
		String userPassword = "admin";
		
		Login login = new Login();
		
		HibernateUtil.beginTransaction();
		User loggedUser = login.login(userLogin, userPassword);
		HibernateUtil.commitTransaction();
		
		assertTrue(loggedUser.getLogin().equals(userLogin) && 
				loggedUser.getPassword().equals(userPassword));
		
	}

}
