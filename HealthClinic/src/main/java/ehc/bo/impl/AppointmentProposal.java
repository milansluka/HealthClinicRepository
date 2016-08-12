package ehc.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import ehc.bo.Resource;

public class AppointmentProposal {
	Map<ResourceType, SortedSet<Resource>> resources = new HashMap<>();
    List<TreatmentType> treatmentTypes;
   
    Date from;
    Date to;
     
	
	public AppointmentProposal(Map<ResourceType, SortedSet<Resource>> resources, List<TreatmentType> treatmentTypes, Date from, Date to) {
		super();
		this.resources = resources;
		this.treatmentTypes = treatmentTypes;
		this.from = from;
		this.to = to;
	}
	
	public Map<ResourceType, SortedSet<Resource>> getResources() {
		return resources;
	}
	public void setResources(Map<ResourceType, SortedSet<Resource>> resources) {
		this.resources = resources;
	}
/*	public TreatmentType getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}*/
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	
	public List<TreatmentType> getTreatmentTypes() {
		return treatmentTypes;
	}

	public void setTreatmentTypes(List<TreatmentType> treatmentTypes) {
		this.treatmentTypes = treatmentTypes;
	}

	public void addTreatmentType(TreatmentType treatmentType) {
		getTreatmentTypes().add(treatmentType);		
	}
	
	public void addTreatmentTypes(List<TreatmentType> treatmentTypes) {
		getTreatmentTypes().addAll(treatmentTypes);		
	}  
}
