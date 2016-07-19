package ehc.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class DeviceDao {
	private static DeviceDao instance = new DeviceDao();
	
	private DeviceDao() {
	}

	public static DeviceDao getInstance() {
		return instance;
	}
	
	
	public Device findById(long id) {
		Session session = HibernateUtil.getCurrentSession();	
		Device device = session.get(Device.class, id);	
		return device;
	} 
	
	public Device findByName(String name) {
	/*	HibernateUtil.beginTransaction();*/

		Session session = HibernateUtil.getCurrentSession();

		String hql = "FROM Device r WHERE r.name = :name";
		Query query = session.createQuery(hql);
		query.setParameter("name", name);
		List results = query.list();
		
		if (results.isEmpty()) {
			return null;
		}
		
		Device device = (Device)results.get(0);	
		
	/*	HibernateUtil.commitTransaction();	*/	
		return device;
	} 
	
	public List<Device> getAll() {
		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Device";
		Query query = session.createQuery(hql);
		List results = query.list();	
		return results;
	}

}
