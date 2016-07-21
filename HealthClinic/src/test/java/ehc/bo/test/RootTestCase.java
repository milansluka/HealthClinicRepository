package ehc.bo.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.Company;
import ehc.bo.impl.CompanyDao;
import ehc.bo.impl.Device;
import ehc.bo.impl.DeviceDao;
import ehc.bo.impl.DeviceType;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Nurse;
import ehc.bo.impl.NurseDao;
import ehc.bo.impl.NurseType;
import ehc.bo.impl.Physician;
import ehc.bo.impl.PhysicianDao;
import ehc.bo.impl.PhysicianType;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.Room;
import ehc.bo.impl.RoomDao;
import ehc.bo.impl.RoomType;
import ehc.bo.impl.Skill;
import ehc.bo.impl.SkillDao;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import junit.framework.TestCase;

public class RootTestCase extends TestCase {
	private List<Long> roomIds = new ArrayList<Long>();
	private List<Long> deviceIds = new ArrayList<Long>();
	private List<Long> physicianIds = new ArrayList<Long>();
	private List<Long> nurseIds = new ArrayList<Long>();
	private List<Long> treatmentTypeIds = new ArrayList<Long>();
	private List<Long> individualIds = new ArrayList<Long>();
	private List<Long> appointmentIds = new ArrayList<Long>();
	private List<Long> skillIds = new ArrayList<Long>();
	
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();
	private SkillDao skillDao = SkillDao.getInstance();
	private RoomDao roomDao = RoomDao.getInstance();
	private TreatmentTypeDao treatmentDao = TreatmentTypeDao.getInstance();
	
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
	
	public void addPhysician(String firstName, String lastName, List<String> skillNames) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");	
		Individual person = new Individual(executor, firstName, lastName);
		long id = (long)HibernateUtil.save(person);
		physicianIds.add(id);	
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();	
		IndividualDao individualDao = IndividualDao.getInstance();
		CompanyDao companyDao = CompanyDao.getInstance();
		person = individualDao.findByFirstAndLastName(firstName, lastName);
		Company company = companyDao.findByName("Company name");
		PhysicianType physicianType = new PhysicianType(executor);
			
		for (String skillName : skillNames) {
			Skill skill = getSkill(skillName);
			physicianType.addSkill(skill);
		}	
		Physician physician = new Physician(executor, physicianType, person, company);
		HibernateUtil.save(physician);
		HibernateUtil.commitTransaction();
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
	
	public Skill addSkill(String name) {
		/*HibernateUtil.beginTransaction();*/
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Skill skill = new Skill(executor, name);
		long id = (long) HibernateUtil.save(skill);
		skillIds.add(id);		
	/*	HibernateUtil.commitTransaction();*/	
		return skill;
	}
	
	public void addDevice(String name, List<String> treatmentTypeNames) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		DeviceType deviceType = new DeviceType(executor);
		TreatmentType treatmentType = null;
		for (String treatmentTypeName : treatmentTypeNames) {
			treatmentType = treatmentDao.findByName(treatmentTypeName);	
			deviceType.addPossibleTreatmentType(treatmentType);
		}
		Device device = new Device(executor, deviceType, name);
		long id = (long) HibernateUtil.save(device);
		deviceIds.add(id);		
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
	
