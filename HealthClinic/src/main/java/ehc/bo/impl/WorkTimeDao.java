package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class WorkTimeDao {
	private static WorkTimeDao instance = new WorkTimeDao();
	
	private WorkTimeDao() {
	}

	public static WorkTimeDao getInstance() {
		return instance;
	}
	
	public WorkTime findById(long id) {
		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM WorkTime wt WHERE wt.id = :id";
		Query query = session.createQuery(hql).setParameter("id", id);		
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		WorkTime workTime = (WorkTime)results.get(0);	
		return workTime;
	} 
}
