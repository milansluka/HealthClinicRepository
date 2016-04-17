package milansluka.HealthClinic.bo;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import milansluka.HealthClinic.AppTest;
import milansluka.HealthClinic.Utils;
import milansluka.HealthClinic.bo.impl.User;
import milansluka.HealthClinic.bo.impl.UserRight;
import milansluka.HealthClinic.bo.impl.UserRightType;


public class UserTest extends TestCase{
	
    public void testApp()
    {
		Session session = new Utils().getSession();
		session.beginTransaction();
 
		User user = new User();		
		user.setLogin("milan");
		user.setPassword("m12345");
		
    	UserRight right = new UserRight();
    	right.setType(UserRightType.CREATE_APPOINTMENT);
    	
    	user.assignRight(right);
		
		long userId = (Long)session.save(user);
		
		session.getTransaction().commit();
		
		String hql = "FROM User u WHERE u.id = :user_id";
		Query query = session.createQuery(hql);
		query.setParameter("user_id", userId);
		List results = query.list();
		
		User milan = (User)results.get(0);
		
		session.close();
		
	/*	assertNotNull(milan);*/
    	
		if (milan == null) {
			assertTrue(false);
		}
	   
    	assertTrue(milan.getRights().get(0).getType() == UserRightType.CREATE_APPOINTMENT);
    }
}
