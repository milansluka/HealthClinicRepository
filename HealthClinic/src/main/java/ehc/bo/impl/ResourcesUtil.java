package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ehc.bo.Resource;
import ehc.util.DateUtil;

public class ResourcesUtil {
	private PhysicianDao physicianDao = PhysicianDao.getInstance();
	private NurseDao nurseDao = NurseDao.getInstance();
	private RoomDao roomDao = RoomDao.getInstance();

	public List<AppointmentProposal> getAppointmentProposals(Date from, TreatmentType treatmentType, int count) {
		List<AppointmentProposal> appointmentProposals = new ArrayList<>();

		int proposedAppointments = 0;
		// in seconds
		int margin = 0;
		// in seconds
		int treatmentDuration = treatmentType.getDuration();
		int grid = 30 * 60;

		Date to = DateUtil.addSeconds(from, treatmentDuration + margin);

		while (proposedAppointments < count) {
			Map<ResourceType, List<Resource>> resources = getResources(from, to, treatmentType.getResourceTypes());

			if (resources != null) {
				AppointmentProposal appointmentProposal = new AppointmentProposal(resources, treatmentType, from, to);
				appointmentProposals.add(appointmentProposal);
				proposedAppointments++;
			}

			from = DateUtil.addSeconds(from, grid);
			to = DateUtil.addSeconds(to, grid);
		}

		return appointmentProposals;
	}

	private List<Resource> findSuitableResources(List<? extends ResourceImpl> resources, ResourceType resourceType, Date from, Date to) {
		List<Resource> suitableResources = new ArrayList<>();
		
		for (Resource resource : resources) {
			if (resource.isSuitable(resourceType)) {
				if (resource.isAvailable(from, to)) {
					suitableResources.add(resource);
				}
			}
		}	
		return suitableResources;
	}

	public Map<ResourceType, List<Resource>> getResources(Date from, Date to, List<ResourceType> neededResourceTypes) {
		/* List<Resource> resources = new ArrayList<>(); */
		Map<ResourceType, List<Resource>> resources = new HashMap<>();
		boolean foundResource = false;

		for (ResourceType neededResourceType : neededResourceTypes) {
			if (neededResourceType instanceof PhysicianType) {
				List<Physician> physicians = physicianDao.getAll();
				List<Resource> suitablePhysicians = findSuitableResources(physicians, neededResourceType, from, to);
				if (suitablePhysicians.isEmpty()) {
					return null;		
				}
/*				List<Resource> suitablePhysicians = new ArrayList<>();
				for (Physician physician : physicians) {
					if (physician.isSuitable(neededResourceType)) {
						if (physician.isAvailable(from, to)) {
							suitablePhysicians.add(physician);
							foundResource = true;
						}
					}
				}*/
				resources.put(neededResourceType, suitablePhysicians);

			} else if (neededResourceType instanceof NurseType) {
				List<Nurse> nurses = nurseDao.getAll();
				List<Resource> suitableNurses = findSuitableResources(nurses, neededResourceType, from, to);
				if (suitableNurses.isEmpty()) {
					return null;		
				}
				resources.put(neededResourceType, suitableNurses);
/*				for (Nurse nurse : nurses) {
					if (nurse.isSuitable(neededResourceType)) {
						if (nurse.isAvailable(from, to)) {
							resources.add(nurse);
							foundResource = true;
						}
					}
				}*/
			} else if (neededResourceType instanceof RoomType) {
				List<Room> rooms = roomDao.getAll();
				List<Resource> suitableRooms = findSuitableResources(rooms, neededResourceType, from, to);
				if (suitableRooms.isEmpty()) {
					return null;		
				}
				resources.put(neededResourceType, suitableRooms);
/*				for (Room room : rooms) {
					if (room.isSuitable(neededResourceType)) {
						if (room.isAvailable(from, to)) {
							resources.add(room);
							foundResource = true;
						}
					}
				}*/
			}
/*			if (!foundResource) {
				return null;
			}
			foundResource = false;*/
		}
		return resources;
	}

	/*
	 * public Physician findPhysician(Date from, Date to, TreatmentType
	 * treatmentType) { PhysicianDao physicianDao = PhysicianDao.getInstance();
	 * List<Physician> physicians = physicianDao.getAll();
	 * 
	 * PhysicianType requiredPhysicianType = null;
	 */

	/*
	 * for (ResourceType resourceType : treatmentType.resourceTypes) { if
	 * (resourceType instanceof PhysicianType) { requiredPhysicianType =
	 * (PhysicianType)resourceType; } }
	 * 
	 * for (Physician physician : physicians) { if
	 * (physician.isCompetent(requiredPhysicianType)) { if
	 * (physician.isAvailable(from, to)) { return physician; } } }
	 * 
	 * return null; }
	 * 
	 * public Nurse findNurse() { NurseDao nurseDao = NurseDao.getInstance();
	 * List<Nurse> nurses = nurseDao.getAll();
	 * 
	 * if (!nurses.isEmpty()) { return nurses.get(0); } return null; }
	 * 
	 * public Room findRoom() { RoomDao roomDao = RoomDao.getInstance();
	 * List<Room> rooms = roomDao.getAll();
	 * 
	 * if (!rooms.isEmpty()) { return rooms.get(0); } return null; }
	 */

}
