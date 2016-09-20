package ehc.bo.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.AppointmentScheduleData;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.AppointmentStateValue;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Money;
import ehc.bo.impl.PatientBill;
import ehc.bo.impl.PatientBillItem;
import ehc.bo.impl.PatientReceipt;
import ehc.bo.impl.PatientReceiptDao;
import ehc.bo.impl.Payment;
import ehc.bo.impl.PaymentChannel;
import ehc.bo.impl.PaymentChannelType;
import ehc.bo.impl.PaymentDao;
import ehc.bo.impl.Treatment;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class PayForTreatments extends RootTestCase {
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();
	private PatientReceiptDao patientReceiptDao = PatientReceiptDao.getInstance();
	private List<Long> appointmentIds = new ArrayList<>();

	protected void setUp() throws Exception {
		super.setUp();
		
		if (!isSystemSet()) {
			setUpSystem();
		}
			
		//appointment 2.7.2016 from 7:30 to 8:30
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Date from = DateUtil.date(2016, 7, 2, 7, 30, 0);
		Date to = DateUtil.date(2016, 7, 2, 8, 30, 0);
		TreatmentType treatmentType = treatmentTypeDao.findByName("Odstraňovanie pigmentov chrbát");
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);
		Individual individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
		List<Resource> resources = new ArrayList<>();
		Individual physicianPerson = individualDao.findByFirstAndLastName("Mária", "Petrášová");
		resources.add(physicianPerson.getReservableSourceRoles().get(0));
		AppointmentScheduler appointmentScheduler = new AppointmentScheduler(getWorkTime(), HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);
		AppointmentScheduleData appointmentScheduleData = new AppointmentScheduleData(from, to, resources);
		Appointment appointment = appointmentScheduler.getAppointment(executor, appointmentScheduleData, treatmentTypes, individual);
		long appId = addAppointment(appointment);
		appointmentIds.add(appId);
		HibernateUtil.commitTransaction();
		
		//execute treatments	
		HibernateUtil.beginTransaction();
		Appointment appointment2 = appointmentDao.findById(appId);
		Treatment treatment = new Treatment(executor, appointment2, appointment2.getPlannedTreatmentTypes().get(0), new Money(new BigDecimal("80.0")), appointment2.getFrom(), appointment2.getTo());
		appointment.setState(executor, AppointmentStateValue.CONFIRMED);
		treatment.addResource(appointment2.getResources().get(0));
		HibernateUtil.save(treatment);
		HibernateUtil.commitTransaction();
	}
	
	public void testApp() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Appointment appointment = appointmentDao.findById(appointmentIds.get(0));
	/*	Treatment treatment = appointment.getExecutedTreatments().get(0);*/
		
		Money paidAmount = new Money();
		
		for (Treatment treatment : appointment.getExecutedTreatments()) {
			paidAmount = paidAmount.add(treatment.getPrice());
		}
		
		long paymentId = -1;
		
		Individual individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
        PaymentChannel paymentChannel = new PaymentChannel(executor, individual, PaymentChannelType.CASH);
        HibernateUtil.save(paymentChannel);
		
        PatientBill patientBill = new PatientBill(executor, paidAmount, 0.19, appointment);
        for (Treatment treatment : appointment.getExecutedTreatments()) {
        	patientBill.addItem(new PatientBillItem(executor, treatment.getTreatmentType().getName(), treatment.getPrice(), treatment)); 	
        }
        HibernateUtil.save(patientBill);
         
		Payment payment = new Payment(executor, appointment.getPatientBill().getItems(), 
				paymentChannel, paidAmount);
		
		long receiptId = -1;
		
		if (payment.isSufficient()) {
			paymentId = (long)HibernateUtil.save(payment);	
			PatientReceipt patientReceipt = new PatientReceipt(executor, "Mária", "Petrášová", appointment, payment.getPaymentChannel().getType());
			patientReceipt.addPaidBills(payment.getBillItemsToPay());
			Hibernate.initialize(patientReceipt.getItems());
			receiptId = (long)HibernateUtil.save(patientReceipt);
		}
	
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		PaymentDao paymentDao = PaymentDao.getInstance();
		payment = paymentDao.findById(paymentId);
		appointment = appointmentDao.findById(appointmentIds.get(0));
		PatientReceipt patientReceipt = patientReceiptDao.findById(receiptId);
		assertTrue(payment.getPaidAmount().equals(paidAmount) && appointment.isPayed());
		assertEquals(paidAmount, patientReceipt.getTotalPayedPrice());
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
