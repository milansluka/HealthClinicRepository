package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class TreatmentGroupDao {
	private static TreatmentGroupDao instance = new TreatmentGroupDao();
	
	private TreatmentGroupDao() {
	}

	public static TreatmentGroupDao getInstance() {
		return instance;
	}
	
	public TreatmentGroup findById(long id) {
		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM TreatmentGroup t WHERE t.id = :id";
		Query query = session.createQuery(hql).setParameter("id", id);		
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		TreatmentGroup treatmentGroup = (TreatmentGroup)results.get(0);	
		return treatmentGroup;
	} 
	
	public TreatmentGroup findByName(String name) {
		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM TreatmentGroup t WHERE t.name = :name";
		Query query = session.createQuery(hql).setParameter("name", name);
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		TreatmentGroup treatmentGroup = (TreatmentGroup)results.get(0);
	
		return treatmentGroup;
	}

}
