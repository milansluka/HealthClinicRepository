package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ehc.util.DateUtil;

public class WorkTime {
	List<Day> days = new ArrayList<Day>();
	
	public WorkTime() {
		super();
		days.add(new Day("Nedeľa", 7, 0, 18, 0));
		days.add(new Day("Pondelok", 7, 0, 18, 0));
		days.add(new Day("Utorok", 8, 30, 18, 0));
		days.add(new Day("Streda", 9, 0, 14, 0));
		days.add(new Day("Štvrtok", 7, 30, 18, 0));
		days.add(new Day("Piatok", 7, 0, 18, 0));	
		days.add(new Day("Sobota", 7, 0, 18, 0));
	}
		
	public WorkTime(List<Day> days) {
		super();
		this.days = days;
	}

	public Date getStartWorkTime(Date date) {
		Date retDate = DateUtil.normDays(date);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		Day day = days.get(dayOfWeek-1);
		retDate = DateUtil.addSeconds(retDate, day.getFrom());
		return retDate;	
	}
	
	public Date getEndWorkTime(Date date) {
		Date retDate = DateUtil.normDays(date);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		Day day = days.get(dayOfWeek-1);
		retDate = DateUtil.addSeconds(retDate, day.getTo());
		return retDate;	
	}	
}
