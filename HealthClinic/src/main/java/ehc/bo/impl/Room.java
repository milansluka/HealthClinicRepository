package ehc.bo.impl;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Room extends ModifiableObject {
	/*List<Device> devices;*/
/*	List<RoomAssignment> roomAssignments;*/
    String name;
    RoomType type;
    List<Appointment> appointments;
      
	protected Room() {
		super();
	}
	
	public Room(User executor, RoomType roomType, String name) {
		super(executor);
		this.name = name;
		this.type = roomType;
	}
	
/*	@OneToMany
	public List<Device> getDevices() {
		return devices;
	}
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}*/
	
/*	@OneToMany(mappedBy = "room")
	public List<RoomAssignment> getRoomAssignments() {
		return roomAssignments;
	}

	public void setRoomAssignments(List<RoomAssignment> roomAssignments) {
		this.roomAssignments = roomAssignments;
	}*/
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "room_type_id")
	public RoomType getType() {
		return type;
	}

	public void setType(RoomType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "room")
	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
	
	public void addAppointment(Appointment appointment) {
		getAppointments().add(appointment);
	}
    
    
}
