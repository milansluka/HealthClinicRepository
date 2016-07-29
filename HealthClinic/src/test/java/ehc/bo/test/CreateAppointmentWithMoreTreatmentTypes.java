package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class CreateAppointmentWithMoreTreatmentTypes extends RootTestCase {
	private IndividualDao individualDao = IndividualDao.getInstance();
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();

	protected void setUp() throws Exception {
		super.setUp();
		if (!isSystemSet()) {
			setUpSystem();	
		}
		
		HibernateUtil.beginTransaction();
		Individual existingPerson = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
		HibernateUtil.commitTransaction();
		if (existingPerson == null) {
			Login login = new Login();
			
			HibernateUtil.beginTransaction();
			User executor = login.login("admin", "admin");
			Individual person = new Individual(executor, "Janko", "Mrkvicka");
			HibernateUtil.save(person);		
			HibernateUtil.commitTransaction();
			
		}
	
	}
	
	public void testApp() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin",  "admin");
		Individual individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
		
		TreatmentType treatmentType = treatmentTypeDao.findByName("Odstraňovanie pigmentov chrbát");
		TreatmentType treatmentType2 = treatmentTypeDao.findByName("OxyGeneo - tvár");
		Date from = DateUtil.date(2016, 7, 28, 9, 45, 0);
		Date to = DateUtil.date(2016, 7, 28, 11, 45, 0);
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);
		treatmentTypes.add(treatmentType2);
		Appointment appointment = new Appointment(executor, from, to, treatmentTypes, individual);
		long id = addAppointment(appointment);
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		appointment = appointmentDao.findById(id);

		assertTrue(appointment.getTreatmentTypes().get(0).getName().equals("Odstraňovanie pigmentov chrbát"));
		
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
