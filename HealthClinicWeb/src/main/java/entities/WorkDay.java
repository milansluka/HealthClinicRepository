package entities;

import java.util.Date;

public class WorkDay {
	private String name;
	private Date start;
	private Date end;
	
	private int startWorkInMinutes;
	private int endWorkInMinutes; 
	
	private int offset;
	private int limit;
	
	public WorkDay(String name, int start, int end) {
		super();
		this.name = name;
		this.startWorkInMinutes = start;
		this.endWorkInMinutes = end;	
	}
	
	public WorkDay(String name, Date start, Date end) {
		super();
		this.name = name;
		this.start = start;
		this.end = end;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}

	public int getStartWorkInMinutes() {
		return startWorkInMinutes;
	}

	public void setStartWorkInMinutes(int startWorkInMinutes) {
		this.startWorkInMinutes = startWorkInMinutes;
	}

	public int getEndWorkInMinutes() {
		return endWorkInMinutes;
	}

	public void setEndWorkInMinutes(int endWorkInMinutes) {
		this.endWorkInMinutes = endWorkInMinutes;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public boolean startTimeIsEqualOrLesser(int time) {
		return time >= startWorkInMinutes;
	}	
	
	public boolean startTimeIsEqual(int time) {
		return time == startWorkInMinutes;
	}	
}
