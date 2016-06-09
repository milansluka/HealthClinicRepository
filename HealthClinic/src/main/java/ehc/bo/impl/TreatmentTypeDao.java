package ehc.bo.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class TreatmentTypeDao {
	private static TreatmentTypeDao instance = new TreatmentTypeDao();
	
	private TreatmentTypeDao() {
	}

	public static TreatmentTypeDao getInstance() {
		return instance;
	}
	
	public TreatmentType findByName(String name) {
		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM TreatmentType t WHERE t.name = :name";
		Query query = session.createQuery(hql).setParameter("name", name);
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		TreatmentType treatmentType = (TreatmentType)results.get(0);
		
	/*	Hibernate.initialize(individual.getSourceRoles());	
		Hibernate.initialize(individual.getTargetRoles());*/	
		
	/*	HibernateUtil.commitTransaction();	*/	
		return treatmentType;
	}

}
