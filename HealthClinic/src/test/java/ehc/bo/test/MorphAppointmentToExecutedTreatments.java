package ehc.bo.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.AppointmentStateValue;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Money;
import ehc.bo.impl.Physician;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.Treatment;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.bo.impl.WorkTime;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class MorphAppointmentToExecutedTreatments extends RootTestCase {
	private String personFirstName = "Jan";
	private String personLastName = "Novak";
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();
	private long appointmentId;

	protected void setUp() throws Exception {
		super.setUp();
		
		if (!isSystemSet()) {
			setUpSystem();
		}
		
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
		
		HibernateUtil.beginTransaction();
		WorkTime workTime = getWorkTime();
		HibernateUtil.commitTransaction();

		AppointmentScheduler resourcesUtil = new AppointmentScheduler(workTime, HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);

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
		List<AppointmentProposal> appointmentProposals = resourcesUtil.getAppointmentProposals(when, to, treatmentTypes, 1);

		AppointmentProposal appointmentProposal = appointmentProposals.get(0);
		List<Resource> resources = new ArrayList<>();

		for (Entry<ResourceType, SortedSet<Resource>> entry : appointmentProposal.getResources().entrySet()) {
			resources.add(entry.getValue().first());
		}

		Appointment appointment = new Appointment(executor, when, to, individual);
		appointment.addResources(resources);
		appointmentId = addAppointment(appointment);
		HibernateUtil.commitTransaction();
	}
	
	public void testApp() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Appointment appointment = appointmentDao.findById(appointmentId);
		TreatmentType treatmentType = treatmentTypeDao.findByName("Odstraňovanie pigmentov chrbát");
		appointment.setState(executor, AppointmentStateValue.CONFIRMED);
		Treatment treatment = new Treatment(executor, appointment, treatmentType, new Money(new BigDecimal("50.0")), appointment.getFrom(), appointment.getTo());
		Individual executorOfTreatment = individualDao.findByFirstAndLastName("Mária", "Petrášová");
		Physician physician = (Physician)executorOfTreatment.getReservableSourceRoles().get(0);
		treatment.addResource(physician);
		HibernateUtil.save(treatment);
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		appointment = appointmentDao.findById(appointmentId);
		Money money = new Money(new BigDecimal("50.0"));
		assertTrue(appointment.getExecutedTreatments().get(0).getPrice().equals(money));
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
