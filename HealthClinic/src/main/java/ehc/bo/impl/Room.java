package ehc.bo.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Room extends ModifiableObject {
	/*List<Device> devices;*/
	List<RoomAssignment> roomAssignments;
    String name;
      
	protected Room() {
		super();
	}
	
	public Room(User executor, String name) {
		super(executor);
		this.name = name;
	}
	
/*	@OneToMany
	public List<Device> getDevices() {
		return devices;
	}
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}*/
	
	@OneToMany(mappedBy = "room")
	public List<RoomAssignment> getRoomAssignments() {
		return roomAssignments;
	}

	public void setRoomAssignments(List<RoomAssignment> roomAssignments) {
		this.roomAssignments = roomAssignments;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    
}
