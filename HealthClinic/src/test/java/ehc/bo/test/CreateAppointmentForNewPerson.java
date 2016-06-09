package ehc.bo.test;

import java.util.Date;

import ehc.bo.impl.Appointment;
import ehc.bo.impl.Individual;
import ehc.bo.impl.Login;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import ehc.util.Util;
import junit.framework.TestCase;

public class CreateAppointmentForNewPerson extends TestCase {
	private String treatmentName = "some treatment";
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();

	protected void setUp() throws Exception {
		super.setUp();

		HibernateUtil.beginTransaction();
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentName);
		HibernateUtil.commitTransaction();

		if (treatmentType == null) {
			HibernateUtil.beginTransaction();
			Login login = new Login();
			User executor = login.login("admin", "admin");
			treatmentType = new TreatmentType(executor, treatmentName, "some treatment type");
			HibernateUtil.save(treatmentType);
			HibernateUtil.commitTransaction();
		}

	}

	public void testApp() {
		String personFirstName = "Jan";
		String personLastName = "Novak";
			
		Login login = new Login();

		HibernateUtil.beginTransaction();
	
		User executor = login.login("admin", "admin");
		Individual person = new Individual(executor, personFirstName, personLastName);
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentName);
		Date from = DateUtil.date(2016, 4, 20, 10, 0, 0);
		Date to = DateUtil.date(2016, 4, 20, 10, 30, 0);
		Appointment appointment = new Appointment(executor, from, to, treatmentType, person);
		
		HibernateUtil.save(appointment);
		
		HibernateUtil.commitTransaction();
		
	
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
