package ehc.bo.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.hibernate.Hibernate;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.AppointmentScheduleData;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.Company;
import ehc.bo.impl.CompanyDao;
import ehc.bo.impl.CreditCard;
import ehc.bo.impl.Day;
import ehc.bo.impl.Device;
import ehc.bo.impl.DeviceDao;
import ehc.bo.impl.DeviceType;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Money;
import ehc.bo.impl.Nurse;
import ehc.bo.impl.NurseDao;
import ehc.bo.impl.NurseType;
import ehc.bo.impl.PatientBill;
import ehc.bo.impl.PatientBillItem;
import ehc.bo.impl.PatientReceipt;
import ehc.bo.impl.PatientReceiptItem;
import ehc.bo.impl.Payment;
import ehc.bo.impl.PaymentChannel;
import ehc.bo.impl.Physician;
import ehc.bo.impl.PhysicianDao;
import ehc.bo.impl.PhysicianType;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.Room;
import ehc.bo.impl.RoomDao;
import ehc.bo.impl.RoomType;
import ehc.bo.impl.Skill;
import ehc.bo.impl.SkillDao;
import ehc.bo.impl.Treatment;
import ehc.bo.impl.TreatmentGroup;
import ehc.bo.impl.TreatmentGroupDao;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.bo.impl.WaitingIndividual;
import ehc.bo.impl.WaitingIndividualDao;
import ehc.bo.impl.WorkTime;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import junit.framework.TestCase;

public class RootTestCase extends TestCase {
	private List<Long> roomIds = new ArrayList<Long>();
	private List<Long> deviceIds = new ArrayList<Long>();
	private List<Long> physicianIds = new ArrayList<Long>();
	private List<Long> nurseIds = new ArrayList<Long>();
	private List<Long> treatmentTypeIds = new ArrayList<Long>();
	private List<Long> treatmentGroupIds = new ArrayList<Long>();
	private List<Long> individualIds = new ArrayList<Long>();
	private List<Long> waitingIndividualIds = new ArrayList<Long>();
	private List<Long> appointmentIds = new ArrayList<Long>();
	private List<Long> skillIds = new ArrayList<Long>();
	private List<Long> workTimeIds = new ArrayList<Long>();

	private AppointmentDao appointmentDao = AppointmentDao.getInstance();
	private SkillDao skillDao = SkillDao.getInstance();
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();
	private CompanyDao companyDao = CompanyDao.getInstance();

	protected long addIndividual(String firstName, String lastName) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		Individual person = new Individual(executor, firstName, lastName);
		long id = (long) HibernateUtil.save(person);
		HibernateUtil.commitTransaction();

		individualIds.add(id);

