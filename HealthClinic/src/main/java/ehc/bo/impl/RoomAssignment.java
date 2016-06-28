package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "room_assignment")
public class RoomAssignment extends BaseObject {
	TreatmentType treatmentType;
	Room room;
	
	@ManyToOne
	@JoinColumn(name = "treatment_type_id")
	public TreatmentType getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	public Room getRoom() {
		return room;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
}
