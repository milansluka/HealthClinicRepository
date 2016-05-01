package ehc.bo.test;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ehc.bo.impl.User;
import ehc.bo.impl.UserManager;
import ehc.bo.impl.PartyManager;
import ehc.bo.impl.Permission;
import ehc.bo.impl.UserRightType;
import ehc.bo.impl.UserValidation;
import ehc.util.Util;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UserTest extends TestCase {
	
	private void addUser() {
		User user = new User();
		user.setLogin("milan5");
		user.setPassword("12345");
		
		PartyManager manager = new PartyManager();
		manager.addPartyRole(3, 3, user);
		
	/*	UserManager manager = new UserManager(null);
		
		User user = new User();
		user.setLogin("milan5");
		user.setPassword("12345");
			
		UserValidation validation = new UserValidation(user);

		if (validation.loginIsValid() && validation.passwordIsValid()) {
			manager.addUser(user);	    
		}
		
		User loggedUser = manager.login("milan5", "12345");
		
		assertNotNull(loggedUser);*/
	}

	public void testApp() {
		addUser();
	}
}
