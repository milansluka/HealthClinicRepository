package ehc.bo.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.AppointmentStateValue;
import ehc.bo.impl.Login;
import ehc.bo.impl.Money;
import ehc.bo.impl.PatientBill;
import ehc.bo.impl.PatientBillItem;
import ehc.bo.impl.Treatment;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import junit.framework.Assert;

public class PatientBillTest extends RootTestCase {
	private long appointmentId;
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();

	@BeforeClass
	public void beforeClass() {
		setUpSystem2();
		
		appointmentId = createAppointment2("Janko", "Mrkvicka", "Odstraňovanie pigmentov chrbát", 
				DateUtil.date(2016, 9, 16, 11, 0, 0), DateUtil.date(2016, 9, 16, 11, 0, 0), 
				DateUtil.date(2016, 9, 16, 12, 0, 0));
		

	}

	@Test
	public void createPatientBill() {
		Login login = new Login();
		HibernateUtil.beginTransaction();
		User executor = login.login("admin", "admin");
		Appointment appointment = appointmentDao.findById(appointmentId);
		appointment.setState(executor, AppointmentStateValue.CONFIRMED);
		Treatment treatment = new Treatment(executor, appointment, appointment.getPlannedTreatmentTypes().get(0), 
				new Money(25), DateUtil.date(2016, 9, 16, 11, 0, 0), DateUtil.date(2016, 9, 16, 12, 0, 0));
		HibernateUtil.save(treatment);
	
		PatientBill patientBill = new PatientBill(executor, new Money(25), 0.19, appointment);
		patientBill.addItem(new PatientBillItem(executor, "Odstraňovanie pigmentov chrbát", new Money(25), treatment));
		long patientBillId = (Long)HibernateUtil.save(patientBill);
		HibernateUtil.commitTransaction();
        
		HibernateUtil.beginTransaction();
		patientBill = HibernateUtil.get(PatientBill.class, patientBillId);
		Assert.assertEquals(new Money(25), patientBill.getTotalPrice());
		Assert.assertEquals(0.19, patientBill.getVATrate());
		Assert.assertEquals("Janko", patientBill.getAppointment().getIndividual().getFirstName());
		
		for(PatientBillItem patientBillItem : patientBill.getItems()) {
			Assert.assertEquals("Odstraňovanie pigmentov chrbát", patientBillItem.getName());
			Assert.assertEquals(new Money(25), patientBillItem.getPrice());
		}
		
		HibernateUtil.commitTransaction();
	}
	
    @AfterClass
    public void afterClass() {
    	tearDownSystem();
    }
}
