package ehc.bo.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class IndividualDao {
	private static IndividualDao instance = new IndividualDao();
	
	private IndividualDao() {
	}

	public static IndividualDao getInstance() {
		return instance;
	}
	
	public Individual findByFirstAndLastName(String firstName, String lastName) {
		HibernateUtil.beginTransaction();

		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM Individual u WHERE u.firstName = :firstName and u.name = :lastName";
		Query query = session.createQuery(hql);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		Individual individual = (Individual)results.get(0);
		
		Hibernate.initialize(individual.getSourceRoles());	
		Hibernate.initialize(individual.getTargetRoles());	
		
		HibernateUtil.commitTransaction();	
		
		
		return individual;
	}

}
