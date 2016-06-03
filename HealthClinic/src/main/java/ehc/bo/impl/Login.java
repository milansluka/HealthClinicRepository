package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class Login {
	 private boolean loginSuccess; 



	public Login() {
		super();

		loginSuccess = false;
	}
	
	public User login(String login, String password) {
		HibernateUtil.beginTransaction();
		Session session = HibernateUtil.getCurrentSession();
		
		String hql = "FROM User u WHERE u.login = :login and u.password = :password";
		Query query = session.createQuery(hql);
		query.setParameter("login", login);
		query.setParameter("password", password);
		List results = query.list();
		
		HibernateUtil.commitTransaction();
		
		if (results.isEmpty()) {
			return null;
		}

		return (User)results.get(0);
	}

	public boolean tryLogin(User user) {
		if (user != null) {
			HibernateUtil.beginTransaction();
			Session session = HibernateUtil.getCurrentSession();
			
			String hql = "FROM User u WHERE u.login = :login and u.password = :password";
			Query query = session.createQuery(hql);
			query.setParameter("login", user.getLogin());
			query.setParameter("password", user.getPassword());
			List results = query.list();
			
			HibernateUtil.commitTransaction();
			
			loginSuccess = true;
			
			return !results.isEmpty();
		}
		return false;
	}

	public boolean isLoginSuccess() {
		return loginSuccess;
	}	
}
