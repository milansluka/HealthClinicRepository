package ehc.bo.test;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ehc.bo.impl.User;
import ehc.bo.impl.UserManager;
import ehc.bo.impl.Permission;
import ehc.bo.impl.UserRightType;
import ehc.bo.impl.UserValidation;
import ehc.util.Util;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UserTest extends TestCase {
	
	private void addUser() {	
		UserManager manager = new UserManager(null);
		
		User user = new User();
		user.setLogin("milan5");
		user.setPassword("12345");
			
		UserValidation validation = new UserValidation(user);

		if (validation.loginIsValid() && validation.passwordIsValid()) {
			manager.addUser(user);
		
		}
		
		User loggedUser = manager.login("milan5", "12345");
		
		assertNotNull(loggedUser);
	}

	public void testApp() {
		addUser();
/*		Session session = new Util().getSession();
		session.beginTransaction();

		User user = new User();
		user.setLogin("milan");
		user.setPassword("m12345");

		Permission right = new Permission();
		right.setType(UserRightType.CREATE_APPOINTMENT);

		user.assignRight(right);

		long userId = (Long) session.save(user);

		session.getTransaction().commit();

		String hql = "FROM User u WHERE u.id = :user_id";
		Query query = session.createQuery(hql);
		query.setParameter("user_id", userId);
		List results = query.list();

		User milan = (User) results.get(0);

		session.close();

		 assertNotNull(milan); 

		if (milan == null) {
			assertTrue(false);
		}

		assertTrue(milan.getRights().get(0).getType() == UserRightType.CREATE_APPOINTMENT);*/
	}
}
