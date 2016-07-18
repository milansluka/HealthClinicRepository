package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import ehc.bo.Resource;
import ehc.util.DateUtil;

public class ResourcesUtil {
	private PhysicianDao physicianDao = PhysicianDao.getInstance();
	private NurseDao nurseDao = NurseDao.getInstance();
	private RoomDao roomDao = RoomDao.getInstance();
	private WorkTime workTime = new WorkTime();

	public ResourcesUtil() {

	}

	public ResourcesUtil(WorkTime workTime) {
		super();
		this.workTime = workTime;
	}

	private Date moveFromIfOutOfWorkTime(Date from, Date to) {
		Date startWorkTime = workTime.getStartWorkTime(from);
		Date endWorkTime = workTime.getEndWorkTime(to);
		if (from.before(startWorkTime)) {
			return startWorkTime;
		} else if (to.after(endWorkTime)) {
			Date nextDay = DateUtil.addDays(from, 1);
			startWorkTime = workTime.getStartWorkTime(nextDay);
			return startWorkTime;
		}
		return from;
	}

	public List<AppointmentProposal> getAppointmentProposals(Date from, Date to, TreatmentType treatmentType,
			int count) {
		/*
		 * if (DateUtil.addSeconds(from, treatmentType.getDuration()).after(to))
		 * { return null; }
		 */
		long appointmentDuration = (to.getTime() - from.getTime()) / 1000;

		if (appointmentDuration < treatmentType.getDuration()) {
			return null;
		}

		List<AppointmentProposal> appointmentProposals = new ArrayList<>();
		from = moveFromIfOutOfWorkTime(from, to);
		to = DateUtil.addSeconds(from, (int) appointmentDuration);
		int proposedAppointments = 0;
		// in seconds
		int margin = 0;
		// in seconds
		int treatmentDuration = treatmentType.getDuration();
		int grid = 30 * 60;

		/* Date to = DateUtil.addSeconds(from, treatmentDuration + margin); */

		while (proposedAppointments < count) {

			Map<ResourceType, SortedSet<Resource>> resources = getResources(from, to, treatmentType.getResourceTypes());

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

	private SortedSet<Resource> findSuitableResources(List<? extends ResourceImpl> resources, ResourceType resourceType,
			Date from, Date to, Comparator<? extends ResourceImpl> comparator) {
		SortedSet<Resource> suitableResources = new TreeSet(comparator);

		for (Resource resource : resources) {
			if (resource.isSuitable(resourceType)) {
				if (resource.isAvailable(from, to)) {
					suitableResources.add(resource);
				}
			}
		}
		return suitableResources;
	}

	public Map<ResourceType, SortedSet<Resource>> getResources(Date from, Date to, List<ResourceType> neededResourceTypes) {
		/* List<Resource> resources = new ArrayList<>(); */
		Map<ResourceType, SortedSet<Resource>> resources = new HashMap<>();
		boolean foundResource = false;
		List<Physician> physicians = physicianDao.getAll();
		List<Nurse> nurses = nurseDao.getAll();
		List<Room> rooms = roomDao.getAll();

		for (ResourceType neededResourceType : neededResourceTypes) {
			if (neededResourceType instanceof PhysicianType) {
				/* Set<Physician> physicians = physicianDao.getAll(); */
				SortedSet<Resource> suitablePhysicians = findSuitableResources(physicians, neededResourceType, from, to,
						new PhysicianSuitabilityComparator());
				if (suitablePhysicians.isEmpty()) {
					return null;
				}
				resources.put(neededResourceType, suitablePhysicians);

			} else if (neededResourceType instanceof NurseType) {
				/* Set<Nurse> nurses = nurseDao.getAll(); */
				SortedSet<Resource> suitableNurses = findSuitableResources(nurses, neededResourceType, from, to,
						new NurseSuitabilityComparator());
				if (suitableNurses.isEmpty()) {
					return null;
				}
				resources.put(neededResourceType, suitableNurses);
				/*
				 * for (Nurse nurse : nurses) { if
				 * (nurse.isSuitable(neededResourceType)) { if
				 * (nurse.isAvailable(from, to)) { resources.add(nurse);
				 * foundResource = true; } } }
				 */
			} else if (neededResourceType instanceof RoomType) {
				/* Set<Room> rooms = roomDao.getAll(); */
				SortedSet<Resource> suitableRooms = findSuitableResources(rooms, neededResourceType, from, to,
						new RoomSuitabilityComparator());
				if (suitableRooms.isEmpty()) {
					return null;
				}
				resources.put(neededResourceType, suitableRooms);
				/*
				 * for (Room room : rooms) { if
				 * (room.isSuitable(neededResourceType)) { if
				 * (room.isAvailable(from, to)) { resources.add(room);
				 * foundResource = true; } } }
				 */
			}
			/*
			 * if (!foundResource) { return null; } foundResource = false;
			 */
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
