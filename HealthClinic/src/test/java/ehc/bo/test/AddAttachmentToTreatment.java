package ehc.bo.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.AppointmentScheduleData;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.AppointmentStateValue;
import ehc.bo.impl.Attachment;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Money;
import ehc.bo.impl.Treatment;
import ehc.bo.impl.TreatmentDao;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class AddAttachmentToTreatment extends RootTestCase {
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();
	private TreatmentDao treatmentDao = TreatmentDao.getInstance();
	private List<Long> treatmentIds = new ArrayList<Long>();

	protected void setUp() throws Exception {
		super.setUp();

		if (!isSystemSet()) {
			setUpSystem();
		}

		// appointment 2.7.2016 from 7:30 to 8:30
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Date from = DateUtil.date(2016, 7, 2, 7, 30, 0);
		Date to = DateUtil.date(2016, 7, 2, 8, 30, 0);
		TreatmentType treatmentType = treatmentTypeDao.findByName("Odstraňovanie pigmentov chrbát");
		List<TreatmentType> treatmentTypes = new ArrayList<TreatmentType>();
		treatmentTypes.add(treatmentType);
		Individual individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
		List<Resource> resources = new ArrayList<Resource>();
		Individual physicianPerson = individualDao.findByFirstAndLastName("Mária", "Petrášová");
		resources.add(physicianPerson.getReservableSourceRoles().get(0));
		AppointmentScheduler appointmentScheduler = new AppointmentScheduler(getWorkTime(), HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);
		AppointmentScheduleData appointmentScheduleData = new AppointmentScheduleData(from, to, resources);
		Appointment appointment = appointmentScheduler.getAppointment(executor, appointmentScheduleData, treatmentTypes, individual);
		long appId = addAppointment(appointment);
		HibernateUtil.commitTransaction();

		// execute treatments
		HibernateUtil.beginTransaction();
		Appointment appointment2 = appointmentDao.findById(appId);
		Treatment treatment = new Treatment(executor, appointment2, appointment2.getPlannedTreatmentTypes().get(0), 
				new Money(new BigDecimal("80.0")), appointment2.getFrom(), appointment2.getTo());
		appointment.setState(executor, AppointmentStateValue.CONFIRMED);
		treatment.addResource(appointment2.getResources().get(0));
		long treatmentId = (Long) HibernateUtil.save(treatment);
		treatmentIds.add(treatmentId);
		HibernateUtil.commitTransaction();
	}

	public void testApp() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Treatment treatment = treatmentDao.findById(treatmentIds.get(0));
		Attachment attachment = new Attachment(executor, "some attachment", "/", treatment);
		HibernateUtil.save(attachment);
		HibernateUtil.commitTransaction();

		HibernateUtil.beginTransaction();
		treatment = treatmentDao.findById(treatmentIds.get(0));
		attachment = treatment.getAttachments().get(0);
        assertTrue(attachment.getName().equals("some attachment"));
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
