package ehc.bo.test;

import java.util.ArrayList;
import java.util.List;

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
/*	private List<Long> physicianIds = new ArrayList<Long>();*/
	private List<Long> nurseIds = new ArrayList<Long>();
	private List<Long> treatmentTypeIds = new ArrayList<Long>();
	private List<Long> individualIds = new ArrayList<Long>();
	
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

	private void addPhysician(String firstName, String lastName) {

		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		Individual person = new Individual(executor, firstName, lastName);
		long id = (long)HibernateUtil.save(person);
		individualIds.add(id);

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

	private void addRoom(String name) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		RoomType roomType = new RoomType(executor);

		Room room = new Room(executor, roomType, name);

		long id = (long) HibernateUtil.save(room);
		roomIds.add(id);
		
		HibernateUtil.commitTransaction();

	
	}

	private void addNurse(String firstName, String lastName) {
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
		HibernateUtil.delete(room);
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

	private void addPhysicians() {
		addPhysician("Mária", "Petrášová");
		addPhysician("Marika", "Piršelová");
	}

	private void addRooms() {
		addRoom("test room 1");
		addRoom("test room 2");
		addRoom("test room 3");
	}

	private void addNurses() {
		addNurse("Zuzana", "Vanková");

	}

	private void removePhysicians() {
		for (long id : individualIds) {
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

	private void addTreatmentType(String name, String category, double price) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		PhysicianType physicianType = new PhysicianType(executor);
		NurseType nurseType = new NurseType(executor);
		RoomType roomType = new RoomType(executor);
		TreatmentType treatmentType = new TreatmentType(executor, name, category, price);
		treatmentType.addResourceType(physicianType);
		treatmentType.addResourceType(nurseType);
		treatmentType.addResourceType(roomType);

		long id = (long)HibernateUtil.save(treatmentType);
		
		treatmentTypeIds.add(id);
		
		HibernateUtil.commitTransaction();
	}

	private void addTreatmentTypes() {
		addTreatmentType("Odstraňovanie pigmentov chrbát", "Odstránenie pigmentových škvŕn", 80);
	}

	protected void setUpSystem() {
		addPhysicians();
		addRooms();
		addTreatmentTypes();
		addNurses();
	}

	protected void tearDownSystem() {
		removePhysicians();
		removeRooms();
		removeTreatmentTypes();
		removeNurses();
	}

	public void testApp() {
		setUpSystem();
		tearDownSystem();
	}

}
