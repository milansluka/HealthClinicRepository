package ehc.bo.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import ehc.bo.AppointmentDao;
import ehc.bo.IndividualDao;
import ehc.bo.TreatmentDao;
import ehc.bo.TreatmentTypeDao;

public class AppointmentManager {
	private AppointmentDao appointmentDao;
	private IndividualDao individualDao;
	private TreatmentTypeDao treatmentTypeDao;

	public AppointmentManager() {
		super();
		appointmentDao = new AppointmentDaoImpl();
		individualDao = new IndividualDaoImpl();
		treatmentTypeDao = new TreatmentTypeDaoImpl();
	}

	public void createAppointment(Appointment appointment) {
		if (appointment == null) {
			return;
		}
		if (appointment.getIndividual() == null) {
			return;
		}
		if (appointment.getTreatmentType() == null) {
			return;
		}

		Individual existingPerson = individualDao.findIndividual(appointment.getIndividual());
		TreatmentType treatmentType = treatmentTypeDao.find(appointment.getTreatmentType());

		if (treatmentType == null) {
			return;
		}
	
		appointment.assignTreatmentType(treatmentType);

		if (existingPerson == null) {
			System.out.println("Creating appointment for new person");
		
			individualDao.addIndividual(appointment.getIndividual());
			appointmentDao.addAppointment(appointment);

		} else {
			System.out.println("Creating appointment for existing person");
			appointment.assignPerson(existingPerson);
			appointmentDao.addAppointment(appointment);
		}
	}
}
