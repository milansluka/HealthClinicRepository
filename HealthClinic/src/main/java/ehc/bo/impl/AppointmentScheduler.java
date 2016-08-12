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

public class AppointmentScheduler {
	public static final String TREATMENTS_CANNOT_BE_JOINED_MSG = "Treatments can not be planned in one appointment, it is recommended to create more appointments";
	public static final String INSUFFICIENT_DIFFERENCE_BETWEEN_FROM_AND_TO_MSG = "Treatment type duration is longer than set duration";

	private PhysicianDao physicianDao = PhysicianDao.getInstance();
	private NurseDao nurseDao = NurseDao.getInstance();
	private DeviceDao deviceDao = DeviceDao.getInstance();
	private WorkTime workTime;
	private int timeGridInMinutes;
	private String message;

	public AppointmentScheduler(WorkTime workTime, int timeGridInMinutes) {
		super();
		this.workTime = workTime;
		this.timeGridInMinutes = timeGridInMinutes;
	}

	public Appointment getAppointment(User executor, AppointmentScheduleData appointmentScheduleData,
			List<TreatmentType> treatmentTypes, Individual individual) {
		Appointment appointment = new Appointment(executor, appointmentScheduleData.getFrom(),
				appointmentScheduleData.getTo(), individual);
		appointment.setResources(appointmentScheduleData.getResources());
		appointment.setTreatmentTypes(treatmentTypes);
		return appointment;
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

	private boolean sufficientAppointmentDuration(Date from, Date to, List<TreatmentType> treatmentTypes) {
		long appointmentDuration = getAppointmentDuration(from, to);
		long treatmentDuration = 0;

		for (TreatmentType treatmentType : treatmentTypes) {
			treatmentDuration += treatmentType.getDuration();
		}

		return appointmentDuration >= treatmentDuration;
	}

	private long getAppointmentDuration(Date from, Date to) {
		return (to.getTime() - from.getTime()) / 1000;
	}

	private boolean areResourcesAvailable(Date from, Date to, List<Resource> resources) {
		for (Resource resource : resources) {
			if (!resource.isAvailable(from, to)) {
				return false;
			}
		}
		return true;
	}

	public AppointmentScheduleData getAppointmentScheduleData(Date from, Date to, List<TreatmentType> treatmentTypes,
			List<Resource> resources) {
		from = moveFromIfOutOfWorkTime(from, to);
		to = DateUtil.addSeconds(from, (int) getAppointmentDuration(from, to));
		if (areResourcesAvailable(from, to, resources)) {
			return new AppointmentScheduleData(from, to, resources);
		}
		return null;
	}

	public List<AppointmentScheduleData> getAppointmentScheduleData(Date from, Date to,
			List<TreatmentType> treatmentTypes, List<Resource> resources, int count) {
		from = moveFromIfOutOfWorkTime(from, to);
		to = DateUtil.addSeconds(from, (int) getAppointmentDuration(from, to));
		List<AppointmentScheduleData> appointmentScheduleDatas = new ArrayList<>();
		int givenAppointmentScheduleData = 0;
		while (givenAppointmentScheduleData < count) {
			if (areResourcesAvailable(from, to, resources)) {
				appointmentScheduleDatas.add(new AppointmentScheduleData(from, to, resources));
				givenAppointmentScheduleData++;
			}

			from = DateUtil.addSeconds(from, timeGridInMinutes * 60);
			to = DateUtil.addSeconds(to, timeGridInMinutes * 60);
		}

		return appointmentScheduleDatas;
	}

	public AppointmentProposal getAppointmentProposal(Date from, Date to, List<TreatmentType> treatmentTypes) {
		if (!canBeCombinedIntoOneAppointment(treatmentTypes)) {
			
			return null;
		}

		if (!sufficientAppointmentDuration(from, to, treatmentTypes)) {
			return null;
		}

		from = moveFromIfOutOfWorkTime(from, to);
		to = DateUtil.addSeconds(from, (int) getAppointmentDuration(from, to));
		Map<ResourceType, SortedSet<Resource>> resources = getResources(from, to,
				treatmentTypes);

		if (resources != null) {
			return new AppointmentProposal(resources, treatmentTypes, from, to);
		}
		return null;
	}

	public List<AppointmentProposal> getAppointmentProposals(Date from, Date to, List<TreatmentType> treatmentTypes,
			int count) {
		if (!canBeCombinedIntoOneAppointment(treatmentTypes)) {
			setMessage(TREATMENTS_CANNOT_BE_JOINED_MSG);
			return null;
		}

		if (!sufficientAppointmentDuration(from, to, treatmentTypes)) {
			setMessage(INSUFFICIENT_DIFFERENCE_BETWEEN_FROM_AND_TO_MSG);
			return null;
		}

		from = moveFromIfOutOfWorkTime(from, to);
		to = DateUtil.addSeconds(from, (int) getAppointmentDuration(from, to));

		int proposedAppointments = 0;
		List<AppointmentProposal> appointmentProposals = new ArrayList<>();

		while (proposedAppointments < count) {
			Map<ResourceType, SortedSet<Resource>> resources = getResources(from, to,
					treatmentTypes);

			if (resources != null) {
				AppointmentProposal appointmentProposal = new AppointmentProposal(resources, treatmentTypes, from, to);
				appointmentProposals.add(appointmentProposal);
				proposedAppointments++;
			}

			from = DateUtil.addSeconds(from, timeGridInMinutes * 60);
			to = DateUtil.addSeconds(to, timeGridInMinutes * 60);
		}

		return appointmentProposals;
	}
	
	private SortedSet<Resource> findSuitableRooms(List<TreatmentType> treatmentTypes, Date from, Date to) {
		TreatmentType treatmentType = treatmentTypes.get(0);
		List<RoomType> roomTypes = treatmentType.getPossibleRoomTypes();
        List<RoomType> roomTypesCopy = new ArrayList<>(roomTypes);
		for (int i = 1; i < treatmentTypes.size(); i++) {
			TreatmentType treatmentType2 = treatmentTypes.get(i);
			for (int j = 0; j < roomTypesCopy.size(); j++) {
				if (!roomTypesCopy.get(j).getPossibleTreatmentTypes().contains(treatmentType2)) {
					RoomType roomType = roomTypesCopy.get(j);
					roomTypes.remove(roomType);
				}
			}
		}		
		TreeSet<Resource> rooms = new TreeSet(new RoomSuitabilityComparator()); 		
		if (roomTypes.size() > 0) {
			for (RoomType roomType : roomTypes) {
				rooms.add(roomType.getRoom());
			}
		}	
		return rooms;
	}
	
	private void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public boolean canBeCombinedIntoOneAppointment(List<TreatmentType> treatmentTypes) {
		if (treatmentTypes.size() < 2) {
			return true;
		}
		List<ResourceType> resourceTypes = treatmentTypes.get(0).getResourceTypes();
		for (int i = 1; i < treatmentTypes.size(); i++) {
			TreatmentType treatmentType = treatmentTypes.get(i);
			if (resourceTypes.size() != treatmentType.getResourceTypes().size()) {
				return false;
			} else {
				boolean areTheSame = resourceTypes.containsAll(treatmentType.getResourceTypes());
				if (!areTheSame) {
					return false;
				}
			}
		}
		return true;
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

	private Map<ResourceType, SortedSet<Resource>> getResources(Date from, Date to,
			List<TreatmentType> treatmentTypes) {
		Map<ResourceType, SortedSet<Resource>> resources = new HashMap<>();
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
}
