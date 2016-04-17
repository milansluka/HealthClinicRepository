package milansluka.HealthClinic.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class UserValidation {
	private SessionFactory sessionFactory;
	private User user;

	public UserValidation(User user, SessionFactory sessionFactory) {
		super();
		this.user = user;
		this.sessionFactory = sessionFactory;
	}

	
	//checks if login name is unique
	public boolean loginIsValid() {
		if (sessionFactory == null) {
			return false;
		}
		if (user == null) {
			return false;
		}

		String login = user.getLogin();

		if (!login.isEmpty())
		{
			Session session = sessionFactory.openSession();
			
			String hql = "FROM User u WHERE u.login = :login";
			Query query = session.createQuery(hql);
			query.setParameter("login", login);
			List results = query.list();
			
			session.close();

			return results.isEmpty();
		}

		return false;

	}

	public boolean passwordIsValid() {
		return true;
	}

}
