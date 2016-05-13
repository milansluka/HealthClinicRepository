package ehc.bo.test;

import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.Individual;
import ehc.bo.impl.Login;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import junit.framework.TestCase;

public class CreateAppointmentTest extends TestCase {

	public void testApp() {
	/*	createAppointment();*/
	}

	public void createUser2() {

	}

	public void loginSuccessful() {

	}

	public void loginFailed() {

	}

	public void createAppointmentNewPerson() {

	}

	public void createAppointment() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Login login = new Login(session);
		User executor = login.login("admin", "admin123");
		HibernateUtil.closeCurrentSession();

		Date from = DateUtil.date(2016, 4, 20, 10, 0, 0);
		Date to = DateUtil.date(2016, 4, 20, 10, 30, 0);
		String firstName = "Milan";
		String lastName = "Sluka";
		String phone = "1111";
		String treatmentTypeStr = "some type";

		session = HibernateUtil.getSessionFactory().openSession();
		TreatmentType treatmentType = TreatmentType.getTreatmentType(treatmentTypeStr, session);
		HibernateUtil.closeCurrentSession();

		if (treatmentType == null) {
			treatmentType = new TreatmentType(executor, "some name", treatmentTypeStr);
		}

		Individual person = null;
		session = HibernateUtil.getSessionFactory().openSession();
		List<Individual> foundPersons = Individual.getIndividuals(firstName, lastName, phone, session);
		HibernateUtil.closeCurrentSession();
		long personId = 0;

		if (foundPersons.isEmpty()) {

			Individual newPerson = new Individual(executor, firstName, lastName, phone);
			HibernateUtil.beginTransaction();

			personId = (long) HibernateUtil.save(newPerson);

			HibernateUtil.commitTransaction();

			session = HibernateUtil.getSessionFactory().openSession();
			person = Individual.getPerson(personId, session);
			HibernateUtil.closeCurrentSession();

		} else {
			person = (Individual) foundPersons.get(0);
		}

		Appointment appointment = new Appointment(executor, from, to, treatmentType, person);

		HibernateUtil.beginTransaction();
		HibernateUtil.save(appointment);
		HibernateUtil.commitTransaction();

		/*
		 * HibernateUtil.beginTransaction(); User executor = new User();
		 * Appointment appointent = new Appointment(executor);
		 * HibernateUtil.save(appointent);
		 * 
		 * HibernateUtil.commitTransaction();
		 */

	}

}
