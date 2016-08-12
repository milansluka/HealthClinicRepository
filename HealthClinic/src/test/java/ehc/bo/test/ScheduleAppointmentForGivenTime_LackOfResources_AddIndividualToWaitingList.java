package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.bo.impl.WaitingIndividual;
import ehc.bo.impl.WaitingIndividualDao;
import ehc.bo.impl.WorkTime;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class ScheduleAppointmentForGivenTime_LackOfResources_AddIndividualToWaitingList extends RootTestCase {
	private String personFirstName = "Jan";
	private String personLastName = "Novak";
	private String treatmentName = "Odstraňovanie pigmentov chrbát";
	private Date when = DateUtil.date(2016, 7, 7, 8, 0, 0);
	private Date to = DateUtil.date(2016, 7, 7, 9, 20, 0);
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();
	private WaitingIndividualDao waitingIndividualDao = WaitingIndividualDao.getInstance();

	protected void setUp() throws Exception {
		super.setUp();

		if (isSystemSet()) {
			tearDownSystem();
		}

		addPhysicians();
		addNurses();
		addTreatmentTypes();
		addIndividuals();
		addDevices();
		addWorkTime();
		
		List<String> treatmentNames = new ArrayList<>();
		treatmentNames.add("Odstraňovanie pigmentov chrbát");
		treatmentNames.add("OxyGeneo - tvár");
		addRoom("test room 1", treatmentNames);

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

		// appointment from 7:30 to 8:30
		Date when = DateUtil.date(2016, 7, 7, 7, 30, 0);
		Date to = DateUtil.date(2016, 7, 7, 8, 30, 0);
		String treatmentName = "OxyGeneo - tvár";
		String personFirstName = "Janko";
		String personLastName = "Mrkvicka";

		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Individual individual = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentName);
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);
		AppointmentScheduler appointmentScheduler = new AppointmentScheduler(getWorkTime(), HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);
		List<AppointmentProposal> appointmentProposals = appointmentScheduler.getAppointmentProposals(when, to, treatmentTypes, 1);

		AppointmentProposal appointmentProposal = appointmentProposals.get(0);
		/*Date from = DateUtil.date(2016, 7, 7, 7, 30, 0);*/
		/*Date to = DateUtil.date(2016, 7, 7, 8, 30, 0);*/
		List<Resource> resources = new ArrayList<>();

		for (Entry<ResourceType, SortedSet<Resource>> entry : appointmentProposal.getResources().entrySet()) {
			resources.add(entry.getValue().first());
		}

		Appointment appointment = new Appointment(executor, when, to, individual);
		appointment.addResources(resources);
		addAppointment(appointment);
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

	public void testApp() {
		Login login = new Login();

		HibernateUtil.beginTransaction();
		User executor = login.login("admin", "admin");
		Individual person = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentName);
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);
		AppointmentScheduler appointmentScheduler = new AppointmentScheduler(getWorkTime(), HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);
		AppointmentProposal appointmentProposal = appointmentScheduler.getAppointmentProposal(when, to, treatmentTypes);
		HibernateUtil.commitTransaction();
		long id = -1;
		if (appointmentProposal == null) {
			WaitingIndividual waitingIndividual = new WaitingIndividual(executor, person, treatmentType, when, to);
			id = addWaitingIndividual(waitingIndividual);
		}			
			
		HibernateUtil.beginTransaction();
		WaitingIndividual waitingIndividual = waitingIndividualDao.findById(id);
		assertTrue(waitingIndividual.getIndividual().getFirstName().equals(personFirstName));
		HibernateUtil.commitTransaction();
	}

}
