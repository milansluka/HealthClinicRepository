package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)  
public class ResourceType extends ModifiableObject {
	private TreatmentType treatmentType;
	
	public ResourceType() {
		super();
	}
	
	public ResourceType(User executor) {
		super(executor);
	}
	
	@ManyToOne
	@JoinColumn(name = "treatmenttype")
	public TreatmentType getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}	
}
