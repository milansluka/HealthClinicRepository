package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.AppointmentScheduleData;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.AppointmentStateValue;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Physician;
import ehc.bo.impl.PhysicianDao;
import ehc.bo.impl.Room;
import ehc.bo.impl.RoomDao;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class CancelAppointment extends RootTestCase {
	private IndividualDao individualDao = IndividualDao.getInstance();
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private String personFirstName = "Jan";
	private String personLastName = "Novak";
	private String treatmentName = "Odstraňovanie pigmentov chrbát";
	private PhysicianDao physicianDao = PhysicianDao.getInstance();
	private RoomDao roomDao = RoomDao.getInstance();
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();

	protected void setUp() throws Exception {
		super.setUp();
		
		if (!isSystemSet()) {
			setUpSystem();		
		}
			
		HibernateUtil.beginTransaction();
		Individual existingPerson = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		HibernateUtil.commitTransaction();
		if (existingPerson == null) {
			addIndividual(personFirstName, personLastName);
			
		}	
		Login login = new Login();

		HibernateUtil.beginTransaction();	
		User executor = login.login("admin", "admin");
		Individual person = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentName);
		Date from = DateUtil.date(2016, 4, 20, 10, 0, 0);
		Date to = DateUtil.date(2016, 4, 20, 10, 30, 0);
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);
		
		
		List<Physician> physicians = physicianDao.getAll();
		List<Room> rooms = roomDao.getAll();
		
		Physician physician = physicians.get(0);
		Room room = rooms.get(0);	
		List<Resource> resources = new ArrayList<>();
		resources.add(physician);
		resources.add(room);
		AppointmentScheduler appointmentScheduler = new AppointmentScheduler(getWorkTime(), HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);
		AppointmentScheduleData appointmentScheduleData = new AppointmentScheduleData(from, to, resources);
		Appointment appointment = appointmentScheduler.getAppointment(executor, appointmentScheduleData, treatmentTypes, person);
	/*	Appointment appointment = new Appointment(executor, from, to, person);*/
		addAppointment(appointment);
		HibernateUtil.commitTransaction();	
	}
	
	public void testApp() {		
		Login login = new Login();
		
		HibernateUtil.beginTransaction();
		User executor = login.login("admin", "admin");
		Individual person = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		List<Appointment> appointments = person.getAppointments();
		Appointment appointment = appointments.get(0);
		long id = appointment.getId();
		appointment.setState(executor, AppointmentStateValue.CANCELLED);
        HibernateUtil.saveOrUpdate(appointment);
        HibernateUtil.commitTransaction();
        
        HibernateUtil.beginTransaction();
        Appointment cancelledAppointment = appointmentDao.findById(id);
        assertTrue(cancelledAppointment.getState().getValue() == AppointmentStateValue.CANCELLED);

        HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
