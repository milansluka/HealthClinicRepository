package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentSchedulingInfo implements Comparable<AppointmentSchedulingInfo> {
	private Date from;
	private Date to;
	List<TreatmentType> treatmentTypes = new ArrayList<TreatmentType>();
	
	public AppointmentSchedulingInfo(Date from, Date to, List<TreatmentType> treatmentTypes) {
		super();
		this.from = from;
		this.to = to;
		this.treatmentTypes = treatmentTypes;
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
	public List<TreatmentType> getTreatmentTypes() {
		return treatmentTypes;
	}
	public void setTreatmentTypes(List<TreatmentType> treatmentTypes) {
		this.treatmentTypes = treatmentTypes;
	}
	public int getDurationInSeconds() {
		return (int) (getTo().getTime() - getFrom().getTime()) / 1000;
	}

	@Override
	public int compareTo(AppointmentSchedulingInfo o) {
		int result = getFrom().compareTo(o.getFrom());
		if (result != 0) {
			return result;
		}
		return getDurationInSeconds()-o.getDurationInSeconds();
	}
	
}
