package ehc.bo.test;

import java.util.ArrayList;
import java.util.List;

import ehc.bo.impl.AppointmentScheduler;
import ehc.bo.impl.HealthPoint;
import ehc.bo.impl.Login;
import ehc.bo.impl.Money;
import ehc.bo.impl.PhysicianType;
import ehc.bo.impl.ResourceTypeWithSkills;
import ehc.bo.impl.RoomType;
import ehc.bo.impl.Skill;
import ehc.bo.impl.SkillDao;
import ehc.bo.impl.TreatmentGroup;
import ehc.bo.impl.TreatmentGroupDao;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;

public class CanTreatmentsBeCombined_CanBe extends RootTestCase {
	private SkillDao skillDao = SkillDao.getInstance();
	private TreatmentGroupDao treatmentGroupDao = TreatmentGroupDao.getInstance();
	private TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
	private List<Long> treatmentGroupIds = new ArrayList<>();
	protected void setUp() throws Exception {
		super.setUp();
		
		HibernateUtil.beginTransaction();
		Login login = new Login();
		SkillDao skillDao = SkillDao.getInstance();
		User executor = login.login("admin", "admin");
		TreatmentGroup treatmentGroup = new TreatmentGroup(executor, "test treatments");
		long treatmentGroupId = (long)HibernateUtil.save(treatmentGroup);
		treatmentGroupIds.add(treatmentGroupId);
		
		TreatmentType treatmentType = new TreatmentType(executor, "tr1", new Money(), 0.1, 60*60, treatmentGroup);
        ResourceTypeWithSkills physicianType = new PhysicianType(executor);
    	Skill skill = skillDao.findByName("skill 1");
		if (skill == null) {
			skill = new Skill(executor, "skill 1");
		}
        physicianType.addSkill(skill);
		treatmentType.addResourceType(physicianType);
		treatmentType.addResourceType(new RoomType(executor));		
		HibernateUtil.save(treatmentType);	
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
	/*	executor = login.login("admin", "admin");*/
		
		treatmentType = new TreatmentType(executor, "tr2", new Money(), 0.1, 60*60, treatmentGroup);
        physicianType = new PhysicianType(executor);
    	skill = skillDao.findByName("skill 1");
		if (skill == null) {
			skill = new Skill(executor, "skill 1");
		}
        physicianType.addSkill(skill);
		treatmentType.addResourceType(physicianType);
		treatmentType.addResourceType(new RoomType(executor));	
		HibernateUtil.save(treatmentType);
		HibernateUtil.commitTransaction();
		addWorkTime();
	}
		
	public void testApp() {
		HibernateUtil.beginTransaction();
		TreatmentType treatmentType = treatmentTypeDao.findByName("tr1");
		TreatmentType treatmentType2 = treatmentTypeDao.findByName("tr2");
		List<TreatmentType> treatmentTypes = new ArrayList<>();
		treatmentTypes.add(treatmentType);
		treatmentTypes.add(treatmentType2);
		AppointmentScheduler appointmentScheduler = new AppointmentScheduler(getWorkTime(), HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES);
	
		assertTrue(appointmentScheduler.canBeCombinedIntoOneAppointment(treatmentTypes));
		HibernateUtil.commitTransaction();
	
			
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		HibernateUtil.beginTransaction();
		for (long id : treatmentGroupIds) {
			TreatmentGroup treatmentGroup = treatmentGroupDao.findById(id);
			HibernateUtil.delete(treatmentGroup);		
		}
		
		List<Skill> skills = skillDao.getAll();
		for (Skill skill : skills) {
			HibernateUtil.delete(skill);
		}
		
		HibernateUtil.commitTransaction();
	}
	

}
