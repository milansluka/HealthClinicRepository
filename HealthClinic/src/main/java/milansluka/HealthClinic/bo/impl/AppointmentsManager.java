package milansluka.HealthClinic.bo.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;



public class AppointmentsManager {
	/* private Session session; */
	private SessionFactory sessionFactory;

	public AppointmentsManager(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}

	public void createAppointment(Appointment appointment) {
		if (appointment == null) {
			return;
		}
		if (appointment.getPerson() == null) {
			return;
		}
		if (appointment.getIntervention() == null) {
			return;
		}

		Person existingPerson = findPerson(appointment.getPerson());

		Session session = sessionFactory.openSession();
		session.beginTransaction();

		if (existingPerson == null) {
		/*	session.save(appointment.getPerson());*/
			session.save(appointment);
			

		} else {
			appointment.assignPerson(existingPerson);
			
			session.save(appointment);
		}
		
		session.getTransaction().commit();	
		session.close();

	}

	private Person findPerson(Person person) {
		Session session = sessionFactory.openSession();

		String hql = "FROM Person p WHERE p.phone = :phone";
		Query query = session.createQuery(hql);
		query.setParameter("phone", person.getPhone());
		List results = query.list();
		
		Person foundPerson = null;
		
		if (!results.isEmpty()) {
			foundPerson = (Person)results.get(0);			
		}
		
		session.close();
		
		return foundPerson;
	}

}
