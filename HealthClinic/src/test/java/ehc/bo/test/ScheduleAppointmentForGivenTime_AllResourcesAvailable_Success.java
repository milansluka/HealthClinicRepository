package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.hibernate.Hibernate;

import ehc.bo.Resource;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentDao;
import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.AppointmentScheduleData;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.Login;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;


//There are no other planned appointments
public class ScheduleAppointmentForGivenTime_AllResourcesAvailable_Success extends RootTestCase {
	private String personFirstName = "Jan";
	private String personLastName = "Novak";
	private String treatmentName = "Odstraňovanie pigmentov chrbát";
	private Date when = DateUtil.date(2016, 7, 7, 8, 0, 0);
	private Date to = DateUtil.date(2016, 7, 7, 9, 20, 0);
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();
	
	protected void setUp() throws Exception {
		super.setUp();

		if (!isSystemSet()) {
			setUpSystem();
		}
		
		HibernateUtil.beginTransaction();
		Individual existingPerson = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		HibernateUtil.commitTransaction();
		if (existingPerson == null) {
			Login login = new Login();
			
			HibernateUtil.beginTransaction();
			User executor = login.login("admin", "admin");
			Individual person = new Individual(executor, personFirstName, personLastName);
			HibernateUtil.save(person);		
			HibernateUtil.commitTransaction();
			
		}
	}

	public void testApp() {
		Login login = new Login();
		
		HibernateUtil.beginTransaction();
		User executor = login.login("admin", "admin");
		Individual person = individualDao.findByFirstAndLastName(personFirstName, personLastName);
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentName);
		Hibernate.initialize(treatmentType.getResourceTypes());
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);
		
		AppointmentScheduler appointmentScheduler = new AppointmentScheduler(getWorkTime(), HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);
        List<AppointmentProposal> appointmentProposals = appointmentScheduler.getAppointmentProposals(when, to, treatmentTypes, 1);
      
        
        AppointmentProposal appointmentProposal = appointmentProposals.get(0);   
            
        Map<ResourceType, SortedSet<Resource>> resources = appointmentProposal.getResources();
        
        assertTrue(appointmentProposal.getFrom().equals(when) && 
        		appointmentProposal.getResources().size() == 4); 
        
        List<Resource> resourceList = new ArrayList<>();
        
        for (Map.Entry<ResourceType, SortedSet<Resource>> entry : resources.entrySet()) {
        	resourceList.add(entry.getValue().first());	
        }
        
        AppointmentScheduleData appointmentScheduleData = new AppointmentScheduleData(when, to, resourceList);
        Appointment appointment = appointmentScheduler.getAppointment(executor, appointmentScheduleData, treatmentTypes, person);
        long appointmentId = addAppointment(appointment);
        HibernateUtil.commitTransaction();
        
        HibernateUtil.beginTransaction();
        appointment = appointmentDao.findById(appointmentId);
        assertEquals(4, appointment.getResources().size());
        HibernateUtil.commitTransaction();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
