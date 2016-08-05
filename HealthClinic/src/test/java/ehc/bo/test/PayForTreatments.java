package ehc.bo.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.AppointmentStateValue;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Money;
import ehc.bo.impl.Payment;
import ehc.bo.impl.PaymentChannel;
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
		Appointment appointment = new Appointment(executor, from, to, treatmentTypes, individual);
		appointment.addResources(resources);
		long appId = addAppointment(appointment);
		appointmentIds.add(appId);
		HibernateUtil.commitTransaction();
		
		//execute treatments	
		HibernateUtil.beginTransaction();
		Appointment appointment2 = appointmentDao.findById(appId);
		Treatment treatment = new Treatment(executor, appointment2, appointment2.getTreatmentTypes().get(0), new Money(new BigDecimal("80.0")), appointment2.getFrom(), appointment2.getTo());
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
			paidAmount.add(treatment.getPrice());
		}
		
		long paymentId = -1;
		
		Individual individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
        PaymentChannel paymentChannel = new PaymentChannel(executor, individual);
        HibernateUtil.save(paymentChannel);
		
		Payment payment = new Payment(executor, appointment, appointment.getExecutedTreatments(), 
				paymentChannel, paidAmount);
		if (payment.isSufficient()) {
			paymentId = (long)HibernateUtil.save(payment);		
		}
	
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		PaymentDao paymentDao = PaymentDao.getInstance();
		payment = paymentDao.findById(paymentId);
		assertTrue(payment.getPaidAmount().equals(paidAmount));
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
