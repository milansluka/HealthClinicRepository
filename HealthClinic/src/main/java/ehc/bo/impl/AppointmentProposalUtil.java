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

public class AppointmentProposalUtil {
	private PhysicianDao physicianDao = PhysicianDao.getInstance();
	private NurseDao nurseDao = NurseDao.getInstance();
	private RoomDao roomDao = RoomDao.getInstance();
	private DeviceDao deviceDao = DeviceDao.getInstance();
	private WorkTime workTime;

/*	public AppointmentProposalUtil() {

	}*/

	public AppointmentProposalUtil(WorkTime workTime) {
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
	
/*	private boolean sufficientAppointmentDuration(Date from, Date to, TreatmentType treatmentType) {
		long appointmentDuration = (to.getTime() - from.getTime()) / 1000;
		
		return appointmentDuration >= treatmentType.getDuration();		
	}*/
	
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
	
/*	public AppointmentProposal getAppointmentProposal(Date from, Date to, List<TreatmentType> treatmentTypes, List<Resource> resources) {
		from = moveFromIfOutOfWorkTime(from, to);
		to = DateUtil.addSeconds(from, (int) getAppointmentDuration(from, to));
		if (areResourcesAvailable(from, to, resources)) {
			Map<ResourceType, SortedSet<Resource>> sortedResources = new HashMap<>();
		}
		return null;
	}
	
	public AppointmentProposal getAppointmentProposal(Date from, Date to, List<TreatmentType> treatmentTypes, List<ResourceType> resourceTypes) {
		from = moveFromIfOutOfWorkTime(from, to);
		to = DateUtil.addSeconds(from, (int) getAppointmentDuration(from, to));	
		
		
	}*/
	
	public AppointmentProposal getAppointmentProposal(Date from, Date to, List<TreatmentType> treatmentTypes) {
		if (!canBeJoined(treatmentTypes)) {
			return null;	
		}
		
		if (!sufficientAppointmentDuration(from, to, treatmentTypes)) {
			return null;		
		}
		
		from = moveFromIfOutOfWorkTime(from, to);
		to = DateUtil.addSeconds(from, (int) getAppointmentDuration(from, to));
		Map<ResourceType, SortedSet<Resource>> resources = getResources(from, to, treatmentTypes.get(0).getResourceTypes());
		
		if (resources != null) {
			return new AppointmentProposal(resources, treatmentTypes, from, to);
		}
		return null;	
	}
	
	public List<AppointmentProposal> getAppointmentProposals(Date from, Date to, List<TreatmentType> treatmentTypes, int count) {
		if (!canBeJoined(treatmentTypes)) {
			return null;	
		}
		
		if (!sufficientAppointmentDuration(from, to, treatmentTypes)) {
			return null;		
		}
		
		from = moveFromIfOutOfWorkTime(from, to);
		to = DateUtil.addSeconds(from, (int) getAppointmentDuration(from, to));
		
		int proposedAppointments = 0;
		int grid = 30 * 60;
		List<AppointmentProposal> appointmentProposals = new ArrayList<>();
		
		while (proposedAppointments < count) {
			Map<ResourceType, SortedSet<Resource>> resources = getResources(from, to, treatmentTypes.get(0).getResourceTypes());

			if (resources != null) {
				AppointmentProposal appointmentProposal = new AppointmentProposal(resources, treatmentTypes, from, to);
				appointmentProposals.add(appointmentProposal);
				proposedAppointments++;
			}

			from = DateUtil.addSeconds(from, grid);
			to = DateUtil.addSeconds(to, grid);
			
		}
		
		return appointmentProposals;	
	}
	
/*	public List<AppointmentProposal> getAppointmentProposalsForMoreTreatments(Date from, Date to, List<TreatmentType> treatmentTypes, int count) {
		List<AppointmentProposal> appointmentProposals = new ArrayList<>();
		List<AppointmentProposal> appointmentProposalsAfterMerging = new ArrayList<>();
		
		if (treatmentTypes.size() < 2) {
			return getAppointmentProposals(from, to, treatmentTypes.get(0), count);	
		}
		
		for (TreatmentType treatmentType : treatmentTypes) {
			appointmentProposals.addAll(getAppointmentProposals(from, to, treatmentType, count));		
		}
		
		for (int i = 0; i < appointmentProposals.size() - 1; i++) {
			AppointmentProposal appointmentProposal = appointmentProposals.get(i);
			AppointmentProposal appointmentProposal2 = appointmentProposals.get(i+1);
			if (canMerge(appointmentProposal, appointmentProposal2)) {
				appointmentProposal.addTreatmentTypes(appointmentProposal2.getTreatmentTypes());
				appointmentProposals.remove(appointmentProposal2);
			}
			appointmentProposalsAfterMerging.add(appointmentProposal);
		}
		return appointmentProposalsAfterMerging;
	}*/
	
	
	private boolean canBeJoined(List<TreatmentType> treatmentTypes) {
		if (treatmentTypes.size() < 2) {
			return true;
		}
		return true;
	}
	
/*	public List<AppointmentProposal> getAppointmentProposals(Date from, Date to, TreatmentType treatmentType,
			int count) {
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

		 Date to = DateUtil.addSeconds(from, treatmentDuration + margin); 

		//getSuitableResources
		
		while (proposedAppointments < count) {

			//getAvailableResources
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
	}*/

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
