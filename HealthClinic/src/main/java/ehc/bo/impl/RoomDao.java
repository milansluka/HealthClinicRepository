package ehc.bo.impl;

import java.util.List;

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
	
	
	public Room findById(long id) {
		Session session = HibernateUtil.getCurrentSession();	
		Room room = session.get(Room.class, id);	
		return room;
	} 
	
	public Room findByName(String name) {
	/*	HibernateUtil.beginTransaction();*/

		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Room r WHERE r.name = :name";	
		List results = session.createQuery(hql).setParameter("name", name).list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		Room room = (Room)results.get(0);			
	/*	HibernateUtil.commitTransaction();	*/	
		return room;
	} 
	
	public List<Room> getAll() {
		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Room";
		List results =  session.createQuery(hql).list();	
		return results;
	}

}
