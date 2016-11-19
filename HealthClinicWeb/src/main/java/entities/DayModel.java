package entities;

import java.util.ArrayList;
import java.util.List;

public class DayModel {
	private String name;
	private List<RoomLane> roomLanes;
	private RoomLane roomLane;
	private int roomLaneIndex;
		
	public DayModel(String name, List<String> roomNames) {
		super();
		this.name = name;
		initRoomLanes(roomNames);
	}
	
    public int getRoomLaneIndex() {
		return roomLaneIndex;
	}

	public void setRoomLaneIndex(int roomLaneIndex) {
		this.roomLaneIndex = roomLaneIndex;
	}

	public void initRoomLanes(List<String> roomNames) {
    	roomLanes = new ArrayList<RoomLane>(roomNames.size());
    	
    	for (int i = 0; i < roomNames.size(); i++) {
    		roomLanes.add(new RoomLane(roomNames.get(i)));
    	} 	
    }

	public void addRoomLane(RoomLane roomLane) {
		roomLanes.add(roomLane);
	}
	
/*	public void addTimeWindowModel(TimeWindowModel timeWindowModel) {
		for (RoomLane roomLane : roomLanes) {
			if (roomLane.getName().equals(timeWindowModel.getRoomName())) {
				roomLane.addTimeWindowModel(timeWindowModel);
				return;
			}
		}
	}*/
	
	public void addAppointmentModel(AppointmentModel appointmentModel) {
		for (RoomLane roomLane : roomLanes) {
			if (roomLane.getName().equals(appointmentModel.getRoomName())) {
				roomLane.addAppointmentModel(appointmentModel);
				return;
			}
		}
	}
	
	public RoomLane getRoomLane() {
		return roomLane;
	}

	public void setRoomLane(RoomLane roomLane) {
		this.roomLane = roomLane;
	}

	public List<RoomLane> getRoomLanes() {
		return roomLanes;
	}

	public void setRoomLanes(List<RoomLane> roomLanes) {
		this.roomLanes = roomLanes;
	}
}
