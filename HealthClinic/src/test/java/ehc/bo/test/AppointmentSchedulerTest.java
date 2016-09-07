package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.AppointmentScheduleData;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.bo.impl.WorkTime;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import junit.framework.Assert;

public class AppointmentSchedulerTest extends RootTestCase {
	private AppointmentScheduler appointmentScheduler;
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();

	@DataProvider
	public static Object[][] appointmentDateTimes() {
		int year = 2016;
		int month = 8;

		Object[][] dates = { { DateUtil.date(year, month, 1, 7, 0, 0), DateUtil.date(year, month, 1, 7, 0, 0) },
				{ DateUtil.date(year, month, 1, 17, 0, 0), DateUtil.date(year, month, 1, 17, 0, 0) },
				{ DateUtil.date(year, month, 1, 17, 10, 0), DateUtil.date(year, month, 2, 8, 30, 0) },
				{ DateUtil.date(year, month, 2, 7, 0, 0), DateUtil.date(year, month, 2, 8, 30, 0) },
				{ DateUtil.date(year, month, 3, 7, 0, 0), DateUtil.date(year, month, 3, 9, 0, 0) },
				{ DateUtil.date(year, month, 4, 7, 0, 0), DateUtil.date(year, month, 4, 7, 30, 0) },
				{ DateUtil.date(year, month, 5, 7, 0, 0), DateUtil.date(year, month, 5, 7, 0, 0) },
				{ DateUtil.date(year, month, 6, 7, 0, 0), DateUtil.date(year, month, 6, 7, 0, 0) },
				{ DateUtil.date(year, month, 6, 17, 10, 0), DateUtil.date(year, month, 8, 7, 0, 0) } };

		return dates;
	}

