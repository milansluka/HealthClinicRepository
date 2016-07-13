package ehc.bo.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.Company;
import ehc.bo.impl.CompanyDao;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Nurse;
import ehc.bo.impl.NurseDao;
import ehc.bo.impl.NurseType;
import ehc.bo.impl.Physician;
import ehc.bo.impl.PhysicianDao;
import ehc.bo.impl.PhysicianType;
import ehc.bo.impl.ResourceImpl;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.Room;
import ehc.bo.impl.RoomDao;
import ehc.bo.impl.RoomType;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import junit.framework.TestCase;

public class RootTestCase extends TestCase {
	private List<Long> roomIds = new ArrayList<Long>();
	private List<Long> physicianIds = new ArrayList<Long>();
	private List<Long> nurseIds = new ArrayList<Long>();
	private List<Long> treatmentTypeIds = new ArrayList<Long>();
	private List<Long> individualIds = new ArrayList<Long>();
	private List<Long> appointmentIds = new ArrayList<Long>();
	
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();
	
	protected long addIndividual(String firstName, String lastName) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		Individual person = new Individual(executor, firstName, lastName);
		long id = (long)HibernateUtil.save(person);
		HibernateUtil.commitTransaction();
		
		return id;
	}
	
	public int getCountOfResources() {
		RoomDao roomDao = RoomDao.getInstance();
		PhysicianDao physicianDao = PhysicianDao.getInstance();
		NurseDao nurseDao = NurseDao.getInstance();
		List<Room> rooms = roomDao.getAll();
		List<Physician> physicians = physicianDao.getAll();
		List<Nurse> nurses = nurseDao.getAll();
		
		return rooms.size() + nurses.size() + physicians.size();
	}

	public void addPhysician(String firstName, String lastName) {

		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		Individual person = new Individual(executor, firstName, lastName);
		long id = (long)HibernateUtil.save(person);
		physicianIds.add(id);

		IndividualDao individualDao = IndividualDao.getInstance();
		CompanyDao companyDao = CompanyDao.getInstance();

		person = individualDao.findByFirstAndLastName(firstName, lastName);
		Company company = companyDao.findByName("Company name");

		/* ResourceType resourceType = new ResourceType(executor); */

		PhysicianType physicianType = new PhysicianType(executor);
		/* HibernateUtil.save(resourceType); */

		Physician physician = new Physician(executor, physicianType, person, company);
		HibernateUtil.save(physician);

	/*	physicianIds.add(id);*/

		HibernateUtil.commitTransaction();
	}

	public void addRoom(String name) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		RoomType roomType = new RoomType(executor);

		Room room = new Room(executor, roomType, name);

		long id = (long) HibernateUtil.save(room);
		roomIds.add(id);
		
		HibernateUtil.commitTransaction();
	}
	
	public long addAppointment(Appointment appointment) {
		HibernateUtil.getCurrentSessionWithTransaction();
		/*HibernateUtil.beginTransaction();*/
		long id = (long) HibernateUtil.save(appointment);
		appointmentIds.add(id);	
		/*HibernateUtil.commitTransaction();*/
		return id;
	}

	public void addNurse(String firstName, String lastName) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		Individual person = new Individual(executor, firstName, lastName);
		long id = (long) HibernateUtil.save(person);
		nurseIds.add(id);
		
		IndividualDao individualDao = IndividualDao.getInstance();
		CompanyDao companyDao = CompanyDao.getInstance();

		person = individualDao.findByFirstAndLastName(firstName, lastName);
		Company company = companyDao.findByName("Company name");

		/* ResourceType resourceType = new ResourceType(executor); */

		NurseType nurseType = new NurseType(executor);
		/* HibernateUtil.save(resourceType); */

		Nurse nurse = new Nurse(executor, nurseType, person, company);
		HibernateUtil.save(nurse);

		HibernateUtil.commitTransaction();

	}

	protected void removeRoom(long id) {
		RoomDao roomDao = RoomDao.getInstance();
		HibernateUtil.beginTransaction();
		Room room = roomDao.findById(id);
		
		if (room != null) {
			HibernateUtil.delete(room);			
		}
		HibernateUtil.commitTransaction();
	}
	
	protected void removeIndividual(long id) {
		IndividualDao individualDao = IndividualDao.getInstance();

		HibernateUtil.beginTransaction();
		Individual individual = individualDao.findById(id);

		HibernateUtil.delete(individual);

		HibernateUtil.commitTransaction();
		
	}

	private void removePhysician(long id) {
		IndividualDao individualDao = IndividualDao.getInstance();

		HibernateUtil.beginTransaction();
		Individual individual = individualDao.findById(id);

		HibernateUtil.delete(individual);

		HibernateUtil.commitTransaction();
	}

	private void removeNurse(long id) {
		IndividualDao individualDao = IndividualDao.getInstance();

		HibernateUtil.beginTransaction();
		Individual individual = individualDao.findById(id);
		HibernateUtil.delete(individual);
		HibernateUtil.commitTransaction();
	}

	private void removeTreatmentType(long id) {
		TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();

		HibernateUtil.beginTransaction();
		TreatmentType treatmentType = treatmentTypeDao.findById(id);
		HibernateUtil.delete(treatmentType);
		HibernateUtil.commitTransaction();
	}
	
	protected void addIndividuals() {
		addIndividual("Janko", "Mrkvicka");
	}

	protected void addPhysicians() {
		addPhysician("Mária", "Petrášová");
		addPhysician("Marika", "Piršelová");
	}

	protected void addRooms() {
		addRoom("test room 1");
		addRoom("test room 2");
		addRoom("test room 3");
	}

	protected void addNurses() {
		addNurse("Zuzana", "Vanková");

	}
	
	protected void removeAppointments() {
		for (long id : appointmentIds) {
			removeAppointment(id);
		}	
	}
	
	protected void removeAppointment(long id) {
		HibernateUtil.beginTransaction();
		Appointment appointment = appointmentDao.findById(id);
		/*appointment.setResources(new ArrayList<>());	*/
		Session session = HibernateUtil.getCurrentSessionWithTransaction();	
		appointment.removeResources();
		HibernateUtil.saveOrUpdate(appointment);
/*		for (Resource resource : appointment.getResources()) {
			appointment.removeResources();
		}*/	
		HibernateUtil.delete(appointment);
		HibernateUtil.commitTransaction();	
	}

	private void removePhysicians() {
		for (long id : physicianIds) {
			removePhysician(id);
		} 
	}

	private void removeRooms() {
		for (long id : roomIds) {
			removeRoom(id);
		}
	}

	private void removeNurses() {
		for (long id : nurseIds) {
			removeNurse(id);
		} 
	}

	private void removeTreatmentTypes() {
		for (long id : treatmentTypeIds) {
			removeTreatmentType(id);
		} 
	}
	
	private void removeIndividuals() {
		for (long id : individualIds) {
			removeIndividual(id);
		} 
	}
	
	

	protected boolean isSystemSet() {
		RoomDao roomDao = RoomDao.getInstance();

		HibernateUtil.beginTransaction();

		Room room1 = roomDao.findByName("1");
		Room room2 = roomDao.findByName("2");
		Room room3 = roomDao.findByName("3");

		IndividualDao individualDao = IndividualDao.getInstance();
		Individual individual1 = individualDao.findByFirstAndLastName("Mária", "Petrášová");
		Individual individual2 = individualDao.findByFirstAndLastName("Marika", "Piršelová");

		TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
		TreatmentType treatmentType = treatmentTypeDao.findByName("Odstraňovanie pigmentov chrbát");

		HibernateUtil.commitTransaction();

		return room1 != null && room2 != null && room3 != null && individual1 != null && individual2 != null
				&& treatmentType != null;
	}
	
	protected void addTreatmentType(String name, String category, List<ResourceType> resourceTypes, int duration) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		TreatmentType treatmentType = new TreatmentType(executor, name, category, 50, duration);
		
		for (ResourceType resourceType : resourceTypes) {
			treatmentType.addResourceType(resourceType);		
		}

		long id = (long)HibernateUtil.save(treatmentType);
		
		treatmentTypeIds.add(id);
		
		HibernateUtil.commitTransaction();	
	}

	protected void addTreatmentType(String name, String category, double price, int duration) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		PhysicianType physicianType = new PhysicianType(executor);
		NurseType nurseType = new NurseType(executor);
		RoomType roomType = new RoomType(executor);
		TreatmentType treatmentType = new TreatmentType(executor, name, category, price, duration);
		treatmentType.addResourceType(physicianType);
		treatmentType.addResourceType(nurseType);
		treatmentType.addResourceType(roomType);

		long id = (long)HibernateUtil.save(treatmentType);
		
		treatmentTypeIds.add(id);
		
		HibernateUtil.commitTransaction();
	}
	
	

	protected void addTreatmentTypes() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		
		addTreatmentType("Odstraňovanie pigmentov chrbát", "Odstránenie pigmentových škvŕn", 80, 60*60);
		
		List<ResourceType> resourceTypes = new ArrayList<>();
		resourceTypes.add(new PhysicianType(executor));
		resourceTypes.add(new NurseType(executor));
		resourceTypes.add(new RoomType(executor));
		
		addTreatmentType("OxyGeneo - tvár", "OxyGeneo", resourceTypes, 60*60);
	}

	protected void setUpSystem() {
		addPhysicians();
		addRooms();
		addTreatmentTypes();
		addNurses();
		addIndividuals();
	}

	protected void tearDownSystem() {
		removeAppointments();
		removePhysicians();
		removeRooms();
		removeNurses();
		removeIndividuals();
		removeTreatmentTypes();
	}

	public void testApp() {
		setUpSystem();
		tearDownSystem();
	}
}
