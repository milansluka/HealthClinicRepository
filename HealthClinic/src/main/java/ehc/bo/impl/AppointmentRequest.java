package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentRequest {
	private List<TreatmentType> treatmentTypes = new ArrayList<TreatmentType>();
	private Individual caller;
	private SchedulingHorizon horizon;
	
	public AppointmentRequest(Individual caller, SchedulingHorizon horizon) {
		super();
		this.caller = caller;
		this.horizon = horizon;
	}
	
	public long calculateAppointmentLengthInMinutes() {
		long treatmentDurationInSeconds = 0;
		for (TreatmentType treatmentType : treatmentTypes) {
			treatmentDurationInSeconds += treatmentType.getDuration();
		}
		return treatmentDurationInSeconds/60;
	}
	
	
	public List<TreatmentType> getTreatmentTypes() {
		return treatmentTypes;
	}
	public void setTreatmentTypes(List<TreatmentType> treatmentTypes) {
		this.treatmentTypes = treatmentTypes;
	}
	
	public Individual getCaller() {
		return caller;
	}

	public void setCaller(Individual caller) {
		this.caller = caller;
	}
	
	public SchedulingHorizon getHorizon() {
		return horizon;
	}

	public void setHorizon(SchedulingHorizon horizon) {
		this.horizon = horizon;
	}

	public Date getFrom() {
		return horizon.getFrom();
	}
	public void setFrom(Date from) {
		horizon.setFrom(from);
	}
	public Date getTo() {
		return horizon.getTo();
	}
	public void setTo(Date to) {
		horizon.setTo(to);
	}
	public void addTreatmentType(TreatmentType treatmentType) {
		if (treatmentType == null) {
			return;
		}
		getTreatmentTypes().add(treatmentType);
	}
}