	@DataProvider
	public static Object[][] appointmentParams() {
		int year = 2016;
		int month = 8;

		Object[][] params = {{"Janko", "Mrkvicka", "Odstraňovanie pigmentov celá tvár", DateUtil.date(year, month, 1, 7, 0, 0), DateUtil.date(year, month, 1, 7, 0, 0), DateUtil.date(year, month, 1, 9, 0, 0)},
				{"Jozef", "Šemota", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 1, 10, 0, 0), DateUtil.date(year, month, 1, 10, 0, 0), DateUtil.date(year, month, 1, 11, 0, 0) },
				{"Milan", "Sluka", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 13, 0, 0), DateUtil.date(year, month, 1, 13, 0, 0), DateUtil.date(year, month, 1, 13, 30, 0) },
				{"Michaela", "Holienčíková", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 17, 30, 0), DateUtil.date(year, month, 1, 17, 30, 0), DateUtil.date(year, month, 1, 18, 0, 0) },
				{"Ondrej", "Štalmach", "OxyGeneo - tvár", DateUtil.date(year, month, 2, 8, 30, 0), DateUtil.date(year, month, 2, 8, 30, 0), DateUtil.date(year, month, 2, 9, 0, 0) },
				{"Karol", "Kubanda", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 7, 0, 0), DateUtil.date(year, month, 1, 7, 0, 0), DateUtil.date(year, month, 1, 7, 30, 0) },
				{"Jana", "Pavlanská", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 7, 15, 0), DateUtil.date(year, month, 1, 7, 30, 0), DateUtil.date(year, month, 1, 8, 0, 0) },
				{"Martin", "Mrník", "OxyGeneo - tvár", DateUtil.date(year, month, 1, 17, 30, 0), DateUtil.date(year, month, 2, 9, 0, 0), DateUtil.date(year, month, 2, 9, 30, 0) },
				{"Tatiana", "Hiková", "Odstraňovanie pigmentov celá tvár", DateUtil.date(year, month, 1, 16, 0, 0), DateUtil.date(year, month, 1, 16, 0, 0), DateUtil.date(year, month, 1, 18, 0, 0) },
				{"Nataša", "Gerthofferová", "Odstraňovanie pigmentov celá tvár", DateUtil.date(year, month, 1, 14, 0, 0), DateUtil.date(year, month, 1, 14, 0, 0), DateUtil.date(year, month, 1, 16, 0, 0) },
				{"Zuzana", "Cibulková", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 1, 13, 0, 0), DateUtil.date(year, month, 1, 13, 0, 0), DateUtil.date(year, month, 1, 14, 0, 0) },
				{"Petra", "Mokrá", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 1, 13, 0, 0), DateUtil.date(year, month, 2, 8, 30, 0), DateUtil.date(year, month, 2, 9, 30, 0) },
				{"Marianna", "Pastvová", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 1, 13, 0, 0), DateUtil.date(year, month, 2, 9, 30, 0), DateUtil.date(year, month, 2, 10, 30, 0) },
				{"Olga", "Gablasová", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 1, 12, 15, 0), DateUtil.date(year, month, 2, 10, 30, 0), DateUtil.date(year, month, 2, 11, 30, 0) },
				{"Juraj", "Marček", "Odstraňovanie pigmentov celá tvár", DateUtil.date(year, month, 2, 12, 30, 0), DateUtil.date(year, month, 2, 12, 30, 0), DateUtil.date(year, month, 2, 14, 30, 0) },
				{"Lukáš", "Trnka", "Odstraňovanie pigmentov chrbát", DateUtil.date(year, month, 2, 11, 30, 0), DateUtil.date(year, month, 2, 11, 30, 0), DateUtil.date(year, month, 2, 12, 30, 0) },
				{"Pavol", "Kocinec", "OxyGeneo - tvár", DateUtil.date(year, month, 2, 10, 0, 0), DateUtil.date(year, month, 2, 10, 0, 0), DateUtil.date(year, month, 2, 10, 30, 0) },
				{"Tomáš", "Krivko", "OxyGeneo - tvár", DateUtil.date(year, month, 2, 8, 15, 0), DateUtil.date(year, month, 2, 9, 30, 0), DateUtil.date(year, month, 2, 10, 0, 0) },
				{"František", "Zicho", "Epilácia brucha", DateUtil.date(year, month, 3, 9, 0, 0), DateUtil.date(year, month, 3, 9, 0, 0), DateUtil.date(year, month, 3, 10, 0, 0) },
				{"Peter", "Korbel", "Epilácia brucha", DateUtil.date(year, month, 3, 9, 0, 0), DateUtil.date(year, month, 3, 9, 0, 0), DateUtil.date(year, month, 3, 10, 0, 0) },
				};

		return params;
	}

	@BeforeClass
	public void beforeClass() {
		setUpSystem2();

		HibernateUtil.beginTransaction();
		WorkTime workTime = getWorkTime();
		HibernateUtil.commitTransaction();
		appointmentScheduler = new AppointmentScheduler(workTime, 15);
	}

	@AfterClass
	public void afterClass() {
		tearDownSystem();
	}

	@Test(dataProvider = "appointmentDateTimes", priority = 1)
	public void getAppointmentProposalWhenResourcesAvailable(Date when, Date expectedWhen) {
		String wantedTreatmentName = "Odstraňovanie pigmentov chrbát";

		HibernateUtil.beginTransaction();
		TreatmentType treatmentType = treatmentTypeDao.findByName(wantedTreatmentName);
		Date to = DateUtil.addSeconds(when, treatmentType.getDuration());
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);

		AppointmentProposal appointmentProposal = appointmentScheduler.getAppointmentProposal(when, to, treatmentTypes);
		HibernateUtil.commitTransaction();

		Assert.assertNotNull(appointmentProposal);
		Assert.assertEquals(expectedWhen, appointmentProposal.getFrom());
	}

	@Test(dataProvider = "appointmentDateTimes", priority = 2)
	public void scheduleAppointment(Date when, Date expectedWhen) {
		String wantedTreatmentName = "Odstraňovanie pigmentov chrbát";

		HibernateUtil.beginTransaction();
		TreatmentType treatmentType = treatmentTypeDao.findByName(wantedTreatmentName);
		Date to = DateUtil.addSeconds(when, treatmentType.getDuration());
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);
		AppointmentProposal appointmentProposal = appointmentScheduler.getAppointmentProposal(when, to, treatmentTypes);
		Assert.assertEquals(expectedWhen, appointmentProposal.getFrom());
		HibernateUtil.commitTransaction();
	}
	
	@Test(dataProvider = "appointmentParams", priority = 3, groups = {"setup"})
	public void createAppointment(String customerFirstName, String customerLastName, String wantedTreatmentName, Date when, Date expectedWhen, Date expectedTo) {
		HibernateUtil.beginTransaction();
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
		List<AppointmentProposal> appointmentProposals = appointmentScheduler.getAppointmentProposals(when, to, treatmentTypes, 1);
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

		HibernateUtil.beginTransaction();
		appointment = HibernateUtil.get(Appointment.class, appointmentId);
		Assert.assertEquals(expectedWhen, appointment.getFrom());
		Assert.assertEquals(expectedTo, appointment.getTo());
		HibernateUtil.commitTransaction();	
	}
	
	@Test(dependsOnMethods = {"createAppointment"})
	public void statistics() {
		
		
		Assert.assertTrue(true);
	}

	/*
	 * @Test public void getAppointmentProposalWhenResourcesNotAvailable() {
	 * String customerFirstName = "Jan"; String customerLastName = "Mrkvicka";
	 * Date when = DateUtil.date(2016, 8, 24, 11, 0, 0); String
	 * wantedTreatmentName = "";
	 * 
	 * }
	 */
}
