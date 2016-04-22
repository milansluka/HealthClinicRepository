package ehc.bo.impl;

import java.util.List;

import org.hibernate.Hibernate;
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
		Intervention intervention = findIntervention(appointment.getIntervention());
		
		if (intervention == null) {
			return;
		}
		
		
		appointment.assignIntervention(intervention);

		Session session = sessionFactory.openSession();
		session.beginTransaction();

		if (existingPerson == null) {
			
			System.out.println("Creating appointment for new person");			
		/*	session.save(appointment.getPerson());*/
			session.save(appointment);
			

		} else {
			System.out.println("Creating appointment for existing person");
			
			
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
			Hibernate.initialize(foundPerson.getAppointments());
		}
		
		session.close();
		
		return foundPerson;
	}
	
	private Intervention findIntervention(Intervention intervention) {
		Session session = sessionFactory.openSession();

		String hql = "FROM Intervention i WHERE i.name = :name";
		Query query = session.createQuery(hql);
		query.setParameter("name", intervention.getName());
		List results = query.list();
		
		Intervention foundIntervention = null;
		
		if (!results.isEmpty()) {
			foundIntervention = (Intervention)results.get(0);	
			Hibernate.initialize(foundIntervention.getAppointments());
		}
		
		
		
		session.close();
		
		return foundIntervention;
	}

}
