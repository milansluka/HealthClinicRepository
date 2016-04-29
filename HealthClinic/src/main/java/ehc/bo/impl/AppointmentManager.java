package ehc.bo.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import ehc.bo.AppointmentDao;
import ehc.bo.IndividualDao;
import ehc.bo.TreatmentDao;

public class AppointmentManager {
	private AppointmentDao appointmentDao;
	private IndividualDao individualDao;
	private TreatmentDao treatmentDao;

	public AppointmentManager() {
		super();
	}

	public void createAppointment(Appointment appointment) {
		if (appointment == null) {
			return;
		}
		if (appointment.getPerson() == null) {
			return;
		}
		if (appointment.getTreatment() == null) {
			return;
		}

		Individual existingPerson = individualDao.findIndividual(appointment.getPerson());
		Treatment treatment = treatmentDao.findTreatment(appointment.getTreatment());

		if (treatment == null) {
			return;
		}

		appointment.assignTreatment(treatment);

		if (existingPerson == null) {
			System.out.println("Creating appointment for new person");
			appointmentDao.addAppointment(appointment);

		} else {
			System.out.println("Creating appointment for existing person");
			appointment.assignPerson(existingPerson);
			appointmentDao.addAppointment(appointment);
		}
	}

/*	private Individual findPerson(Individual person) {

		return foundPerson;
	}*/

/*	private Treatment findIntervention(Treatment intervention) {
		Session session = sessionFactory.openSession();

		String hql = "FROM Intervention i WHERE i.name = :name";
		Query query = session.createQuery(hql);
		query.setParameter("name", intervention.getName());
		List results = query.list();

		Treatment foundIntervention = null;

		if (!results.isEmpty()) {
			foundIntervention = (Treatment) results.get(0);
			Hibernate.initialize(foundIntervention.getAppointments());
		}

		session.close();

		return foundIntervention;
	}*/

}
