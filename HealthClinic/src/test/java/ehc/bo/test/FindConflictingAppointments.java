package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.AppointmentScheduleData;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Physician;
import ehc.bo.impl.PhysicianDao;
import ehc.bo.impl.Room;
import ehc.bo.impl.RoomDao;
import ehc.bo.impl.SchedulingUtil;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import junit.framework.TestCase;

public class FindConflictingAppointments extends RootTestCase {
	private IndividualDao individualDao = IndividualDao.getInstance();
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();

	
	private PhysicianDao physicianDao = PhysicianDao.getInstance();
	private RoomDao roomDao = RoomDao.getInstance();

	protected void setUp() throws Exception {
		super.setUp();
		
		if (!isSystemSet()) {
			setUpSystem3();		
		}
			
		HibernateUtil.beginTransaction();
		
		String personFirstName = "Jan";
		String personLastName = "Novak";
		
		Individual existingPerson = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		HibernateUtil.commitTransaction();
		if (existingPerson == null) {
			Login login = new Login();
			
			HibernateUtil.beginTransaction();
			User executor = login.login("admin", "admin");
			Individual person = new Individual(executor, personFirstName, personLastName);
			HibernateUtil.save(person);		
			HibernateUtil.commitTransaction();		
		}	
		addAppointment("Jan", "Novak", "Odstranovanie pigmentov chrbat", DateUtil.date(2017, 3, 20, 7, 0, 0), DateUtil.date(2017, 3, 20, 8, 0, 0));
		addAppointment("Karol", "Kubanda", "OxyGeneo tvar", DateUtil.date(2017, 3, 20, 7, 0, 0), DateUtil.date(2017, 3, 20, 7, 15, 0));
		
	}
	
	public void testApp() {		
		Login login = new Login();

		HibernateUtil.beginTransaction();
		String personFirstName = "Adam";
		String personLastName = "Mrkvicka";
		User executor = login.login("admin", "admin");
		Individual person = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		String treatmentName = "Omladenie tvare";
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentName);
		Date from = DateUtil.date(2017, 3, 20, 7, 0, 0);
		Date to = DateUtil.date(2017, 3, 20, 10, 30, 0);
		List<TreatmentType> treatmentTypes = new ArrayList<TreatmentType>();
		treatmentTypes.add(treatmentType);
		SchedulingUtil util = new SchedulingUtil();
		Set<Appointment> conflicts = util.getConflictingAppointments(from, to, treatmentTypes);
		
		assertEquals(2, conflicts.size());
	    List<Appointment> foundConflicts = new ArrayList<Appointment>(conflicts);
		assertEquals("Odstranovanie pigmentov chrbat", foundConflicts.get(0).getPlannedTreatmentTypes().get(0).getName());
		assertEquals("OxyGeneo tvar", foundConflicts.get(1).getPlannedTreatmentTypes().get(0).getName());
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}
}
