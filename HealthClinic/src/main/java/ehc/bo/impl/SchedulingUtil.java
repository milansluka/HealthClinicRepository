package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import ehc.bo.Resource;
import ehc.util.DateUtil;

public class SchedulingUtil {
	protected PhysicianDao physicianDao = PhysicianDao.getInstance();
	protected NurseDao nurseDao = NurseDao.getInstance();
	protected DeviceDao deviceDao = DeviceDao.getInstance();
	protected AppointmentDao appointmentDao = AppointmentDao.getInstance();

	public SchedulingUtil() {

	}


	public boolean isConflict(Appointment appointment, Date from, Date to, List<TreatmentType> treatmentTypes) {
		List<Resource> resources = new ArrayList<Resource>();
		List<Physician> physicians = physicianDao.getAll();
		List<Nurse> nurses = nurseDao.getAll();
		List<Device> devices = deviceDao.getAll();
		resources.addAll(physicians);
		resources.addAll(nurses);
		resources.addAll(devices);
		List<ResourceType> neededResourceTypes = treatmentTypes.get(0).getResourceTypes();		
		return !areNeededResourcesAvailable(resources, neededResourceTypes, from, to, appointment);
	}

	public Set<Appointment> getConflictingAppointments(Date from, Date to, List<TreatmentType> treatmentTypes) {
		List<Resource> resources = new ArrayList<Resource>();
		List<Physician> physicians = physicianDao.getAll();
		List<Nurse> nurses = nurseDao.getAll();
		List<Device> devices = deviceDao.getAll();
		resources.addAll(physicians);
		resources.addAll(nurses);
		resources.addAll(devices);

		List<ResourceType> neededResourceTypes = treatmentTypes.get(0).getResourceTypes();
		Set<Appointment> conflictingAppointments = new HashSet<Appointment>();

		if (areNeededResourcesAvailable(resources, neededResourceTypes, from, to)) {
			return conflictingAppointments;
		}

		List<Appointment> appointments = appointmentDao.getAllIntersecting(from, to);
		List<Resource> suitableResources = findSuitableResources(resources, neededResourceTypes, from, to);

		for (Appointment appointment : appointments) {
			for (Resource resource : suitableResources) {
				if (hasResource(appointment, resource)) {
					conflictingAppointments.add(appointment);
				}
			}
		}
		return conflictingAppointments;
	}
	
	
	public Set<Appointment> getConflictingAppointmentsStartingFrom(Date start, Date from, Date to, List<TreatmentType> treatmentTypes) {
		List<Resource> resources = new ArrayList<Resource>();
		List<Physician> physicians = physicianDao.getAll();
		List<Nurse> nurses = nurseDao.getAll();
		List<Device> devices = deviceDao.getAll();
		resources.addAll(physicians);
		resources.addAll(nurses);
		resources.addAll(devices);

		List<ResourceType> neededResourceTypes = treatmentTypes.get(0).getResourceTypes();
		Set<Appointment> conflictingAppointments = new HashSet<Appointment>();

		if (areNeededResourcesAvailable(resources, neededResourceTypes, from, to)) {
			return conflictingAppointments;
		}

		List<Appointment> appointments = appointmentDao.getAllIntersectingFrom(start, from, to);
		List<Resource> suitableResources = findSuitableResources(resources, neededResourceTypes, from, to);

		for (Appointment appointment : appointments) {
			for (Resource resource : suitableResources) {
				if (hasResource(appointment, resource)) {
					conflictingAppointments.add(appointment);
				}
			}
		}
		return conflictingAppointments;
	}

