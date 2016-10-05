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
import ehc.bo.impl.Nurse;
import ehc.bo.impl.Physician;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.Room;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.bo.impl.WorkTime;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import junit.framework.Assert;

public class AppointmentSchedulerAssigningResourcesTest extends RootTestCase {
	private AppointmentScheduler appointmentScheduler;
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();

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

	@DataProvider
	public static Object[][] appointmentParams() {
		int year = 2016;
		int month = 9;
		int day = 26;

		Object[][] params = {
				{ DateUtil.date(year, month, day, 7, 15, 0), DateUtil.date(year, month, day, 8, 15, 0),
						"Odstraňovanie pigmentov chrbát", "Petrášová", "Podhorská", "test room 1" },
				{ DateUtil.date(year, month, day, 7, 15, 0), DateUtil.date(year, month, day, 8, 15, 0),
						"Epilácia brucha", "Piršelová", "Vanková", "test room 2" },
				{ DateUtil.date(year, month, day, 8, 15, 0), DateUtil.date(year, month, day, 9, 15, 0),
						"Epilácia brucha", "Piršelová", "Podhorská", "test room 1" },
				{ DateUtil.date(year, month, day, 9, 45, 0), DateUtil.date(year, month, day, 10, 45, 0),
						"Epilácia brucha", "Piršelová", "Podhorská", "test room 1" },
				{ DateUtil.date(year, month, day, 11, 30, 0), DateUtil.date(year, month, day, 12, 0, 0),
						"OxyGeneo - tvár", "Piršelová", "Vanková", "test room 3" },
				{ DateUtil.date(year, month, day, 12, 30, 0), DateUtil.date(year, month, day, 14, 30, 0),
						"Odstraňovanie pigmentov celá tvár", "Petrášová", "Podhorská", "test room 2" }, 
				{ DateUtil.date(year, month, day, 15, 0, 0), DateUtil.date(year, month, day, 15, 30, 0),
							"OxyGeneo - tvár", "Piršelová", "Vanková", "test room 3" },
				{ DateUtil.date(year, month, day, 15, 45, 0), DateUtil.date(year, month, day, 16, 15, 0),
								"OxyGeneo - tvár", "Piršelová", "Vanková", "test room 3" },
				{ DateUtil.date(year, month, day, 16, 0, 0), DateUtil.date(year, month, day, 18, 0, 0),
									"Odstraňovanie pigmentov celá tvár", "Petrášová", "Podhorská", "test room 2" },
				{ DateUtil.date(year, month, day, 17, 0, 0), DateUtil.date(year, month, day, 18, 0, 0),
										"Epilácia brucha", "Piršelová", "Vanková", "test room 1" },
				};

		return params;
	}

	@Test(dataProvider = "appointmentParams")
	public void scheduleAppointment(Date when, Date to, String treatmentTypeName, String expectedPhysicianName,
			String expectedNurseName, String expectedRoomName) {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentTypeName);
		Individual customer = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");

		if (customer == null) {
			addIndividual2("Janko", "Mrkvicka");
			customer = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
		}
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

		HibernateUtil.beginTransaction();
		appointment = HibernateUtil.get(Appointment.class, appointmentId);

		Physician physician = null;
		Nurse nurse = null;
		Room room = null;

		for (Resource resource : appointment.getResources()) {
			if (resource instanceof Physician) {
				physician = (Physician) resource;
			} else if (resource instanceof Nurse) {
				nurse = (Nurse) resource;
			} else if (resource instanceof Room) {
				room = (Room) resource;
			}
		}

		Assert.assertEquals(expectedPhysicianName, physician.getSource().getName());
		Assert.assertEquals(expectedNurseName, nurse.getSource().getName());
		Assert.assertEquals(expectedRoomName, room.getName());

		HibernateUtil.commitTransaction();
	}
}
