package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class TreatmentDao {
	private static TreatmentDao instance = new TreatmentDao();
	
	private TreatmentDao() {
	}

	public static TreatmentDao getInstance() {
		return instance;
	}
	
	public Treatment findById(long id) {
		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM Treatment t WHERE t.id = :id";
		Query query = session.createQuery(hql).setParameter("id", id);		
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		Treatment treatment = (Treatment)results.get(0);	
		return treatment;
	} 
}
