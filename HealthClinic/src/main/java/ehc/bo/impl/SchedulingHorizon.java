package ehc.bo.impl;

import java.util.Date;

import ehc.util.DateUtil;

public class SchedulingHorizon {
	private Date from;
	private Date to;
	
	public static Date defaultFrom() {
		return DateUtil.now();
	}
	
	public static Date defaultTo() {
		return DateUtil.addDays(HealthPoint.SCHEDULING_HORIZON_IN_DAYS);
	}

	public SchedulingHorizon(Date from, Date to) {
		super();
		this.from = from;
		this.to = to;
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
