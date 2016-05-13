package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import ehc.util.Util;

public class UserValidation {
    private Session currentSession;
    
	private User user;

	public UserValidation(User user) {
		super();
		this.user = user;
	}
	
	private void openCurrentSession() {
		SessionFactory sessionFactory = new Util().getSessionFactory();
		currentSession = sessionFactory.openSession();
	}
	
	private void closeCurrentSession() {
		SessionFactory sessionFactory = new Util().getSessionFactory();
		currentSession.close();
	}

	
	//checks if login name is unique
	public boolean loginIsValid(Session session) {
		if (user == null) {
			return false;
		}

		String login = user.getLogin();

		if (!login.isEmpty())
		{
		
			
			String hql = "FROM User u WHERE u.login = :login";
			Query query = session.createQuery(hql);
			query.setParameter("login", login);
			List results = query.list();
			
		

			return results.isEmpty();
		}

		return false;

	}

	public boolean passwordIsValid() {
		return true;
	}

}
