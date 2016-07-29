package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class WaitingIndividualDao {
	private static WaitingIndividualDao instance = new WaitingIndividualDao();
	
	private WaitingIndividualDao() {
	}

	public static WaitingIndividualDao getInstance() {
		return instance;
	}
	
	public WaitingIndividual findById(long id) {
		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM WaitingIndividual i WHERE i.id = :id";
		Query query = session.createQuery(hql).setParameter("id", id);
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		WaitingIndividual waitingIndividual = (WaitingIndividual)results.get(0);	
		
	/*	HibernateUtil.commitTransaction();	*/	
		return waitingIndividual;	
	}
}
