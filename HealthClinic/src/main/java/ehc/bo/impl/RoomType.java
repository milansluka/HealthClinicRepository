package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "room_type")
@PrimaryKeyJoinColumn(name="id") 
public class RoomType extends ResourceType {
	List<TreatmentType> possibleTreatmentTypes = new ArrayList<TreatmentType>();
	
	protected RoomType() {
		super();
	}
	
	public RoomType(User executor) {
		super(executor);
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "treatment_type_assignment", joinColumns = {@JoinColumn(name = "room_type_id")},
	inverseJoinColumns = {@JoinColumn(name = "treatment_type_id")})
	public List<TreatmentType> getPossibleTreatmentTypes() {
		return possibleTreatmentTypes;
	}

	public void setPossibleTreatmentTypes(List<TreatmentType> possibleTreatmentTypes) {
		this.possibleTreatmentTypes = possibleTreatmentTypes;
	}
	
	public void addPossibleTreatmentType(TreatmentType treatmentType) {
		getPossibleTreatmentTypes().add(treatmentType);
	}
	
	public boolean containsTreatmentType(TreatmentType treatmentType) {		
		return getPossibleTreatmentTypes().contains(treatmentType);
	}
}
