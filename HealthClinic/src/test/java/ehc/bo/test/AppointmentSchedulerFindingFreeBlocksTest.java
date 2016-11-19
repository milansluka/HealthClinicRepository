package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.TimeWindow;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.WorkTime;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class AppointmentSchedulerFindingFreeBlocksTest extends RootTestCase {
	private AppointmentScheduler appointmentScheduler;
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	
	@DataProvider
	public static Object[][] appointmentDateTimes() {
		Object[][] treatmentTypeNames = { {"Odstraňovanie pigmentov chrbát", 12}};
		
		return treatmentTypeNames;
	}
	
	@BeforeClass
	public void beforeClass() {
		setUpSystem2();
		
		HibernateUtil.beginTransaction();
		WorkTime workTime = getWorkTime();
		HibernateUtil.commitTransaction();

		appointmentScheduler = new AppointmentScheduler(workTime, 15);
	}
	
	@AfterMethod
	public void deleteAppointments() {
		removeAppointments();
	}
	
	@Test
	public void getFreeTimeWindowsIfNoAppointmentPlanned() {
		Date monday = DateUtil.date(2016, 9, 26);
		Date friday = DateUtil.addDays(monday, 4);
		
		HibernateUtil.beginTransaction();
		WorkTime workTime = getWorkTime();
		Date from = workTime.getStartWorkTime(monday);
		Date to = workTime.getEndWorkTime(friday);	
		TreatmentType treatmentType = treatmentTypeDao.findByName("Odstraňovanie pigmentov chrbát");
		List<TreatmentType> treatmentTypes = new ArrayList<TreatmentType>();
		treatmentTypes.add(treatmentType);
		List<TimeWindow> timeWindows = appointmentScheduler.findAvailableTimeWindows(from, to, treatmentTypes, 60*60);	
		assertEquals(10, timeWindows.size());	
		HibernateUtil.commitTransaction();

	}
	
/*	@Test
	public void addAppointmentsTest() {
		addAppointments();
		
		HibernateUtil.beginTransaction();
		List<Appointment> appointments = appointmentDao.getAll();
		assertEquals(10, appointments.size());
		HibernateUtil.commitTransaction();
	}*/
	
	
	
	@Test(dataProvider = "appointmentDateTimes")
	public void getFreeTimeWindows(String treatmentTypeName, int expectedTimeWindowsCount) {	
		Date monday = DateUtil.date(2016, 9, 26);
		Date friday = DateUtil.addDays(monday, 4);
		addAppointments();
		HibernateUtil.beginTransaction();
		WorkTime workTime = getWorkTime();
		Date from = workTime.getStartWorkTime(monday);
		Date to = workTime.getEndWorkTime(friday);	
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentTypeName);
		List<TreatmentType> treatmentTypes = new ArrayList<TreatmentType>();
		treatmentTypes.add(treatmentType);
		List<TimeWindow> timeWindows = appointmentScheduler.findAvailableTimeWindows(from, to, treatmentTypes, 60*60);	
		assertEquals(expectedTimeWindowsCount, timeWindows.size());	
		HibernateUtil.commitTransaction();

	}

	@AfterClass
	public void afterClass() {
		tearDownSystem();
	}

}
