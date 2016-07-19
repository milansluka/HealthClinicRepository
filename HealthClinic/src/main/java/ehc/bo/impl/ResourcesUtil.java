package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import ehc.bo.Resource;
import ehc.util.DateUtil;

public class ResourcesUtil {
	private PhysicianDao physicianDao = PhysicianDao.getInstance();
	private NurseDao nurseDao = NurseDao.getInstance();
	private RoomDao roomDao = RoomDao.getInstance();
	private DeviceDao deviceDao = DeviceDao.getInstance();
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
		Map<ResourceType, SortedSet<Resource>> resources = new HashMap<>();
		List<Physician> physicians = physicianDao.getAll();
		List<Nurse> nurses = nurseDao.getAll();
		List<Room> rooms = roomDao.getAll();
		List<Device> devices = deviceDao.getAll();

		for (ResourceType neededResourceType : neededResourceTypes) {
			if (neededResourceType instanceof PhysicianType) {
				SortedSet<Resource> suitablePhysicians = findSuitableResources(physicians, neededResourceType, from, to,
						new PhysicianSuitabilityComparator());
				if (suitablePhysicians.isEmpty()) {
					return null;
				}
				resources.put(neededResourceType, suitablePhysicians);

			} else if (neededResourceType instanceof NurseType) {
				SortedSet<Resource> suitableNurses = findSuitableResources(nurses, neededResourceType, from, to,
						new NurseSuitabilityComparator());
				if (suitableNurses.isEmpty()) {
					return null;
				}
				resources.put(neededResourceType, suitableNurses);

			} else if (neededResourceType instanceof RoomType) {
				SortedSet<Resource> suitableRooms = findSuitableResources(rooms, neededResourceType, from, to,
						new RoomSuitabilityComparator());
				if (suitableRooms.isEmpty()) {
					return null;
				}
				resources.put(neededResourceType, suitableRooms);
			} else if (neededResourceType instanceof DeviceType) {
				SortedSet<Resource> suitableDevices = findSuitableResources(devices, neededResourceType, from, to,
						new DeviceSuitabilityComparator());
				if (suitableDevices.isEmpty()) {
					return null;
				}
				resources.put(neededResourceType, suitableDevices);
			}
		}
		return resources;
	}
}
