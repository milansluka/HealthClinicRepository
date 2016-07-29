package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import ehc.bo.Resource;
import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.Device;
import ehc.bo.impl.DeviceType;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Nurse;
import ehc.bo.impl.NurseType;
import ehc.bo.impl.Physician;
import ehc.bo.impl.PhysicianType;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.ResourcesUtil;
import ehc.bo.impl.Room;
import ehc.bo.impl.RoomType;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class ReserveResourcesForTreatment extends RootTestCase {
	private IndividualDao individualDao = IndividualDao.getInstance();

	protected void setUp() throws Exception {
		super.setUp();

		if (!isSystemSet()) {
			setUpSystem();
		}

		HibernateUtil.beginTransaction();

		Login login = new Login();
		User executor = login.login("admin", "admin");

		String treatmentName = "Zväčšenie pier";
		List<ResourceType> resourceTypes = new ArrayList<>();
		PhysicianType physicianType = new PhysicianType(executor);
		physicianType.addSkill(getSkill("some skill 1"));
		physicianType.addSkill(getSkill("some skill 2"));
		resourceTypes.add(physicianType);

		NurseType nurseType = new NurseType(executor);
		nurseType.addSkill(getSkill("nurse skill 1"));
		nurseType.addSkill(getSkill("nurse skill 2"));
		nurseType.addSkill(getSkill("nurse skill 3"));
		resourceTypes.add(nurseType);

		nurseType = new NurseType(executor);
		nurseType.addSkill(getSkill("nurse skill 4"));
		/*
		 * nurseType.addSkill(getSkill("nurse skill 1"));
		 * nurseType.addSkill(getSkill("nurse skill 2"));
		 * nurseType.addSkill(getSkill("nurse skill 3"));
		 */

		resourceTypes.add(nurseType);
		RoomType roomType = new RoomType(executor);
		resourceTypes.add(roomType);
		DeviceType deviceType = new DeviceType(executor);
		resourceTypes.add(deviceType);
		addTreatmentType(treatmentName, "empty", resourceTypes, 60);
		HibernateUtil.commitTransaction();

		/* addIndividual("Milan", "Sluka"); */
		PhysicianType physicianType2 = new PhysicianType(executor);
		HibernateUtil.beginTransaction();
		physicianType2.addSkill(getSkill("some skill 1"));
		physicianType2.addSkill(getSkill("some skill 2"));
		HibernateUtil.commitTransaction();
		addPhysician(physicianType2, "Milan", "Sluka");
		
	/*	add nurse role to physician Milan Sluka*/
		NurseType type = new NurseType(executor);
		HibernateUtil.beginTransaction();
		type.addSkill(getSkill("nurse skill 1"));
		type.addSkill(getSkill("nurse skill 4"));
		HibernateUtil.commitTransaction();
		addNurseRole(type, "Milan", "Sluka");

		/* addIndividual("Martin", "Sluka"); */
		PhysicianType physicianType3 = new PhysicianType(executor);
		HibernateUtil.beginTransaction();
		physicianType3.addSkill(getSkill("some skill 1"));
		HibernateUtil.commitTransaction();
		addPhysician(physicianType3, "Martin", "Sluka");

		/* addIndividual("Jan", "Sluka"); */
		PhysicianType physicianType4 = new PhysicianType(executor);
		HibernateUtil.beginTransaction();
		physicianType4.addSkill(getSkill("some skill 1"));
		physicianType4.addSkill(getSkill("some skill 2"));
		HibernateUtil.commitTransaction();
		addPhysician(physicianType4, "Jan", "Sluka");

		/* addIndividual("Adam", "Novak"); */
		NurseType nurseType2 = new NurseType(executor);
		HibernateUtil.beginTransaction();
		nurseType2.addSkill(getSkill("nurse skill 1"));
		nurseType2.addSkill(getSkill("nurse skill 3"));
		HibernateUtil.commitTransaction();
		addNurse(nurseType2, "Adam", "Novak");

		/* addIndividual("Zuzana", "Novakova"); */
		NurseType nurseType3 = new NurseType(executor);
		HibernateUtil.beginTransaction();
		nurseType3.addSkill(getSkill("nurse skill 1"));
		nurseType3.addSkill(getSkill("nurse skill 2"));
		nurseType3.addSkill(getSkill("nurse skill 3"));
		HibernateUtil.commitTransaction();
		addNurse(nurseType3, "Zuzana", "Novakova");

		/* addIndividual("Jana", "Mrkvickova"); */
		NurseType nurseType4 = new NurseType(executor);
		HibernateUtil.beginTransaction();
		nurseType4.addSkill(getSkill("nurse skill 1"));
		nurseType4.addSkill(getSkill("nurse skill 2"));
		nurseType4.addSkill(getSkill("nurse skill 3"));
		nurseType4.addSkill(getSkill("nurse skill 4"));
		HibernateUtil.commitTransaction();
		addNurse(nurseType4, "Jana", "Mrkvickova");
		
		/* addIndividual("Martina", "Slukova"); */
		NurseType nurseType5 = new NurseType(executor);
		HibernateUtil.beginTransaction();
		nurseType5.addSkill(getSkill("nurse skill 1"));
		nurseType5.addSkill(getSkill("nurse skill 2"));
		nurseType5.addSkill(getSkill("nurse skill 3"));
		HibernateUtil.commitTransaction();
		addNurse(nurseType5, "Martina", "Slukova");

		List<String> treatmentTypeNames = new ArrayList<>();
		treatmentTypeNames.add("Zväčšenie pier");
		treatmentTypeNames.add("Odstraňovanie pigmentov chrbát");
		addDevice("some device 1", treatmentTypeNames);

		treatmentTypeNames = new ArrayList<>();
		treatmentTypeNames.add("OxyGeneo - tvár");
		treatmentTypeNames.add("Zväčšenie pier");
		addRoom("some room 1", treatmentTypeNames);

		treatmentTypeNames = new ArrayList<>();
		treatmentTypeNames.add("OxyGeneo - tvár");
		treatmentTypeNames.add("Odstraňovanie pigmentov chrbát");
		treatmentTypeNames.add("Zväčšenie pier");
		addRoom("some room 2", treatmentTypeNames);

	}

	public void testApp() {
		Login login = new Login();

		HibernateUtil.beginTransaction();
		User executor = login.login("admin", "admin");

		TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
		TreatmentType treatmentType = treatmentTypeDao.findByName("Zväčšenie pier");
		ResourcesUtil resourcesUtil = new ResourcesUtil();
		Date when = DateUtil.date(2016, 7, 24, 11, 30, 0);
		Date to = DateUtil.date(2016, 7, 24, 12, 45, 0);
		List<AppointmentProposal> appointmentProposals = resourcesUtil.getAppointmentProposals(when, to, treatmentType,
				1);

		Physician physician = null;
		Physician physician2 = null;
		Nurse nurse = null;
		Nurse nurse2 = null;
		Device device = null;
		Room room = null;
		Room room2 = null;
		List<Nurse> nurses = new ArrayList<>();

		AppointmentProposal appointmentProposal = appointmentProposals.get(0);
		for (Map.Entry<ResourceType, SortedSet<Resource>> entry : appointmentProposal.getResources().entrySet()) {
			if (entry.getKey() instanceof PhysicianType) {
				/* countOfPhysicians = entry.getValue().size(); */
				physician = (Physician) entry.getValue().last();
				physician2 = (Physician) entry.getValue().first();
			}
			if (entry.getKey() instanceof NurseType) {
				/* countOfPhysicians = entry.getValue().size(); */
				nurse = (Nurse) entry.getValue().first();
				nurse2 = (Nurse) entry.getValue().last();
				nurses.add(nurse);
			}
			if (entry.getKey() instanceof RoomType) {
				/* countOfRooms = entry.getValue().size(); */
				room = (Room) entry.getValue().first();
				room2 = (Room) entry.getValue().last();
			}
			if (entry.getKey() instanceof DeviceType) {
				/* countOfDevices = entry.getValue().size(); */
				device = (Device) entry.getValue().first();
			}
		}

		Individual physicianPerson = (Individual) physician.getSource();
		Individual physicianPerson2 = (Individual) physician2.getSource();
		List<Individual> nursePersons = new ArrayList<>();
		Individual nursePerson2 = (Individual)nurse2.getSource();

		for (Nurse n : nurses) {
			Individual nursePerson = (Individual) n.getSource();
			nursePersons.add(nursePerson);
		}
		
		Individual person1 = individualDao.findByFirstAndLastName("Milan", "Sluka");
		Individual person2 = individualDao.findByFirstAndLastName("Zuzana", "Novakova");
		
		int rolesCount = physicianPerson.getReservableSourceRoles().size();

		assertTrue(physicianPerson.getFirstName().equals("Milan") && physicianPerson.getName().equals("Sluka")
				&& physicianPerson2.getFirstName().equals("Jan") && physicianPerson2.getName().equals("Sluka")
				&& room2.getName().equals("some room 2") && room.getName().equals("some room 1")
				&& device.getName().equals("some device 1") &&
				nursePerson2.getFirstName().equals("Jana") && nursePerson2.getName().equals("Mrkvickova") &&
				rolesCount == 2 && nursePersons.contains(person1) && nursePersons.contains(person2));

		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}
}
