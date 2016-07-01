package ehc.bo.test;

import java.util.Date;

import ehc.bo.impl.Appointment;
import ehc.bo.impl.Individual;
import ehc.bo.impl.Login;
import ehc.bo.impl.Nurse;
import ehc.bo.impl.Physician;
import ehc.bo.impl.PhysicianType;
import ehc.bo.impl.ResourcesUtil;
import ehc.bo.impl.Room;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import ehc.util.Util;
import junit.framework.TestCase;

public class CreateAppointmentForNewPerson extends RootTestCase {
	private String treatmentName = "Odstraňovanie pigmentov chrbát";
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();

	protected void setUp() throws Exception {
		super.setUp();
		
		if (isSystemSet()) {
			setUpSystem();		
		}

/*		HibernateUtil.beginTransaction();
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentName);
		HibernateUtil.commitTransaction();*/

/*		if (treatmentType == null) {
			HibernateUtil.beginTransaction();
			Login login = new Login();
			User executor = login.login("admin", "admin");
			PhysicianType physicianType = new PhysicianType(executor);
			treatmentType = new TreatmentType(executor, treatmentName, "some treatment type", 80, physicianType);
			HibernateUtil.save(treatmentType);
			HibernateUtil.commitTransaction();
		}*/

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
		
		ResourcesUtil allocator = new ResourcesUtil();
		Physician physician = allocator.findPhysician(from, to, treatmentType);
		Nurse nurse = allocator.findNurse();
		Room room = allocator.findRoom();
		
		Appointment appointment = new Appointment(executor, from, to, treatmentType, physician, nurse, room, person);
		
		HibernateUtil.save(appointment);
		
		HibernateUtil.commitTransaction();
		
	
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
