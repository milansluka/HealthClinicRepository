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

	public User findByNameAndPassword(String name, String password) {
		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM User u WHERE u.name = :name and u.password = :password";
		Query query = session.createQuery(hql);
		query.setParameter("name", name);
		query.setParameter("password", password);
		List results = query.list();

		if (results.isEmpty()) {
			return null;
		}

		return (User) results.get(0);
	}

	public User findByName(String name) {
		/* HibernateUtil.beginTransaction(); */

		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM User u WHERE u.name = :name";
		Query query = session.createQuery(hql);
		query.setParameter("name", name);
		List results = query.list();

		/* HibernateUtil.commitTransaction(); */

		if (results.isEmpty()) {
			return null;
		}
		return (User) results.get(0);
	}
}