	public void addRoom(String name, List<String> treatmentTypeNames) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		RoomType roomType = new RoomType(executor);
		TreatmentType treatmentType = null;
		for (String treatmentTypeName : treatmentTypeNames) {
			treatmentType = treatmentDao.findByName(treatmentTypeName);	
			roomType.addPossibleTreatmentType(treatmentType);
		}
		Room room = new Room(executor, roomType, name);
		long id = (long) HibernateUtil.save(room);
		roomIds.add(id);		
		HibernateUtil.commitTransaction();
	}
	
	public void addRoom(String name, RoomType type) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Room room = new Room(executor, type, name);
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
	
	protected void removeSkill(long id) {
		SkillDao skillDao = SkillDao.getInstance();
		
		HibernateUtil.beginTransaction();
		Skill skill = skillDao.findById(id);
		
		if (skill != null) {
			skill.removeResourceTypes();
			HibernateUtil.delete(skill);
		}
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
	
	protected void removeDevice(long id) {
		DeviceDao deviceDao = DeviceDao.getInstance();
		HibernateUtil.beginTransaction();
		Device device = deviceDao.findById(id);
		
		if (device != null) {
			HibernateUtil.delete(device);			
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
		List<String> skillNames1 = new ArrayList<>();
		skillNames1.add("test skill1");
		skillNames1.add("test skill2");
		skillNames1.add("test skill3");
		addPhysician("Mária", "Petrášová", skillNames1);
		
		List<String> skillNames2 = new ArrayList<>();
		skillNames2.add("test skill1");
		skillNames2.add("test skill2");
		addPhysician("Marika", "Piršelová", skillNames2);
	}
	
	protected void addDevices() {
		List<String> treatmentTypeNames1 = new ArrayList<>();
		treatmentTypeNames1.add("Odstraňovanie pigmentov chrbát");
		treatmentTypeNames1.add("OxyGeneo - tvár");
		
		List<String> treatmentTypeNames2 = new ArrayList<>();
		treatmentTypeNames2.add("Odstraňovanie pigmentov chrbát");
		
		List<String> treatmentTypeNames3 = new ArrayList<>();
		treatmentTypeNames3.add("OxyGeneo - tvár");
		
		addDevice("test device 1", treatmentTypeNames1);
		addDevice("test device 2", treatmentTypeNames2);
		addDevice("test device 3", treatmentTypeNames3);
	}

	protected void addRooms() {
		List<String> treatmentTypeNames1 = new ArrayList<>();
		treatmentTypeNames1.add("Odstraňovanie pigmentov chrbát");
		treatmentTypeNames1.add("OxyGeneo - tvár");
		
		List<String> treatmentTypeNames2 = new ArrayList<>();
		treatmentTypeNames2.add("Odstraňovanie pigmentov chrbát");
		
		List<String> treatmentTypeNames3 = new ArrayList<>();
		treatmentTypeNames3.add("OxyGeneo - tvár");
		
		addRoom("test room 1", treatmentTypeNames1);
		addRoom("test room 2", treatmentTypeNames2);
		addRoom("test room 3", treatmentTypeNames3);
	}
	
	protected void addSKills() {
		addSkill("test skill1");
		addSkill("test skill2");
		addSkill("test skill3");
	}

	protected void addNurses() {
		addNurse("Zuzana", "Vanková");

	}
	
	protected void removeSkills() {
		for (long id : skillIds) {
			removeSkill(id);
		}	
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
	/*	appointment.removeResources();*/
		appointment.prepareForDeleting();
	/*	appointment.removeIndividual();*/
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
	
	private void removeDevices() {
		for (long id : deviceIds) {
			removeDevice(id);
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
	
	protected Skill getSkill(String skillName) {
	/*	HibernateUtil.beginTransaction();*/
		Skill skill = skillDao.findByName(skillName);
		
		if (skill == null) {
			skill = addSkill(skillName);
		}
		
		Hibernate.initialize(skill.getResourceTypes());	
	/*	HibernateUtil.commitTransaction();*/
		
		return skill;
	}

	protected void addTreatmentTypes() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		
		List<ResourceType> resourceTypes1 = new ArrayList<>();
		PhysicianType physicianType1 = new PhysicianType(executor);
	    NurseType nurseType1 = new NurseType(executor);
		RoomType roomType1 = new RoomType(executor);
		DeviceType deviceType1 = new DeviceType(executor);
		
		physicianType1.addSkill(getSkill("test skill3"));
		
		resourceTypes1.add(physicianType1);
		resourceTypes1.add(nurseType1);
		resourceTypes1.add(roomType1);
		resourceTypes1.add(deviceType1);
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		login = new Login();
		executor = login.login("admin", "admin");
		
		List<ResourceType> resourceTypes2 = new ArrayList<>();
		PhysicianType physicianType2 = new PhysicianType(executor);
		NurseType nurseType2 = new NurseType(executor);
		RoomType roomType2 = new RoomType(executor);
		DeviceType deviceType2 = new DeviceType(executor);
		
		physicianType2.addSkill(getSkill("test skill1"));
		physicianType2.addSkill(getSkill("test skill2"));
		
		resourceTypes2.add(physicianType2);
		resourceTypes2.add(nurseType2);
		resourceTypes2.add(roomType2);
		resourceTypes2.add(deviceType2);
		HibernateUtil.commitTransaction();
		
		addTreatmentType("Odstraňovanie pigmentov chrbát", "Odstránenie pigmentových škvŕn", resourceTypes1, 60*60);
		addTreatmentType("OxyGeneo - tvár", "OxyGeneo", resourceTypes2, 60*60);
	}

	protected void setUpSystem() {
		addTreatmentTypes();
		addPhysicians();
		addRooms();	
		addDevices();
		addNurses();
		addIndividuals();
	}

	protected void tearDownSystem() {
		removeAppointments();
		removePhysicians();
		removeRooms();
		removeDevices();
		removeNurses();
		removeIndividuals();
		removeTreatmentTypes();
		removeSkills();
	}

	public void testApp() {
		setUpSystem();
		tearDownSystem();
	}
}
