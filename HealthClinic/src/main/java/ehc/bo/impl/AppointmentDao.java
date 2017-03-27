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
//		String hql = "FROM Appointment a WHERE a.from >= :from and a.from <= :to or "
//				+ "a.to >= :from and a.to <= :to";
		
		
		String hql = "FROM Appointment a WHERE (a.from <= :from and a.to > :from)"
				+ "or (a.from >= :from and a.from < :to)";
		
		Query query = session.createQuery(hql);
		query.setParameter("from", from);
		query.setParameter("to", to);
		List results = query.list();
		return results;
	}
	
	//get all appointment that are not within from1 and to1, but are intersecting with from2 and to2
	//get all appointments from certain time intersecting with from2 and to2
	public List<Appointment> getAllIntersectingFrom(Date from1, Date from2, Date to2) {
		Session session = HibernateUtil.getCurrentSession();
		String hql = "FROM Appointment a WHERE a.from > :from1 and (a.from >= :from2 and a.from <= :to2 or "
				+ "a.to >= :from2 and a.to <= :to2)";
		Query query = session.createQuery(hql);
		query.setParameter("from1", from1);
		query.setParameter("from2", from2);
		query.setParameter("to2", to2);
		List results = query.list();
		return results;
	}
	
}
