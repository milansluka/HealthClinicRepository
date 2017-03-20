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
import ehc.bo.SchedulingPolicy;
import ehc.util.DateUtil;

public class BasicPolicy extends SchedulingPolicy {

	@Override
	public List<AppointmentProposal> getAppointmentProposals(AppointmentRequest request,
			SchedulingCustomisation custom) {
		List<AppointmentProposal> appointmentProposals = new ArrayList<AppointmentProposal>();
		if (request == null) {
			return appointmentProposals;
		}
		long appointmentDuration = 0;
		if (custom != null) {
			if (custom.isCustomAppointmentLength()) {
				appointmentDuration = custom.getCustomAppointmentLengthInMinutes() * 60;
			}

		} else {
			appointmentDuration = request.calculateAppointmentLengthInMinutes() * 60;
		}

		Date from = (Date) request.getFrom().clone();
		Date to = DateUtil.addSeconds(from, (int) appointmentDuration);
		int proposedAppointments = 0;
		while (proposedAppointments < custom.getMaxCountOfFoundAppointmentProposals() && to.before(request.getTo())) {
			Map<ResourceType, SortedSet<Resource>> resources = getResources(from, to, request.getTreatmentTypes());

			if (resources != null) {
				AppointmentProposal appointmentProposal = new AppointmentProposal(resources, request.getTreatmentTypes(), from, to);
				appointmentProposals.add(appointmentProposal);
				proposedAppointments++;
			}

			from = DateUtil.addSeconds(from, custom.getTimeGridInMinutes() * 60);
			to = DateUtil.addSeconds(to, custom.getTimeGridInMinutes() * 60);
			from = moveFromIfOutOfWorkTime(from, to, custom.getWorkTime());
			to = DateUtil.addSeconds(from, (int) appointmentDuration);
		}

		return appointmentProposals;
	}
	
	
	
	private Map<ResourceType, SortedSet<Resource>> getResources(Date from, Date to,
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
	
	private SortedSet<Resource> findSuitableRooms(List<TreatmentType> treatmentTypes, Date from, Date to) {
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
	
	private SortedSet<Resource> findSuitableResources(List<? extends ResourceImpl> resources, ResourceType resourceType,
			Date from, Date to, Comparator<? extends ResourceImpl> comparator) {
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
	
	
	private long getAppointmentDuration(Date from, Date to) {
		return (to.getTime() - from.getTime()) / 1000;
	}
	
	private Date moveFromIfOutOfWorkTime(Date from, Date to, WorkTime workTime) {
		Date startWorkTime = workTime.getStartWorkTime(from);
		Date endWorkTime = workTime.getEndWorkTime(to);
		int duration = (int) getAppointmentDuration(from, to);

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
	
	
	
	

	/*
	 * @Override public List<AppointmentProposal>
	 * getAppointmentProposals(AppointmentRequest request) {
	 * List<AppointmentProposal> appointmentProposals = new
	 * ArrayList<AppointmentProposal>(); if (request == null) { return
	 * appointmentProposals; }
	 * 
	 * long appointmentDuration =
	 * request.calculateAppointmentLengthInMinutes()*60; Date from =
	 * (Date)request.getFrom().clone(); Date to = DateUtil.addSeconds(from,
	 * (int) appointmentDuration); int proposedAppointments = 0; while
	 * (proposedAppointments < HealthPoint.MAX_NUMBER_OF_APPOINTMENT_PROPOSALS
	 * && to.before(request.getTo())) { Map<ResourceType, SortedSet<Resource>>
	 * resources = getResources(from, to, request.getTreatmentTypes());
	 * 
	 * if (resources != null) { AppointmentProposal appointmentProposal = new
	 * AppointmentProposal(resources, request.getTreatmentTypes(), from, to);
	 * appointmentProposals.add(appointmentProposal); proposedAppointments++; }
	 * 
	 * from = DateUtil.addSeconds(from, timeGridInMinutes * 60); to =
	 * DateUtil.addSeconds(to, timeGridInMinutes * 60); from =
	 * moveFromIfOutOfWorkTime(from, to); to = DateUtil.addSeconds(from, (int)
	 * appointmentDuration); } return appointmentProposals; }
	 */

}
