package ehc.bo.test;

import java.util.Date;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.IndividualDao;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import junit.framework.Assert;

public class AppointmentDaoTest extends RootTestCase {

	@BeforeClass
	public void beforeClass() {
		setUpSystem2();
		int year = 2016;


		// create appointments
		/*addAppointment(String customerFirstName, String customerLastName, 
				String treatmentTypeName, Date from, Date to)*/
		
		addAppointment("Janko", "Mrkvicka", 
				"OxyGeneo - tvár", DateUtil.date(year, 8, 1, 9, 0, 0), DateUtil.date(year, 8, 1, 10, 0, 0));
		
		addAppointment("Janko", "Mrkvicka", 
				"OxyGeneo - tvár", DateUtil.date(year, 8, 2, 9, 0, 0), DateUtil.date(year, 8, 2, 10, 0, 0));
		
		addAppointment("Janko", "Mrkvicka", 
				"OxyGeneo - tvár", DateUtil.date(year, 9, 1, 9, 0, 0), DateUtil.date(year, 9, 1, 10, 0, 0));
		
		addAppointment("Janko", "Mrkvicka", 
				"OxyGeneo - tvár", DateUtil.date(year, 8, 5, 9, 0, 0), DateUtil.date(year, 8, 5, 10, 0, 0));
		
		addAppointment("Janko", "Mrkvicka", 
				"OxyGeneo - tvár", DateUtil.date(year, 8, 3, 9, 0, 0), DateUtil.date(year, 8, 3, 10, 0, 0));
		
		addAppointment("Janko", "Mrkvicka", 
				"OxyGeneo - tvár", DateUtil.date(year, 8, 15, 9, 0, 0), DateUtil.date(year, 8, 15, 10, 0, 0));

	}

	@AfterClass
	public void afterClass() {
		tearDownSystem();
	}

	@Test
	public void getAppointmentsWithinTimePeriod() {
		Date from = DateUtil.date(2016, 8, 2, 7, 0, 0);
		Date to = DateUtil.date(2016, 8, 5, 18, 0, 0);
		
		AppointmentDao appointmentDao = new AppointmentDao();
		
		HibernateUtil.beginTransaction();
		List<Appointment> foundAppointments = appointmentDao.getAll(from, to); 
		HibernateUtil.commitTransaction();
		Assert.assertEquals(3, foundAppointments.size());
	}

}
