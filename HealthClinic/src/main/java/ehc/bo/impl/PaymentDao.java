package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class PaymentDao {
	private static PaymentDao instance = new PaymentDao();
	
	private PaymentDao() {
	}

	public static PaymentDao getInstance() {
		return instance;
	}
	
	public Payment findById(long id) {
		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM Payment t WHERE t.id = :id";
		Query query = session.createQuery(hql).setParameter("id", id);		
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		Payment payment = (Payment)results.get(0);	
		return payment;
	} 

}
