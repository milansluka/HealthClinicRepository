package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class NurseDao {
	private static NurseDao instance = new NurseDao();
	
	private NurseDao() {
	}

	public static NurseDao getInstance() {
		return instance;
	}
	
	public List<Nurse> getAll() {
		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Nurse";
		Query query = session.createQuery(hql);
		List results = query.list();	
		return results;
	}

}
