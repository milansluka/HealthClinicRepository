package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name="id") 
public class DeviceType extends ResourceType {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DeviceType)) {
			return false;
		}
		DeviceType deviceType = (DeviceType)obj;
		return getName().equals(deviceType.getName());
	}
	
/*	public void addPossibleTreatmentType(TreatmentType treatmentType) {
		getPossibleTreatmentTypes().add(treatmentType);
	}
	
	public boolean containsTreatmentType(TreatmentType treatmentType) {		
		return getPossibleTreatmentTypes().contains(treatmentType);
	}*/

}
