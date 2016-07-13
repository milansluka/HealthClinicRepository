package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Nurse;
import ehc.bo.impl.NurseDao;
import ehc.bo.impl.Physician;
import ehc.bo.impl.PhysicianDao;
import ehc.bo.impl.ResourceImpl;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.ResourcesUtil;
import ehc.bo.impl.Room;
import ehc.bo.impl.RoomDao;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import junit.framework.TestCase;

public class ResourceIsNotAvailable extends RootTestCase {
	private String personFirstName = "Jan";
	private String personLastName = "Novak";
	private String treatmentName = "Odstraňovanie pigmentov chrbát";
	private Date when = DateUtil.date(2016, 7, 7, 7, 30, 0);
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();
	private PhysicianDao physicianDao = PhysicianDao.getInstance();
	private NurseDao nurseDao = NurseDao.getInstance();
	private RoomDao roomDao = RoomDao.getInstance();
	private List<Long> appointmentIds = new ArrayList<>();

	protected void setUp() throws Exception {
		super.setUp();
		
		if (isSystemSet()) {
			tearDownSystem();
		}
		
		addPhysicians();
		addNurses();
		addTreatmentTypes();
		addIndividuals();
		addRoom("test room 1");
		
		HibernateUtil.beginTransaction();
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
	
		String treatmentName = "OxyGeneo - tvár";
		String personFirstName = "Janko";
		String personLastName = "Mrkvicka";
		
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Individual individual = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentName);	
		
		
		//appointment from 7:00 to 8:00
		Appointment appointment = new Appointment(executor, DateUtil.date(2016, 7, 7, 7, 0, 0), DateUtil.date(2016, 7, 7, 8, 0, 0), treatmentType, individual);
		
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();		
		List<Physician> physicians = physicianDao.getAll();
		List<Room> rooms = roomDao.getAll();
		List<Nurse> nurses = nurseDao.getAll();
		
		Physician physician = physicians.get(0);
		Room room = rooms.get(0);
		Nurse nurse = nurses.get(0);
		
		appointment.addResource(physician);
		appointment.addResource(room);
		appointment.addResource(nurse);
		
		long appointmentId = (long)HibernateUtil.save(appointment);
	    appointmentIds.add(appointmentId);
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		for (long id : appointmentIds) {
			removeAppointment(id);
		} 	
		tearDownSystem();
	}
	
	public void testApp() {	
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Individual individual = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentName);
		
		
	/*	Appointment appointment = new Appointment(executor, when, DateUtil.addSeconds(when, 60*60), treatmentType, individual);*/
			
		Date from1 = DateUtil.date(2016, 7, 7, 7, 0, 0);
		Date to1 = DateUtil.date(2016, 7, 7, 8, 0, 0);
		Date from2 = DateUtil.date(2016, 7, 7, 7, 30, 0);
		Date to2 = DateUtil.date(2016, 7, 7, 8, 30, 0);
		
		Room room = roomDao.findByName("test room 1");	
		ResourceImpl resource = new ResourceImpl(executor);
		boolean roomIsNotAvailable = !room.isAvailable(from2, to2);	
		HibernateUtil.commitTransaction();
			
		assertTrue(roomIsNotAvailable);			
	}

}
