package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.bo.impl.WorkTime;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class ScheduleAppointmentForGivenTime_TimeAfterWorkTime_ScheduleForNextTime extends RootTestCase {
	private String personFirstName = "Jan";
	private String personLastName = "Novak";
	private String treatmentName = "Odstraňovanie pigmentov chrbát";
	private Date when = DateUtil.date(2016, 7, 14, 17, 0, 0);
	private Date to = DateUtil.date(2016, 7, 14, 18, 20, 0);
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();
	private AppointmentScheduler resourcesUtil;

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
		
		HibernateUtil.beginTransaction();
		WorkTime workTime = getWorkTime();
		HibernateUtil.commitTransaction();
		resourcesUtil = new AppointmentScheduler(workTime, HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);
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
	/*	ResourcesUtil resourcesUtil = new ResourcesUtil();*/
		List<AppointmentProposal> appointmentProposals = resourcesUtil.getAppointmentProposals(when, to, treatmentTypes, 1);
		int countOfResources = getCountOfResources();
		HibernateUtil.commitTransaction();

		AppointmentProposal appointmentProposal = appointmentProposals.get(0);
		Date expectedDate = DateUtil.date(2016, 7, 15, 7, 0, 0);

		assertTrue(appointmentProposal.getFrom().equals(expectedDate));
	}
	
	

}
