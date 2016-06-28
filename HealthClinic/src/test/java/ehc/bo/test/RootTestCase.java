package ehc.bo.test;

import ehc.bo.impl.Company;
import ehc.bo.impl.CompanyDao;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Physician;
import ehc.bo.impl.PhysicianType;
import ehc.bo.impl.Room;
import ehc.bo.impl.RoomDao;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import junit.framework.TestCase;

public class RootTestCase extends TestCase {
	private void addPhysician(String firstName, String lastName) {

		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		Individual person = new Individual(executor, firstName, lastName);
		HibernateUtil.save(person);

		IndividualDao individualDao = IndividualDao.getInstance();
		CompanyDao companyDao = CompanyDao.getInstance();

		person = individualDao.findByFirstAndLastName(firstName, lastName);
		Company company = companyDao.findByName("Company name");

		PhysicianType physicianType = new PhysicianType(executor);

		Physician physician = new Physician(executor, physicianType, person, company);
		HibernateUtil.save(physician);

		HibernateUtil.commitTransaction();
	}

	private void addRoom(String name) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		Room room = new Room(executor, name);

		HibernateUtil.save(room);
		HibernateUtil.commitTransaction();
	}

	private void removeRoom(String name) {
		RoomDao roomDao = RoomDao.getInstance();
		HibernateUtil.beginTransaction();
		Room room = roomDao.findByName(name);
		HibernateUtil.delete(room);
		HibernateUtil.commitTransaction();
	}

	private void removePhysician(String firstName, String lastName) {
		IndividualDao individualDao = IndividualDao.getInstance();

		HibernateUtil.beginTransaction();
		Individual individual = individualDao.findByFirstAndLastName(firstName, lastName);
		HibernateUtil.delete(individual);
		HibernateUtil.commitTransaction();
	}

	private void addPhysicians() {
		addPhysician("Mária", "Petrášová");
		addPhysician("Marika", "Piršelová");
	}

	private void addRooms() {
		addRoom("1");
		addRoom("2");
		addRoom("3");
	}

	private void removePhysicians() {
		removePhysician("Mária", "Petrášová");
		removePhysician("Marika", "Piršelová");
	}

	private void removeRooms() {
		removeRoom("1");
		removeRoom("2");
		removeRoom("3");
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
		
		return room1 != null && room2 != null && room3 != null 
				&& individual1 != null && individual2 != null && treatmentType != null;
		
	}

	private void addTreatmentType(String name, String category, double price) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		PhysicianType physicianType = new PhysicianType(executor);
		TreatmentType treatmentType = new TreatmentType(executor, name, category, price, physicianType);

		HibernateUtil.save(treatmentType);
		HibernateUtil.commitTransaction();
	}

	private void addTreatmentTypes() {
		addTreatmentType("Odstraňovanie pigmentov chrbát", "Odstránenie pigmentových škvŕn", 80);
	}

	protected void setUpSystem() {
		addPhysicians();
		addRooms();
		addTreatmentTypes();
	}

	protected void tearDownSystem() {
		removePhysicians();
        removeRooms();
	}

	public void testApp() {
		setUpSystem();
		tearDownSystem();
	}

}
