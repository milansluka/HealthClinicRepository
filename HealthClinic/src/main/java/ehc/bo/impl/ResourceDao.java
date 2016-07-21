package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.bo.Resource;
import ehc.hibernate.HibernateUtil;

public class ResourceDao {
	private static ResourceDao instance = new ResourceDao();
	
	private ResourceDao() {
	}

	public static ResourceDao getInstance() {
		return instance;
	}
	

	
	public Resource findById(long id) {
		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM Resource i WHERE i.id = :id";
		Query query = session.createQuery(hql).setParameter("id", id);
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		Resource individual = (Resource)results.get(0);
		
	/*	Hibernate.initialize(individual.getSourceRoles());	
		Hibernate.initialize(individual.getTargetRoles());	*/
		
	/*	HibernateUtil.commitTransaction();	*/	
		return individual;
		
	}
	
	public Resource findByFirstAndLastName(String firstName, String lastName) {
		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM Individual u WHERE u.firstName = :firstName and u.name = :lastName";
		Query query = session.createQuery(hql);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		Resource individual = (Resource)results.get(0);
				
		return individual;
	}

}
