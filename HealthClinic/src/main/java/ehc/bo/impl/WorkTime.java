package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import ehc.util.DateUtil;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class WorkTime extends ModifiableObject {
	List<Day> days = new ArrayList<Day>();

	protected WorkTime() {
		super();
	}

	public WorkTime(User executor) {
		super(executor);
	}

	@OneToMany(mappedBy = "workTime", cascade = CascadeType.ALL)
	public List<Day> getDays() {
		return days;
	}

	public void setDays(List<Day> days) {
		this.days = days;
	}
	
	public void addDay(Day day) {
		if (day == null) {
			return;
		}
		getDays().add(day);
		day.setWorkTime(this);
	}

	public Date getStartWorkTime(Date date) {
		Date retDate = DateUtil.normDays(date);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		Day day = days.get(dayOfWeek - 1);
		retDate = DateUtil.addSeconds(retDate, day.getStartWorkTime());
		return retDate;
	}

	public Date getEndWorkTime(Date date) {
		Date retDate = DateUtil.normDays(date);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		Day day = days.get(dayOfWeek - 1);
		retDate = DateUtil.addSeconds(retDate, day.getEndWorkTime());
		return retDate;
	}
}
