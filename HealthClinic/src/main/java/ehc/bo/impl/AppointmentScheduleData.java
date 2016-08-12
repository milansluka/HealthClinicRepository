package ehc.bo.impl;

import java.util.Date;
import java.util.List;

import ehc.bo.Resource;

public class AppointmentScheduleData {
	private Date from;
	private Date to;
	private List<Resource> resources;
	
	public AppointmentScheduleData(Date from, Date to, List<Resource> resources) {
		super();
		this.from = from;
		this.to = to;
		this.resources = resources;
	}

	public Date getFrom() {
		return from;
	}

	public Date getTo() {
		return to;
	}

	public List<Resource> getResources() {
		return resources;
	}	
}
