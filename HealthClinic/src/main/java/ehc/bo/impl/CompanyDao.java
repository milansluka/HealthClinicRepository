package ehc.bo.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class CompanyDao {
	private static CompanyDao instance = new CompanyDao();
	
	private CompanyDao() {
	}

	public static CompanyDao getInstance() {
		return instance;
	}
	
	public Company findByName(String name) {
		HibernateUtil.beginTransaction();

		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM Company u WHERE u.name = :name";
		Query query = session.createQuery(hql);
		query.setParameter("name", name);
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		Company company = (Company)results.get(0);
		
		Hibernate.initialize(company.getSourceRoles());	
		Hibernate.initialize(company.getTargetRoles());	
		
		HibernateUtil.commitTransaction();	
		
		
		return company;
	}
}
