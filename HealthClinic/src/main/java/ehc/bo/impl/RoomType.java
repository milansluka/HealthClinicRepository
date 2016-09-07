package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name="id") 
public class RoomType extends ResourceType {
	List<TreatmentType> possibleTreatmentTypes = new ArrayList<TreatmentType>();
	Room room;
	
	protected RoomType() {
		super();
	}
	
	public RoomType(User executor) {
		super(executor);
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "roomtype_treatmenttype", joinColumns = {@JoinColumn(name = "roomtype")},
	inverseJoinColumns = {@JoinColumn(name = "treatmenttype")})
	public List<TreatmentType> getPossibleTreatmentTypes() {
		return possibleTreatmentTypes;
	}

	public void setPossibleTreatmentTypes(List<TreatmentType> possibleTreatmentTypes) {
		this.possibleTreatmentTypes = possibleTreatmentTypes;
	}
	
	
	@OneToOne(mappedBy = "type")
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void addPossibleTreatmentType(TreatmentType treatmentType) {
		getPossibleTreatmentTypes().add(treatmentType);
	}
	
	public boolean containsTreatmentType(TreatmentType treatmentType) {		
		return getPossibleTreatmentTypes().contains(treatmentType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((possibleTreatmentTypes == null) ? 0 : possibleTreatmentTypes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RoomType)) {
			return false;
		}
		
		RoomType roomType = (RoomType)obj;
		if (roomType.getPossibleTreatmentTypes().size() != getPossibleTreatmentTypes().size()) {
			return false;
		}
		
		return getPossibleTreatmentTypes().containsAll(roomType.getPossibleTreatmentTypes());		
	}	
}
