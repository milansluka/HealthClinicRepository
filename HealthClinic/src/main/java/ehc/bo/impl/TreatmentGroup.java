package ehc.bo.impl;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "treatmentgroup")
public class TreatmentGroup extends ModifiableObject {
	private String name;
	private List<TreatmentType> treatmentTypes = new ArrayList<TreatmentType>();
	
	protected TreatmentGroup() {
		super();
	}
	
	public TreatmentGroup(User executor, String name) {
		super(executor);
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(mappedBy = "treatmentGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<TreatmentType> getTreatmentTypes() {
		return treatmentTypes;
	}
	public void setTreatmentTypes(List<TreatmentType> treatmentTypes) {
		this.treatmentTypes = treatmentTypes;
	}
	
	public void addTreatmentType(TreatmentType treatmentType) {
		if (treatmentType == null) {
			return;
		}
		getTreatmentTypes().add(treatmentType);
		treatmentType.setTreatmentGroup(this);
	}	

}
