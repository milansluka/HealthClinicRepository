package ehc.bo.test;

import ehc.bo.impl.Login;
import ehc.bo.impl.Money;
import ehc.bo.impl.NurseType;
import ehc.bo.impl.PhysicianType;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.ResourceTypeWithSkills;
import ehc.bo.impl.RoomType;
import ehc.bo.impl.Skill;
import ehc.bo.impl.TreatmentGroup;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDao;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;

public class AddTreatmentType extends RootTestCase {
    private long treatmentGroupId;

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testApp() {
		Login login = new Login();
		
		HibernateUtil.beginTransaction();
		User executor = login.login("admin", "admin");
		Money price = new Money(80);
		TreatmentGroup treatmentGroup = new TreatmentGroup(executor, "test treatments");
	    treatmentGroupId = (long)HibernateUtil.save(treatmentGroup);
		
		TreatmentType treatmentType = new TreatmentType(executor, "Test treatment", price, 0.1, 60*60, treatmentGroup);
		ResourceTypeWithSkills resourceType = new PhysicianType(executor);
		resourceType.addSkill(new Skill(executor, "some physician test skill"));
		ResourceTypeWithSkills resourceType2 = new NurseType(executor);
		resourceType2.addSkill(new Skill(executor, "some nurse test skill"));
		ResourceType resourceType3 = new RoomType(executor);
		treatmentType.addResourceType(resourceType);
		treatmentType.addResourceType(resourceType2);
		treatmentType.addResourceType(resourceType3);
		
		
		HibernateUtil.save(treatmentType);
		HibernateUtil.commitTransaction();
		
		HibernateUtil.beginTransaction();
		TreatmentTypeDao treatmentTypeDao = TreatmentTypeDao.getInstance();
		treatmentType = treatmentTypeDao.findByName("Test treatment");
		
		assertTrue(treatmentType.getResourceTypes().size() == 3);
		
		HibernateUtil.commitTransaction();		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		HibernateUtil.beginTransaction();
		TreatmentGroup treatmentGroup = HibernateUtil.get(TreatmentGroup.class, treatmentGroupId);
		
		for (TreatmentType treatmentType : treatmentGroup.getTreatmentTypes()) {
			for(ResourceType resourceType : treatmentType.getResourceTypes()) {
				if (resourceType instanceof ResourceTypeWithSkills) {
					ResourceTypeWithSkills resourceTypeWithSkills = (ResourceTypeWithSkills)resourceType;
					for (Skill skill : resourceTypeWithSkills.getSkills()) {
						HibernateUtil.delete(skill);
					}
				}
			}		
		}

		
		HibernateUtil.delete(treatmentGroup);
		HibernateUtil.commitTransaction();
	}
}
