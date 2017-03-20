package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ehc.bo.Resource;
import ehc.util.DateUtil;

public class SchedulingCustomisation {
	private boolean customAppointmentLength = false;
	private boolean customResources = false;
	
	private int maxCountOfFoundAppointmentProposals = Integer.MAX_VALUE;
	private int customAppointmentLengthInMinutes;
	private int timeGridInMinutes = HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES;
	
	private WorkTime workTime;
    private List<Resource> resources = new ArrayList<Resource>();

	public SchedulingCustomisation(WorkTime workTime) {
		super();
		this.workTime = workTime;
	}
	public int getMaxCountOfFoundAppointmentProposals() {
		return maxCountOfFoundAppointmentProposals;
	}
	public void setMaxCountOfFoundAppointmentProposals(int maxCountOfFoundAppointmentProposals) {
		this.maxCountOfFoundAppointmentProposals = maxCountOfFoundAppointmentProposals;
	}
	public int getCustomAppointmentLengthInMinutes() {
		return customAppointmentLengthInMinutes;
	}
	public void setCustomAppointmentLengthInMinutes(int customAppointmentLengthInMinutes) {
		this.customAppointmentLengthInMinutes = customAppointmentLengthInMinutes;
		customAppointmentLength = true;
	}
	public List<Resource> getResources() {
		return resources;
	}
	public void setResources(List<Resource> resources) {
		this.resources = resources;
		customResources = true;
	}

	public boolean isCustomAppointmentLength() {
		return customAppointmentLength;
	}

	public boolean isCustomResources() {
		return customResources;
	}


	public int getTimeGridInMinutes() {
		return timeGridInMinutes;
	}


	public void setTimeGridInMinutes(int timeGridInMinutes) {
		this.timeGridInMinutes = timeGridInMinutes;
	}
	public WorkTime getWorkTime() {
		return workTime;
	} 
	
	
	
	
		
}
