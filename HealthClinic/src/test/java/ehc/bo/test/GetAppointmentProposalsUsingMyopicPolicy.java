package ehc.bo.test;

import java.util.Date;
import java.util.List;

import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.AppointmentRequest;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDao;
import ehc.bo.impl.MyopicPolicy;
import ehc.bo.impl.SchedulingHorizon;
import ehc.bo.impl.SchedulingParameter;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class GetAppointmentProposalsUsingMyopicPolicy extends RootTestCase {
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private IndividualDao individualDao = IndividualDao.getInstance();
	
	protected void setUp() throws Exception {
		super.setUp();
		
		if (!isSystemSet()) {
			setUpSystem3();		
		}
		addAppointment("Pavol", "Kocinec", "Odstranovanie pigmentov chrbat", DateUtil.date(2017, 3, 20, 7, 0, 0), DateUtil.date(2017, 3, 20, 8, 15, 0));
		addAppointment("Karol", "Kubanda", "OxyGeneo tvar", DateUtil.date(2017, 3, 20, 7, 0, 0), DateUtil.date(2017, 3, 20, 7, 15, 0));
		addAppointment("Tomáš", "Krivko", "Liposukcia", DateUtil.date(2017, 3, 20, 7, 45, 0), DateUtil.date(2017, 3, 20, 8, 30, 0));
	}
	
	public void testApp() {	
		//request
		//patient
		String firstName = "Janko";
		String lastName = "Mrkvicka";
		
		//treatment type
		String treatmentTypeName = "Omladenie tvare";
		
		//scheduling horizon
		Date from = DateUtil.date(2017, 3, 20, 7, 0, 0);
		Date to = DateUtil.date(2017, 3, 21, 18, 0, 0);	
		SchedulingHorizon horizon = new SchedulingHorizon(from, to);
			
		HibernateUtil.beginTransaction();
		Individual caller = individualDao.findByFirstAndLastName(firstName, lastName);
		AppointmentRequest request = new AppointmentRequest(caller, horizon);
		SchedulingParameter param = new SchedulingParameter(getWorkTime());
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentTypeName);
		request.addTreatmentType(treatmentType);
		
		AppointmentScheduler scheduler = new AppointmentScheduler(getWorkTime(), HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES, new MyopicPolicy());
        List<AppointmentProposal> appointmentProposals = scheduler.getAppointmentProposals(request, param);
        AppointmentProposal appointmentProposal = appointmentProposals.get(0);
                
        HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}
}
