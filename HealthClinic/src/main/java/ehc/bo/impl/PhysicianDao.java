package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class PhysicianDao {
	private static PhysicianDao instance = new PhysicianDao();
	
	private PhysicianDao() {
	}

	public static PhysicianDao getInstance() {
		return instance;
	}
	
	public List<Physician> getAll() {
		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Physician";
		Query query = session.createQuery(hql);
		List results = query.list();	
		return results;
	}
}
