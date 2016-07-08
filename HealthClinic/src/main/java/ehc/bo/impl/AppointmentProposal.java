package ehc.bo.impl;

import java.util.Date;
import java.util.List;

import ehc.bo.Resource;

public class AppointmentProposal {
	List<Resource> resources;
    TreatmentType treatmentType;
    Date from;
    Date to;
     
	public AppointmentProposal(List<Resource> resources, TreatmentType treatmentType, Date from, Date to) {
		super();
		this.resources = resources;
		this.treatmentType = treatmentType;
		this.from = from;
		this.to = to;
	}
	
	public List<Resource> getResources() {
		return resources;
	}
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	public TreatmentType getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}
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
    
    
}
