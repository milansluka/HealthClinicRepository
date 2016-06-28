package ehc.bo.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class RoomDao {
	private static RoomDao instance = new RoomDao();
	
	private RoomDao() {
	}

	public static RoomDao getInstance() {
		return instance;
	}
	
	public Room findByName(String name) {
	/*	HibernateUtil.beginTransaction();*/

		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM Room r WHERE r.name = :name";
		Query query = session.createQuery(hql);
		query.setParameter("name", name);
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		Room room = (Room)results.get(0);	
		
	/*	HibernateUtil.commitTransaction();	*/	
		return room;
	} 

}
