package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "resource_type")
@Inheritance(strategy=InheritanceType.JOINED)  
public class ResourceType extends ModifiableObject {
	List<TreatmentType> treatmentTypes;
	
	public ResourceType() {
		super();
	}
	
	public ResourceType(User executor) {
		super(executor);
		treatmentTypes = new ArrayList<>();
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "resourceTypes")
	public List<TreatmentType> getTreatmentTypes() {
		return treatmentTypes;
	}

	public void setTreatmentTypes(List<TreatmentType> treatmentTypes) {
		this.treatmentTypes = treatmentTypes;
	}
	
	public void addTreatmentType(TreatmentType treatmentType) {
		getTreatmentTypes().add(treatmentType);
	}
	
}
