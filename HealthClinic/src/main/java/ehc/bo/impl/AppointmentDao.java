package ehc.bo.impl;

import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class AppointmentDao {
	private static AppointmentDao instance = new AppointmentDao();
	
	public static AppointmentDao getInstance() {
		return instance;
	}
	
	public Appointment findById(long id) {
		Session session = HibernateUtil.getCurrentSession();	
		Appointment appointment = session.get(Appointment.class, id);	
		return appointment;
	} 

}