	protected Map<ResourceType, SortedSet<Resource>> getSuitableResources(Date from, Date to,
			List<TreatmentType> treatmentTypes) {
		Map<ResourceType, SortedSet<Resource>> resources = new HashMap<ResourceType, SortedSet<Resource>>();
		List<Physician> physicians = physicianDao.getAll();
		List<Nurse> nurses = nurseDao.getAll();
		List<Device> devices = deviceDao.getAll();

		List<ResourceType> neededResourceTypes = treatmentTypes.get(0).getResourceTypes();

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
				SortedSet<Resource> suitableRooms = findSuitableRooms(treatmentTypes, from, to);
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

	protected Map<ResourceType, SortedSet<Resource>> getAvailableResources(Date from, Date to,
			List<TreatmentType> treatmentTypes) {
		Map<ResourceType, SortedSet<Resource>> resources = new HashMap<ResourceType, SortedSet<Resource>>();
		List<Physician> physicians = physicianDao.getAll();
		List<Nurse> nurses = nurseDao.getAll();
		List<Device> devices = deviceDao.getAll();

		List<ResourceType> neededResourceTypes = treatmentTypes.get(0).getResourceTypes();

		for (ResourceType neededResourceType : neededResourceTypes) {
			if (neededResourceType instanceof PhysicianType) {
				SortedSet<Resource> suitablePhysicians = findAvailableResources(physicians, neededResourceType, from,
						to, new PhysicianSuitabilityComparator());
				if (suitablePhysicians.isEmpty()) {
					return null;
				}
				resources.put(neededResourceType, suitablePhysicians);

			} else if (neededResourceType instanceof NurseType) {
				SortedSet<Resource> suitableNurses = findAvailableResources(nurses, neededResourceType, from, to,
						new NurseSuitabilityComparator());
				if (suitableNurses.isEmpty()) {
					return null;
				}
				resources.put(neededResourceType, suitableNurses);

			} else if (neededResourceType instanceof RoomType) {
				SortedSet<Resource> suitableRooms = findSuitableRooms(treatmentTypes, from, to);
				if (suitableRooms.isEmpty()) {
					return null;
				}
				resources.put(neededResourceType, suitableRooms);
			} else if (neededResourceType instanceof DeviceType) {
				SortedSet<Resource> suitableDevices = findAvailableResources(devices, neededResourceType, from, to,
						new DeviceSuitabilityComparator());
				if (suitableDevices.isEmpty()) {
					return null;
				}
				resources.put(neededResourceType, suitableDevices);
			}
		}
		return resources;
	}

	protected SortedSet<Resource> findSuitableRooms(List<TreatmentType> treatmentTypes, Date from, Date to) {
		TreatmentType treatmentType = treatmentTypes.get(0);
		List<RoomType> roomTypes = treatmentType.getPossibleRoomTypes();
		List<RoomType> roomTypesCopy = new ArrayList<RoomType>(roomTypes);

		// find suitable rooms (each such a room that each treatment can be
		// executed in)
		for (int i = 1; i < treatmentTypes.size(); i++) {
			TreatmentType treatmentType2 = treatmentTypes.get(i);
			for (int j = 0; j < roomTypesCopy.size(); j++) {
				if (!roomTypesCopy.get(j).getPossibleTreatmentTypes().contains(treatmentType2)) {
					RoomType roomType = roomTypesCopy.get(j);
					roomTypes.remove(roomType);
				}
			}
		}
		// create ordered set from suitable rooms that are available
		TreeSet<Resource> rooms = new TreeSet(new RoomSuitabilityComparator());
		if (roomTypes.size() > 0) {
			for (RoomType roomType : roomTypes) {
				Room room = roomType.getRoom();
				if (room.isNotBusy(from, to)) {
					rooms.add(roomType.getRoom());
				}
			}
		}
		return rooms;
	}

	private boolean hasResource(Appointment appointment, Resource resource) {
		for (Resource appointmentResource : appointment.getResources()) {
			if (appointmentResource.equals(resource)) {
				return true;
			}
		}
		return false;
	}

	protected SortedSet<Resource> findAvailableResources(List<? extends ResourceImpl> resources,
			ResourceType resourceType, Date from, Date to, Comparator<? extends ResourceImpl> comparator) {
		SortedSet<Resource> suitableResources = new TreeSet(comparator);

		for (Resource resource : resources) {
			if (resource.isSuitable(resourceType)) {
				if (resource.isNotBusy(from, to)) {
					suitableResources.add(resource);
				}
			}
		}
		return suitableResources;
	}

	protected List<Resource> findSuitableResources(List<? extends Resource> resources, List<ResourceType> resourceTypes,
			Date from, Date to) {
		List<Resource> suitableResources = new ArrayList<Resource>();

		for (ResourceType resourceType : resourceTypes) {
			for (Resource resource : resources) {
				if (resource.isSuitable(resourceType)) {
					suitableResources.add(resource);
				}
			}
		}
		return suitableResources;
	}

	protected SortedSet<Resource> findSuitableResources(List<? extends ResourceImpl> resources,
			List<ResourceType> resourceTypes, Date from, Date to, Comparator<? extends ResourceImpl> comparator) {
		SortedSet<Resource> suitableResources = new TreeSet(comparator);

		for (ResourceType resourceType : resourceTypes) {
			for (Resource resource : resources) {
				if (resource.isSuitable(resourceType)) {
					suitableResources.add(resource);
				}
			}
		}
		return suitableResources;
	}

	protected boolean areNeededResourcesAvailable(List<? extends Resource> resources, List<ResourceType> resourceTypes,
			Date from, Date to) {
		boolean wasFound = false;
		for (ResourceType resourceType : resourceTypes) {
			for (Resource resource : resources) {
				if (resource.isSuitable(resourceType)) {
					if (resource.isNotBusy(from, to)) {
						wasFound = true;
					}
				}
			}
			if (!wasFound) {
				return false;
			}
			wasFound = false;
		}
		return true;
	}
	
	protected boolean areNeededResourcesAvailable(List<? extends Resource> resources, List<ResourceType> resourceTypes,
			Date from, Date to, Appointment appointment) {
		boolean wasFound = false;
		for (ResourceType resourceType : resourceTypes) {
			for (Resource resource : resources) {
				if (resource.isSuitable(resourceType)) {
					if (!hasResource(appointment, resource)) {
						wasFound = true;			
					}
				}
			}
			if (!wasFound) {
				return false;
			}
			wasFound = false;
		}
		return true;
	}

	protected SortedSet<Resource> findSuitableResources(List<? extends ResourceImpl> resources,
			ResourceType resourceType, Date from, Date to, Comparator<? extends ResourceImpl> comparator) {
		SortedSet<Resource> suitableResources = new TreeSet(comparator);

		for (Resource resource : resources) {
			if (resource.isSuitable(resourceType)) {
				suitableResources.add(resource);
			}
		}
		return suitableResources;
	}

	public long getAppointmentDurationInSeconds(Date from, Date to) {
		return (to.getTime() - from.getTime()) / 1000;
	}
	
	public long getDifferenceInSeconds(Date date1, Date date2) {
		return Math.abs(date1.getTime() - date2.getTime()) / 1000;
	}

	protected Date moveFromIfOutOfWorkTime(Date from, Date to, WorkTime workTime) {
		Date startWorkTime = workTime.getStartWorkTime(from);
		Date endWorkTime = workTime.getEndWorkTime(to);
		int duration = (int) getAppointmentDurationInSeconds(from, to);

		if (from.before(startWorkTime)) {
			return startWorkTime;
		} else if (to.after(endWorkTime)) {
			Date nextDay = DateUtil.addDays(from, 1);
			Date nextFrom = workTime.getStartWorkTime(nextDay);
			Date nextTo = DateUtil.addSeconds(nextFrom, duration);

			return moveFromIfOutOfWorkTime(nextFrom, nextTo, workTime);
		}
		return from;
	}
}
