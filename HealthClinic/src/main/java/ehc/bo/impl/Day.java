package ehc.bo.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Day extends ModifiableObject {
/*	String name;
	Date from;
	Date to;*/
	/*private int dayOfWeek;*/
	private String name;
	private int startWorkTime;
	private int endWorkTime;
	private WorkTime workTime;
/*	private Calendar c;*/
	
	protected Day() {
		super();
	}
	
	public Day(User executor, String name, int fromHours, int fromMinutes, int toHours, int toMinutes) {
		super(executor);
		this.name = name;
		startWorkTime = fromHours*3600 + fromMinutes*60;
		endWorkTime = toHours*3600 + toMinutes*60;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "start_work_time")
	public int getStartWorkTime() {
		return startWorkTime;
	}
	public void setStartWorkTime(int from) {
		this.startWorkTime = from;
	}
	
	@Column(name = "end_work_time")
	public int getEndWorkTime() {
		return endWorkTime;
	}
	public void setEndWorkTime(int to) {
		this.endWorkTime = to;
	}

	@ManyToOne
	@JoinColumn(name = "work_time_id")
	public WorkTime getWorkTime() {
		return workTime;
	}

	public void setWorkTime(WorkTime workTime) {
		this.workTime = workTime;
	}
	
	
}
