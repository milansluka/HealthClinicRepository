package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.Day;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.ResourcesUtil;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.bo.impl.WorkTime;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import junit.framework.TestCase;

public class ScheduleAppointmentForGivenTime_TimeBeforeWorkTime_ScheduleForNextTime extends RootTestCase {
	private String personFirstName = "Jan";
	private String personLastName = "Novak";
	private String treatmentName = "Odstraňovanie pigmentov chrbát";
	private Date when = DateUtil.date(2016, 7, 14, 7, 0, 0);
	private Date to = DateUtil.date(2016, 7, 14, 8, 20, 0);
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();
	private ResourcesUtil resourcesUtil;

	protected void setUp() throws Exception {
		super.setUp();

		if (!isSystemSet()) {
			setUpSystem();
		}

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

		List<Day> days = new ArrayList<Day>();
		days.add(new Day("Pondelok", 7, 0, 18, 0));
		days.add(new Day("Utorok", 8, 30, 18, 0));
		days.add(new Day("Streda", 9, 0, 14, 0));
		days.add(new Day("Štvrtok", 7, 30, 18, 0));
		days.add(new Day("Piatok", 7, 0, 18, 0));
		days.add(new Day("Sobota", 7, 0, 18, 0));
		days.add(new Day("Nedeľa", 7, 0, 18, 0));
		WorkTime workTime = new WorkTime(days);
		resourcesUtil = new ResourcesUtil(workTime);
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
	/*	ResourcesUtil resourcesUtil = new ResourcesUtil();*/
		List<AppointmentProposal> appointmentProposals = resourcesUtil.getAppointmentProposals(when, to, treatmentType, 1);
		int countOfResources = getCountOfResources();
		HibernateUtil.commitTransaction();

		AppointmentProposal appointmentProposal = appointmentProposals.get(0);
		Date expectedDate = DateUtil.date(2016, 7, 14, 7, 30, 0);

		assertTrue(appointmentProposal.getFrom().equals(expectedDate));
	}
}
