package ehc.bo.impl;

import java.util.Date;
import java.util.List;

public class ResourcesUtil {
	
	public Physician findPhysician(Date from, Date to, TreatmentType treatmentType) {
		PhysicianDao physicianDao = PhysicianDao.getInstance();
		List<Physician> physicians = physicianDao.getAll();
		
		PhysicianType requiredPhysicianType = null;
		
		for (ResourceType resourceType : treatmentType.resourceTypes) {
			if (resourceType instanceof PhysicianType) {
				requiredPhysicianType  = (PhysicianType)resourceType;		
			}
		}
		
		for (Physician physician : physicians) {
			if (physician.isCompetent(requiredPhysicianType)) {
				if (physician.isAvailable(from, to)) {
					return physician;			
				}		
			}
		}
		
		return null;	
	}
	
	public Nurse findNurse() {
		NurseDao nurseDao = NurseDao.getInstance();
		List<Nurse> nurses = nurseDao.getAll();
		
		if (!nurses.isEmpty()) {
			return nurses.get(0);			
		}    
		return null;
	}
	
	public Room findRoom() {
	    RoomDao roomDao = RoomDao.getInstance();
		List<Room> rooms = roomDao.getAll();
		
		if (!rooms.isEmpty()) {
			return rooms.get(0);			
		}    
		return null;
	}

}
