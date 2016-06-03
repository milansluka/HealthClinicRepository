package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class UserDao {
	private static UserDao instance = new UserDao();

	private UserDao() {
	}

	public static UserDao getInstance() {
		return instance;
	}
	
	public User findByLogin(String login) {
		HibernateUtil.beginTransaction();

		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM User u WHERE u.login = :login";
		Query query = session.createQuery(hql);
		query.setParameter("login", login);
		List results = query.list();
		
		HibernateUtil.commitTransaction();	
		
		if (results.isEmpty()) {
			return null;
		}
		return (User)results.get(0);
	}
}