		return id;
	}

	protected void addCreditCardToIndividual(User executor, Individual individual) {
		HibernateUtil.beginTransaction();
		PaymentChannel creditCard = new CreditCard(executor, individual, "000", DateUtil.date(2020, 8, 1));
		HibernateUtil.save(creditCard);
		HibernateUtil.commitTransaction();
	}

	protected long addIndividual2(String firstName, String lastName) {
		HibernateUtil.getCurrentSessionWithTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Individual person = new Individual(executor, firstName, lastName);
		long id = (long) HibernateUtil.save(person);
		individualIds.add(id);

		return id;
	}

	protected long addWaitingIndividual(WaitingIndividual waitingIndividual) {
		HibernateUtil.beginTransaction();
		long id = (long) HibernateUtil.save(waitingIndividual);
		HibernateUtil.commitTransaction();
		waitingIndividualIds.add(id);

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

	public void addWorkTime() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		WorkTime workTime = new WorkTime(executor);
		workTime.addDay(new Day(executor, "Nedeľa", 0, 0, 0, 0));
		workTime.addDay(new Day(executor, "Pondelok", 7, 0, 18, 0));
		workTime.addDay(new Day(executor, "Utorok", 8, 30, 18, 0));
		workTime.addDay(new Day(executor, "Streda", 9, 0, 14, 0));
		workTime.addDay(new Day(executor, "Štvrtok", 7, 30, 18, 0));
		workTime.addDay(new Day(executor, "Piatok", 7, 0, 18, 0));
		workTime.addDay(new Day(executor, "Sobota", 7, 0, 18, 0));
		long workTimeId = (long) HibernateUtil.save(workTime);
		workTimeIds.add(workTimeId);
		HibernateUtil.commitTransaction();
	}

	public void removeWorkTimes() {
		for (long workTimeId : workTimeIds) {
			removeWorkTime(workTimeId);
		}
	}

	public void removeWorkTime(long id) {
		HibernateUtil.beginTransaction();
		WorkTime workTime = HibernateUtil.get(WorkTime.class, id);
		HibernateUtil.delete(workTime);
		HibernateUtil.commitTransaction();
	}

	public void addPhysician(String firstName, String lastName, List<String> skillNames) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Individual person = new Individual(executor, firstName, lastName);
		long id = (long) HibernateUtil.save(person);
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

	public void addPhysician(PhysicianType physicianType, String firstName, String lastName) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		Individual person = new Individual(executor, firstName, lastName);
		long id = (long) HibernateUtil.save(person);
		physicianIds.add(id);

		IndividualDao individualDao = IndividualDao.getInstance();
		CompanyDao companyDao = CompanyDao.getInstance();

		person = individualDao.findByFirstAndLastName(firstName, lastName);
		Company company = companyDao.findByName("Company name");
		Physician physician = new Physician(executor, physicianType, person, company);
		HibernateUtil.save(physician);
		HibernateUtil.commitTransaction();

	}

	public void addPhysician(String firstName, String lastName) {

		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		Individual person = new Individual(executor, firstName, lastName);
		long id = (long) HibernateUtil.save(person);
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

		/* physicianIds.add(id); */

		HibernateUtil.commitTransaction();
	}

	public Skill addSkill(String name) {
		/* HibernateUtil.beginTransaction(); */
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Skill skill = new Skill(executor, name);
		long id = (long) HibernateUtil.save(skill);
		skillIds.add(id);
		/* HibernateUtil.commitTransaction(); */
		return skill;
	}

	public void addDevice(Device device) {
		HibernateUtil.beginTransaction();
		long id = (long) HibernateUtil.save(device);
		deviceIds.add(id);
		HibernateUtil.commitTransaction();
	}

	public void addDevice(String name, List<String> treatmentTypeNames) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		DeviceType deviceType = new DeviceType(executor, "laser");
		/* TreatmentType treatmentType = null; */
		/*
		 * for (String treatmentTypeName : treatmentTypeNames) { treatmentType =
		 * treatmentDao.findByName(treatmentTypeName);
		 * deviceType.addPossibleTreatmentType(treatmentType); }
		 */
		Device device = new Device(executor, deviceType, name);
		long id = (long) HibernateUtil.save(device);
		deviceIds.add(id);
		HibernateUtil.commitTransaction();
	}

	public void addDevice(String name, String deviceTypeName) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		DeviceType deviceType = new DeviceType(executor, deviceTypeName);
		Device device = new Device(executor, deviceType, name);
		long id = (long) HibernateUtil.save(device);
		deviceIds.add(id);
		HibernateUtil.commitTransaction();
	}

	public void addRoom(Room room) {
		HibernateUtil.beginTransaction();
		long id = (long) HibernateUtil.save(room);
		roomIds.add(id);
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
			treatmentType = treatmentTypeDao.findByName(treatmentTypeName);
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

	public long createAppointment2(String customerFirstName, String customerLastName, String wantedTreatmentName,
			Date when, Date expectedWhen, Date expectedTo) {
		HibernateUtil.beginTransaction();
		AppointmentScheduler appointmentScheduler = new AppointmentScheduler(getWorkTime(), 15);
		Login login = new Login();
		User executor = login.login("admin", "admin");
		TreatmentType treatmentType = treatmentTypeDao.findByName(wantedTreatmentName);
		Individual customer = individualDao.findByFirstAndLastName(customerFirstName, customerLastName);

		if (customer == null) {
			addIndividual2(customerFirstName, customerLastName);
			customer = individualDao.findByFirstAndLastName(customerFirstName, customerLastName);
		}

		Date to = DateUtil.addSeconds(when, treatmentType.getDuration());
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);
		List<AppointmentProposal> appointmentProposals = appointmentScheduler.getAppointmentProposals(when, to,
				treatmentTypes, 1);
		AppointmentProposal appointmentProposal = appointmentProposals.get(0);
		List<Resource> resources = new ArrayList<>();
		for (Map.Entry<ResourceType, SortedSet<Resource>> entry : appointmentProposal.getResources().entrySet()) {
			resources.add(entry.getValue().first());
		}

		AppointmentScheduleData appointmentScheduleData = new AppointmentScheduleData(appointmentProposal.getFrom(),
				appointmentProposal.getTo(), resources);
		Appointment appointment = appointmentScheduler.getAppointment(executor, appointmentScheduleData, treatmentTypes,
				customer);
		long appointmentId = addAppointment(appointment);
		HibernateUtil.commitTransaction();
		
		return appointmentId;
	}

	public void morphAppointmentToTreatments(User executor, Appointment appointment) {
		HibernateUtil.getCurrentSessionWithTransaction();
		List<TreatmentType> treatmentTypes = appointment.getPlannedTreatmentTypes();

		for (TreatmentType treatmentType : treatmentTypes) {
			Treatment treatment = new Treatment(executor, appointment, treatmentType, new Money(new BigDecimal("50.0")),
					appointment.getFrom(), appointment.getTo());
			Individual executorOfTreatment = individualDao.findByFirstAndLastName("Marika", "Piršelová");
			Physician physician = (Physician) executorOfTreatment.getReservableSourceRoles().get(0);
			treatment.addResource(physician);
			HibernateUtil.save(treatment);
		}

	}

	public void morphAppointmentsToTreatments() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		for (long appointmentId : appointmentIds) {
			Appointment appointment = appointmentDao.findById(appointmentId);
			morphAppointmentToTreatments(executor, appointment);
		}
		HibernateUtil.commitTransaction();
	}

	public void payForAppointments() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Individual individual;
		for (long appointmentId : appointmentIds) {
			Appointment appointment = appointmentDao.findById(appointmentId);
			individual = appointment.getIndividual();
			
			payForAppointment(executor, appointment, individual.getPaymentChannels().get(0));
		}
		HibernateUtil.commitTransaction();
	}

	public void payForAppointment(User executor, Appointment appointment, PaymentChannel paymentChannel) {
		HibernateUtil.getCurrentSessionWithTransaction();
		Money paidAmount = new Money();
		PatientBill patientBill = new PatientBill(executor, paidAmount, 0.19, appointment);
        Hibernate.initialize(appointment.getPatientBill());
		for (Treatment treatment : appointment.getExecutedTreatments()) {
			paidAmount.add(treatment.getPrice());
			patientBill.addItem(new PatientBillItem(executor, treatment.getTreatmentType().getName(), treatment.getPrice(), treatment));
		}
		patientBill.setTotalPrice(paidAmount);		
		HibernateUtil.save(patientBill);
		
		Payment payment = new Payment(executor, appointment.getPatientBill().getItems(), paymentChannel,
				paidAmount);
		/*Payment payment = new Payment(executor, appointment, null, null, new Money(0));*/
		HibernateUtil.save(payment);		
			
		PatientReceipt patientReceipt = new PatientReceipt(executor, "Mária", "Petrášová", 
				appointment, payment.getPaymentChannel().getType());
		
		for (PatientBillItem patientBillItem : payment.getBillItemsToPay()) {
				patientReceipt.addItem(new PatientReceiptItem(executor, patientBillItem.getPrice(), 
						patientBillItem.getName()));
			HibernateUtil.save(patientReceipt);
		}
		
	}

	public long addAppointment(Appointment appointment) {
		HibernateUtil.getCurrentSessionWithTransaction();
		/* HibernateUtil.beginTransaction(); */
		long id = (long) HibernateUtil.save(appointment);
		appointmentIds.add(id);
		/* HibernateUtil.commitTransaction(); */
		return id;
	}

	protected void addNurseRole(NurseType nurseType, String firstName, String lastName) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Individual individual = individualDao.findByFirstAndLastName(firstName, lastName);
		Company company = companyDao.findByName("Company name");
		Nurse nurse = new Nurse(executor, nurseType, individual, company);
		HibernateUtil.save(nurse);
		HibernateUtil.commitTransaction();
	}

	public void addNurse(NurseType nurseType, String firstName, String lastName) {
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
		Nurse nurse = new Nurse(executor, nurseType, person, company);
		HibernateUtil.save(nurse);
		HibernateUtil.commitTransaction();

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
		NurseType nurseType = new NurseType(executor);

		Nurse nurse = new Nurse(executor, nurseType, person, company);
		HibernateUtil.save(nurse);
		HibernateUtil.commitTransaction();
	}

	public void addNurse(String firstName, String lastName, List<String> skillNames) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Individual person = new Individual(executor, firstName, lastName);
		long id = (long) HibernateUtil.save(person);
		nurseIds.add(id);
		HibernateUtil.commitTransaction();

		HibernateUtil.beginTransaction();
		IndividualDao individualDao = IndividualDao.getInstance();
		CompanyDao companyDao = CompanyDao.getInstance();
		person = individualDao.findByFirstAndLastName(firstName, lastName);
		Company company = companyDao.findByName("Company name");
		NurseType nurseType = new NurseType(executor);

		for (String skillName : skillNames) {
			Skill skill = getSkill(skillName);
			nurseType.addSkill(skill);
		}
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

	protected void removeWaitingIndividual(long id) {
		WaitingIndividualDao individualDao = WaitingIndividualDao.getInstance();
		HibernateUtil.beginTransaction();
		WaitingIndividual individual = individualDao.findById(id);
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

	private void removeTreatmentGroup(long id) {
		TreatmentGroupDao treatmentGroupDao = TreatmentGroupDao.getInstance();

		HibernateUtil.beginTransaction();
		TreatmentGroup treatmentGroup = treatmentGroupDao.findById(id);
		HibernateUtil.delete(treatmentGroup);
		HibernateUtil.commitTransaction();
	}

	protected void addIndividuals() {
		addIndividual("Janko", "Mrkvicka");
	}

	protected void addIndividuals2() {
		addIndividual("Janko", "Mrkvicka");
		addIndividual("František", "Zicho");
		addIndividual("Michal", "Očko");
		addIndividual("Pavol", "Kocinec");
		addIndividual("Tomáš", "Krivko");
		addIndividual("Lukáš", "Trnka");
		addIndividual("Jozef", "Šemota");
		addIndividual("Jana", "Pavlanská");
		addIndividual("Michaela", "Holienčíková");
		addIndividual("Zuzana", "Cibulková");
		addIndividual("Petra", "Mokrá");
		addIndividual("Karol", "Kubanda");
		addIndividual("Peter", "Korbel");
		addIndividual("Nataša", "Gerthofferová");
		addIndividual("Tatiana", "Hiková");
		addIndividual("Olga", "Gablasová");
		addIndividual("Juraj", "Marček");
		addIndividual("Martin", "Mrkník");
		addIndividual("Ondrej", "Štalmach");
		addIndividual("Marianna", "Pastvová");

		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		HibernateUtil.commitTransaction();

		for (long id : individualIds) {
			HibernateUtil.beginTransaction();
			Individual individual = individualDao.findById(id);
			HibernateUtil.commitTransaction();
			addCreditCardToIndividual(executor, individual);
		}
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

	protected void addDevices2() {
		addDevice("test device 1", "laser");
		addDevice("test device 2", "laser");
	}

	protected void addRooms() {
		List<String> treatmentTypeNames1 = new ArrayList<>();
		treatmentTypeNames1.add("Odstraňovanie pigmentov chrbát");
		treatmentTypeNames1.add("OxyGeneo - tvár");
		treatmentTypeNames1.add("Epilácia brucha");

		List<String> treatmentTypeNames2 = new ArrayList<>();
		treatmentTypeNames2.add("Odstraňovanie pigmentov chrbát");
		treatmentTypeNames2.add("Odstraňovanie pigmentov celá tvár");
		treatmentTypeNames2.add("Epilácia brucha");

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
		addSkill("test skillA");
		addSkill("test skillB");
	}

	protected void addNurses() {
		addNurse("Zuzana", "Vanková");
	}

	protected void addNurses2() {
		List<String> skillNames1 = new ArrayList<>();
		skillNames1.add("test skillA");
		skillNames1.add("test skillB");
		addNurse("Zuzana", "Vanková", skillNames1);

		List<String> skillNames2 = new ArrayList<>();
		skillNames2.add("test skillA");
		addNurse("Helena", "Podhorská", skillNames2);
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
		/* appointment.setResources(new ArrayList<>()); */
		/* appointment.removeResources(); */
		appointment.prepareForDeleting();
		/* appointment.removeIndividual(); */
		HibernateUtil.saveOrUpdate(appointment);
		/*
		 * for (Resource resource : appointment.getResources()) {
		 * appointment.removeResources(); }
		 */
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

	private void removeTreatmentGroups() {
		for (long id : treatmentGroupIds) {
			removeTreatmentGroup(id);
		}
	}

	private void removeIndividuals() {
		for (long id : individualIds) {
			removeIndividual(id);
		}
	}

	private void removeWaitingIndividuals() {
		for (long id : waitingIndividualIds) {
			removeWaitingIndividual(id);
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

	protected void addTreatmentType(String name, List<ResourceType> resourceTypes, int duration,
			TreatmentGroup treatmentGroup) {
		/* HibernateUtil.beginTransaction(); */

		Login login = new Login();
		User executor = login.login("admin", "admin");
		TreatmentType treatmentType = new TreatmentType(executor, name, new Money(50), 0.1, duration, treatmentGroup);

		for (ResourceType resourceType : resourceTypes) {
			treatmentType.addResourceType(resourceType);
		}

		long id = (long) HibernateUtil.save(treatmentType);
		treatmentTypeIds.add(id);
		/* HibernateUtil.commitTransaction(); */
	}

	protected void addTreatmentType(String name, String price, double provision, int duration,
			TreatmentGroup treatmentGroup) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		PhysicianType physicianType = new PhysicianType(executor);
		NurseType nurseType = new NurseType(executor);
		RoomType roomType = new RoomType(executor);
		TreatmentType treatmentType = new TreatmentType(executor, name, new Money(new BigDecimal(price)), provision,
				duration, treatmentGroup);
		treatmentType.addResourceType(physicianType);
		treatmentType.addResourceType(nurseType);
		treatmentType.addResourceType(roomType);

		treatmentGroup.addTreatmentType(treatmentType);
		HibernateUtil.saveOrUpdate(treatmentGroup);
		/* long id = (long)HibernateUtil.saveOrUpdate(treatmentGroup); */
		/*
		 * long id = treatmentGroup.getId(); treatmentTypeIds.add(id);
		 */

		HibernateUtil.commitTransaction();
	}

	protected TreatmentGroup addTreatmentGroup(String name) {
		Login login = new Login();
		User executor = login.login("admin", "admin");
		TreatmentGroup treatmentGroup = new TreatmentGroup(executor, name);
		long id = (long) HibernateUtil.save(treatmentGroup);
		treatmentGroupIds.add(id);

		return treatmentGroup;
	}

	protected Skill getSkill(String skillName) {
		Skill skill = skillDao.findByName(skillName);

		if (skill == null) {
			skill = addSkill(skillName);
		}

		Hibernate.initialize(skill.getResourceTypes());

		return skill;
	}

	public WorkTime getWorkTime() {
		long workTimeId = workTimeIds.get(0);
		HibernateUtil.getCurrentSessionWithTransaction();
		WorkTime workTime = HibernateUtil.get(WorkTime.class, workTimeId);
		Hibernate.initialize(workTime.getDays());
		return workTime;
	}

	protected void addTreatmentTypes() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		List<ResourceType> resourceTypes1 = new ArrayList<>();
		PhysicianType physicianType1 = new PhysicianType(executor);
		NurseType nurseType1 = new NurseType(executor);
		RoomType roomType1 = new RoomType(executor);
		DeviceType deviceType1 = new DeviceType(executor, "laser");

		physicianType1.addSkill(getSkill("test skill3"));

		resourceTypes1.add(physicianType1);
		resourceTypes1.add(nurseType1);
		resourceTypes1.add(roomType1);
		resourceTypes1.add(deviceType1);

		TreatmentGroup treatmentGroup = new TreatmentGroup(executor, "Odstránenie pigmentových škvŕn");

		long treatmentGroupId = (long) HibernateUtil.save(treatmentGroup);
		treatmentGroupIds.add(treatmentGroupId);

		addTreatmentType("Odstraňovanie pigmentov chrbát", resourceTypes1, 60 * 60, treatmentGroup);
		HibernateUtil.commitTransaction();

		HibernateUtil.beginTransaction();
		login = new Login();
		executor = login.login("admin", "admin");

		List<ResourceType> resourceTypes2 = new ArrayList<>();
		PhysicianType physicianType2 = new PhysicianType(executor);
		NurseType nurseType2 = new NurseType(executor);
		RoomType roomType2 = new RoomType(executor);
		DeviceType deviceType2 = new DeviceType(executor, "laser");

		physicianType2.addSkill(getSkill("test skill1"));
		physicianType2.addSkill(getSkill("test skill2"));

		resourceTypes2.add(physicianType2);
		resourceTypes2.add(nurseType2);
		resourceTypes2.add(roomType2);
		resourceTypes2.add(deviceType2);

		TreatmentGroup treatmentGroup2 = new TreatmentGroup(executor, "OxyGeneo");
		treatmentGroupId = (long) HibernateUtil.save(treatmentGroup2);
		treatmentGroupIds.add(treatmentGroupId);

		addTreatmentType("OxyGeneo - tvár", resourceTypes2, 60 * 60, treatmentGroup2);
		HibernateUtil.commitTransaction();

		HibernateUtil.beginTransaction();
		login = new Login();
		executor = login.login("admin", "admin");
		List<ResourceType> resourceTypes3 = new ArrayList<>();
		PhysicianType physicianType3 = new PhysicianType(executor);
		NurseType nurseType3 = new NurseType(executor);
		RoomType roomType3 = new RoomType(executor);
		DeviceType deviceType3 = new DeviceType(executor, "laser");

		physicianType3.addSkill(getSkill("test skill3"));

		resourceTypes3.add(physicianType3);
		resourceTypes3.add(nurseType3);
		resourceTypes3.add(roomType3);
		resourceTypes3.add(deviceType3);

		addTreatmentType("Odstraňovanie pigmentov celá tvár", resourceTypes3, 60 * 60, treatmentGroup);
		HibernateUtil.commitTransaction();
	}

	protected void addTreatmentTypes2() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");

		List<ResourceType> resourceTypes1 = new ArrayList<>();
		PhysicianType physicianType1 = new PhysicianType(executor);
		NurseType nurseType1 = new NurseType(executor);
		RoomType roomType1 = new RoomType(executor);
		DeviceType deviceType1 = new DeviceType(executor, "laser");

		physicianType1.addSkill(getSkill("test skill3"));
		nurseType1.addSkill(getSkill("test skillA"));
		resourceTypes1.add(physicianType1);
		resourceTypes1.add(nurseType1);
		resourceTypes1.add(roomType1);
		resourceTypes1.add(deviceType1);

		TreatmentGroup treatmentGroup = new TreatmentGroup(executor, "Odstránenie pigmentových škvŕn");

		long treatmentGroupId = (long) HibernateUtil.save(treatmentGroup);
		treatmentGroupIds.add(treatmentGroupId);

		addTreatmentType("Odstraňovanie pigmentov chrbát", resourceTypes1, 60 * 60, treatmentGroup);
		HibernateUtil.commitTransaction();

		HibernateUtil.beginTransaction();
		login = new Login();
		executor = login.login("admin", "admin");

		List<ResourceType> resourceTypes2 = new ArrayList<>();
		PhysicianType physicianType2 = new PhysicianType(executor);
		NurseType nurseType2 = new NurseType(executor);
		RoomType roomType2 = new RoomType(executor);
		DeviceType deviceType2 = new DeviceType(executor, "laser");

		physicianType2.addSkill(getSkill("test skill1"));
		physicianType2.addSkill(getSkill("test skill2"));

		nurseType2.addSkill(getSkill("test skillA"));
		nurseType2.addSkill(getSkill("test skillB"));
		resourceTypes2.add(physicianType2);
		resourceTypes2.add(nurseType2);
		resourceTypes2.add(roomType2);
		resourceTypes2.add(deviceType2);

		TreatmentGroup treatmentGroup2 = new TreatmentGroup(executor, "OxyGeneo");
		treatmentGroupId = (long) HibernateUtil.save(treatmentGroup2);
		treatmentGroupIds.add(treatmentGroupId);

		addTreatmentType("OxyGeneo - tvár", resourceTypes2, 30 * 60, treatmentGroup2);
		HibernateUtil.commitTransaction();

		HibernateUtil.beginTransaction();
		login = new Login();
		executor = login.login("admin", "admin");
		List<ResourceType> resourceTypes3 = new ArrayList<>();
		PhysicianType physicianType3 = new PhysicianType(executor);
		NurseType nurseType3 = new NurseType(executor);
		RoomType roomType3 = new RoomType(executor);
		DeviceType deviceType3 = new DeviceType(executor, "laser");

		physicianType3.addSkill(getSkill("test skill3"));
		nurseType3.addSkill(getSkill("test skillA"));
		resourceTypes3.add(physicianType3);
		resourceTypes3.add(nurseType3);
		resourceTypes3.add(roomType3);
		resourceTypes3.add(deviceType3);

		addTreatmentType("Odstraňovanie pigmentov celá tvár", resourceTypes3, 120 * 60, treatmentGroup);
		HibernateUtil.commitTransaction();

		HibernateUtil.beginTransaction();
		login = new Login();
		executor = login.login("admin", "admin");
		List<ResourceType> resourceTypes4 = new ArrayList<>();
		PhysicianType physicianType4 = new PhysicianType(executor);
		NurseType nurseType4 = new NurseType(executor);
		RoomType roomType4 = new RoomType(executor);
		DeviceType deviceType4 = new DeviceType(executor, "laser");

		physicianType4.addSkill(getSkill("test skill1"));
		nurseType4.addSkill(getSkill("test skillA"));
		resourceTypes4.add(physicianType4);
		resourceTypes4.add(nurseType4);
		resourceTypes4.add(roomType4);
		resourceTypes4.add(deviceType4);

		TreatmentGroup treatmentGroup4 = new TreatmentGroup(executor, "Epilácia");
		treatmentGroupId = (long) HibernateUtil.save(treatmentGroup4);
		treatmentGroupIds.add(treatmentGroupId);
		addTreatmentType("Epilácia brucha", resourceTypes4, 60 * 60, treatmentGroup4);
		HibernateUtil.commitTransaction();
	}

	protected void setUpSystem() {
		addTreatmentTypes();
		addPhysicians();
		addRooms();
		addDevices();
		addNurses();
		addIndividuals();
		addWorkTime();
	}

	protected void setUpSystem2() {
		addTreatmentTypes2();
		addPhysicians();
		addRooms();
		addDevices2();
		addNurses2();
		addIndividuals2();
		addWorkTime();
	}

	protected void tearDownSystem() {
		removeAppointments();
		removePhysicians();
		removeRooms();
		removeDevices();
		removeNurses();
		removeWaitingIndividuals();
		removeIndividuals();
		/* removeTreatmentTypes(); */
		removeTreatmentGroups();
		removeSkills();
		removeWorkTimes();
	}

	public void testApp() {
		setUpSystem();
		tearDownSystem();
	}
}
