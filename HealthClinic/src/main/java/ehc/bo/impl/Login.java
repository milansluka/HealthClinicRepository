package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class Login {
	 private boolean loginSuccess; 

	private Session session;

	public Login(Session session) {
		super();
		this.session = session;
		loginSuccess = false;
	}

	public boolean tryLogin(User user) {
		if (session != null && user != null) {
			String hql = "FROM User u WHERE u.login = :login and u.password = :password";
			Query query = session.createQuery(hql);
			query.setParameter("login", user.getLogin());
			query.setParameter("password", user.getPassword());
			List results = query.list();
			
			loginSuccess = true;
			
			return !results.isEmpty();
		}
		return false;
	}

	public boolean isLoginSuccess() {
		return loginSuccess;
	}	
}
