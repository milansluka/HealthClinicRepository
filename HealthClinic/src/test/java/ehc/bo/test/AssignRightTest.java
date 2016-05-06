package ehc.bo.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ehc.bo.impl.User;
import ehc.bo.impl.Permission;
import ehc.bo.impl.UserPermissionType;
import junit.framework.TestCase;

public class AssignRightTest extends TestCase {
	private SessionFactory sessionFactory;

	public void testApp() {
		assignRightNewUser();
/*		sessionFactory = new Configuration().configure().buildSessionFactory();

		Session session = sessionFactory.openSession();
		
		String hql = "FROM User u WHERE u.id = :userId";
		long userId = 11;
		Query query = session.createQuery(hql);
		query.setParameter("userId", userId);
		List results = query.list();	
		User user = (User)results.get(0);
		
	    Hibernate.initialize(user.getRights());
		
		for (UserRight r : user.getRights()) {
			System.out.println("right: " + r.getType());
		}
		
		session.close();
		
		Session session2 = sessionFactory.openSession();
		
		session2.beginTransaction();
				
		UserRight right = new UserRight();
		right.setType(UserRightType.CREATE_USER);
		user.assignRight(right);
		
		session2.update(user);

		session2.getTransaction().commit();
		session2.close();*/
	}
	
	public void assignRightNewUser() {
	/*	sessionFactory = new Configuration().configure().buildSessionFactory();
		
		User user = new User();
		user.setLogin("someadmin");
		user.setPassword("1232");
		
		Permission createAppointment = new Permission();
		createAppointment.setType(UserRightType.CREATE_APPOINTMENT);
		Permission createUser = new Permission();
		createUser.setType(UserRightType.CREATE_USER);
		
		user.assignRight(createAppointment);
		user.assignRight(createUser);

		Session session = sessionFactory.openSession();
		session.save(user);
		session.close();	
		*/
	}

}
