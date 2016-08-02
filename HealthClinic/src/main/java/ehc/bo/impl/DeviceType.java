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
@Table(name = "device_type")
@PrimaryKeyJoinColumn(name="id") 
public class DeviceType extends ResourceType {
	/*private List<TreatmentType> possibleTreatmentTypes = new ArrayList<TreatmentType>();*/
	private String name;
	
	protected DeviceType() {
		super();
	}
	
	public DeviceType(User executor, String name) {
		super(executor);
		this.name = name;
	}

/*	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "device_type_treatment_type", joinColumns = {@JoinColumn(name = "device_type_id")},
	inverseJoinColumns = {@JoinColumn(name = "treatment_type_id")})
	public List<TreatmentType> getPossibleTreatmentTypes() {
		return possibleTreatmentTypes;
	}

	public void setPossibleTreatmentTypes(List<TreatmentType> possibleTreatmentTypes) {
		this.possibleTreatmentTypes = possibleTreatmentTypes;
	}*/
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

/*	public void addPossibleTreatmentType(TreatmentType treatmentType) {
		getPossibleTreatmentTypes().add(treatmentType);
	}
	
	public boolean containsTreatmentType(TreatmentType treatmentType) {		
		return getPossibleTreatmentTypes().contains(treatmentType);
	}*/

}
