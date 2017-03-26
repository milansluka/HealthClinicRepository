package ehc.bo.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class AppointmentDao {
	private static AppointmentDao instance = new AppointmentDao();

	public static AppointmentDao getInstance() {
		return instance;
	}

	public Appointment findById(long id) {
		Session session = HibernateUtil.getCurrentSessionWithTransaction();
		Appointment appointment = session.get(Appointment.class, id);
		return appointment;
	}

	public Appointment findByDateAndSubject(Date when, Individual subject) {
		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Appointment a WHERE a.from = :when and a.individual = :subject";
		Query query = session.createQuery(hql);
		query.setParameter("when", when);
		query.setParameter("subject", subject);
		List results = query.list();

		if (results.isEmpty()) {
			return null;
		}
		Appointment appointment = (Appointment) results.get(0);
		return appointment;
	}

	public List<Appointment> getAll() {
		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Appointment";
		Query query = session.createQuery(hql);
		List results = query.list();
		return results;
	}

	public List<Appointment> getAll(Date from, Date to) {
		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Appointment a WHERE a.from >= :from and a.to <= :to";
		Query query = session.createQuery(hql);
		query.setParameter("from", from);
		query.setParameter("to", to);
		List results = query.list();
		return results;
	}
	
	public List<Appointment> getAllIntersecting(Date from, Date to) {
		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Appointment a WHERE a.from >= :from and a.from <= :from or "
				+ "a.to >= :to and a.to <= :to";
		Query query = session.createQuery(hql);
		query.setParameter("from", from);
		query.setParameter("to", to);
		List results = query.list();
		return results;
	}
}
