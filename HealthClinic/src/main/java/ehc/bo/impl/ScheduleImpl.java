package ehc.bo.impl;

import java.util.Date;
import java.util.List;

import ehc.bo.Schedule;

public class ScheduleImpl implements Schedule {
	private int timeGridInMinutes;
	private WorkTime workTime;
	
/*	public ScheduleModelImpl(int timeGridInMinutes) {
		super();
		this.timeGridInMinutes = timeGridInMinutes;
	}*/
	public int getTimeGridInMinutes() {
		return timeGridInMinutes;
	}
	public void setTimeGridInMinutes(int timeGridInMinutes) {
		this.timeGridInMinutes = timeGridInMinutes;
	}
	public WorkTime getWorkTime() {
		return workTime;
	}
	public void setWorkTime(WorkTime workTime) {
		this.workTime = workTime;
	}
	@Override
	public Date getStartWorkTime(Date day) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Date getEndWorkTime(Date day) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<String> getRoomNames() {
		// TODO Auto-generated method stub
		return null;
	}	
}
