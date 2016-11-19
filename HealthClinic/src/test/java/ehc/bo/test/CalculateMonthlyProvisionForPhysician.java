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
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.Money;
import ehc.bo.impl.Physician;
import ehc.bo.impl.Treatment;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class CalculateMonthlyProvisionForPhysician extends RootTestCase {
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();
	private List<Long> appointmentIds = new ArrayList<Long>();

	protected void setUp() throws Exception {
		super.setUp();
		
		if (!isSystemSet()) {
			setUpSystem();
		}
		
		//set amount of provision from treatment type for physician
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin", "admin");
		Individual individual = individualDao.findByFirstAndLastName("Mária", "Petrášová");
		TreatmentType treatmentType = treatmentTypeDao.findByName("Odstraňovanie pigmentov chrbát");
		Physician physician = (Physician)individual.getReservableSourceRoles().get(0);
		physician.addProvision(executor, treatmentType, 0.5);
		HibernateUtil.saveOrUpdate(physician);
		HibernateUtil.commitTransaction();
		
		//add appointments
		HibernateUtil.beginTransaction();
		
		//appointment 2.7.2016 from 7:30 to 8:30
		Date from = DateUtil.date(2016, 7, 2, 7, 30, 0);
		Date to = DateUtil.date(2016, 7, 2, 8, 30, 0);
		treatmentType = treatmentTypeDao.findByName("Odstraňovanie pigmentov chrbát");
		List<TreatmentType> treatmentTypes = new ArrayList<TreatmentType>();
		treatmentTypes.add(treatmentType);
		individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
		List<Resource> resources = new ArrayList<Resource>();
		Individual physicianPerson = individualDao.findByFirstAndLastName("Mária", "Petrášová");
		resources.add(physicianPerson.getReservableSourceRoles().get(0));
		AppointmentScheduler appointmentScheduler = new AppointmentScheduler(getWorkTime(), HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);
		AppointmentScheduleData appointmentScheduleData = new AppointmentScheduleData(from, to, resources);
		Appointment appointment = appointmentScheduler.getAppointment(executor, appointmentScheduleData, treatmentTypes, individual);
		long appId = addAppointment(appointment);
		appointmentIds.add(appId);
		
		//appointment 10.7.2016 from 9:30 to 10:40
		from = DateUtil.date(2016, 7, 10, 9, 30, 0);
		to = DateUtil.date(2016, 7, 10, 10, 40, 0);
		treatmentType = treatmentTypeDao.findByName("Odstraňovanie pigmentov chrbát");
		treatmentTypes = new ArrayList<TreatmentType>();
		treatmentTypes.add(treatmentType);
		individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
		resources = new ArrayList<Resource>();
		physicianPerson = individualDao.findByFirstAndLastName("Mária", "Petrášová");
		resources.add(physicianPerson.getReservableSourceRoles().get(0));
		appointmentScheduleData = new AppointmentScheduleData(from, to, resources);
		appointment = appointmentScheduler.getAppointment(executor, appointmentScheduleData, treatmentTypes, individual);
		appId = addAppointment(appointment);
		appointmentIds.add(appId);
		HibernateUtil.commitTransaction();
		
		//execute treatments
		HibernateUtil.beginTransaction();
		for (long id : appointmentIds) {
			Appointment appointment2 = appointmentDao.findById(id);
			Treatment treatment = new Treatment(executor, appointment2, appointment2.getPlannedTreatmentTypes().get(0), 
					new Money(new BigDecimal("80.0")), appointment2.getFrom(), appointment2.getTo());
			appointment.setState(executor, AppointmentStateValue.CONFIRMED);
			treatment.addResource(appointment2.getResources().get(0));
			HibernateUtil.save(treatment);
		}
		HibernateUtil.commitTransaction();
	}
	
	public void testApp() {
		HibernateUtil.beginTransaction();
		Individual individual = individualDao.findByFirstAndLastName("Mária", "Petrášová");
		
		//calculate provisions from 1.7.2016 to 30.7.2016
		Date from = DateUtil.date(2016, 7, 1, 9, 30, 0);
		Date to = DateUtil.date(2016, 7, 30, 10, 40, 0);
		
		Physician physician = (Physician)individual.getReservableSourceRoles().get(0);
		Money provisionPerMonth = new Money();
		
		for (Treatment treatment : physician.getTreatments()) {
			if (treatment.getFrom().after(from) && treatment.getTo().before(to)) {
				TreatmentType treatmentType = treatment.getTreatmentType();
				double provisionPercentage = physician.getProvisionFromTreatmentType(treatmentType);
				provisionPerMonth =  provisionPerMonth.add(treatment.getPrice().getPercentage(provisionPercentage));
			}		
		}
		
		Money expectedProvisionPerMonth = new Money(80);
		assertTrue(provisionPerMonth.equals(expectedProvisionPerMonth));
		
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
