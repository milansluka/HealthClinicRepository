package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.AppointmentScheduleData;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class CreateAppointmentWithMoreTreatmentTypes_CannotBeJoined_ChooseResourcesManually extends RootTestCase {
	private IndividualDao individualDao = IndividualDao.getInstance();
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();

	protected void setUp() throws Exception {
		super.setUp();
		if (!isSystemSet()) {
			setUpSystem();	
		}
		
		HibernateUtil.beginTransaction();
		Individual existingPerson = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
		HibernateUtil.commitTransaction();
		if (existingPerson == null) {
			addIndividual("Janko", "Mrkvicka");		
		}
	
	}
	
	public void testApp() {
		HibernateUtil.beginTransaction();
		Login login = new Login();
		User executor = login.login("admin",  "admin");
		Individual individual = individualDao.findByFirstAndLastName("Janko", "Mrkvicka");
		
		TreatmentType treatmentType = treatmentTypeDao.findByName("Odstraňovanie pigmentov chrbát");
		TreatmentType treatmentType2 = treatmentTypeDao.findByName("OxyGeneo - tvár");
		Date from = DateUtil.date(2016, 7, 28, 9, 45, 0);
		Date to = DateUtil.date(2016, 7, 28, 11, 45, 0);
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);
		treatmentTypes.add(treatmentType2);
		
		AppointmentScheduler appointmentScheduler = new AppointmentScheduler(getWorkTime(), HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);
		List<AppointmentProposal> appointmentProposals = appointmentScheduler.getAppointmentProposals(from, to, treatmentTypes, 1);
		
		//treatments can not be joined to one appointment - it is recommended to create
		//two appointments, or if possible, manually choose resources, that can be used
		//for both treatments 
		long appointmentId = -1;
		if (appointmentProposals == null) {
			//choose resources manually
			List<Resource> resources = new ArrayList<>();
			Individual physicianPerson = individualDao.findByFirstAndLastName("Mária", "Petrášová");
			resources.add(physicianPerson.getReservableSourceRoles().get(0));
			
			List<AppointmentScheduleData> appointmentScheduleDatas = appointmentScheduler.getAppointmentScheduleData(from, to, treatmentTypes, resources, 1);
			
			if (appointmentScheduleDatas != null) {
				AppointmentScheduleData appointmentScheduleData = appointmentScheduleDatas.get(0);
				Appointment appointment = appointmentScheduler.getAppointment(executor, appointmentScheduleData, treatmentTypes, individual);
				appointmentId = addAppointment(appointment);
			}
		}
		HibernateUtil.commitTransaction();
	
		HibernateUtil.beginTransaction();
		Appointment appointment = appointmentDao.findById(appointmentId);

		assertTrue(appointment.getPlannedTreatmentTypes().get(0).getName().equals("Odstraňovanie pigmentov chrbát"));
		
		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
