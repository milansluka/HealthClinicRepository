package ehc.bo.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import ehc.bo.Resource;
import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.PhysicianType;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;

public class GetAppointmentProposalsFromGivenTime extends RootTestCase {
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	
	protected void setUp() throws Exception {
		super.setUp();
		
		if (!isSystemSet()) {
			setUpSystem();		
		}	
	}
	
	public void testApp() {	
		String treatmentName = "Odstraňovanie pigmentov chrbát";
		Date when = DateUtil.date(2016, 7, 19, 10, 0, 0);

		HibernateUtil.beginTransaction();
		TreatmentType treatmentType = treatmentTypeDao.findByName(treatmentName);
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);
		Date to = DateUtil.date(2016, 7, 19, 11, 30, 0);
		
		AppointmentScheduler resourcesUtil = new AppointmentScheduler(getWorkTime(), HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);
        List<AppointmentProposal> appointmentProposals = resourcesUtil.getAppointmentProposals(when, to, treatmentTypes, 1);
        AppointmentProposal appointmentProposal = appointmentProposals.get(0);
        int countOfPhysicians = 0;
        
        for (Map.Entry<ResourceType, SortedSet<Resource>> entry : appointmentProposal.getResources().entrySet()) {
        	if (entry.getKey() instanceof PhysicianType) {
        		countOfPhysicians = entry.getValue().size();		
        	}
        }     
        HibernateUtil.commitTransaction();
        
        assertTrue(countOfPhysicians == 1);

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownSystem();
	}

}
