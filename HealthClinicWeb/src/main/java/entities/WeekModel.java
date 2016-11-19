package entities;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import utils.DateUtil;

public class WeekModel {
	private Date from;
	private Date to;
/*	private List<DayModel> dayModels = new ArrayList<DayModel>();*/
	private DayModel[] dayModels;
	private DayModel dayModel;
	private int dayIndex;
	
	public WeekModel(Date from, List<WorkDay> workDays, List<String> roomNames) {
		super();
		this.from = from;
		dayModels = new DayModel[workDays.size()];
		initDayModels(workDays, roomNames);
		to = DateUtil.addDays(from, dayModels.length-1);
		to = DateUtil.addSeconds(to, workDays.get(workDays.size()-1).getEndWorkInMinutes()*60);
	}
	
	
	
	public int getDayIndex() {
		return dayIndex;
	}



	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}



	private void initDayModels(List<WorkDay> workDays, List<String> roomNames) {
		for (int i = 0; i < dayModels.length; i++) {
			dayModels[i] = new DayModel(workDays.get(i).getName(), roomNames);
		}
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
		
	public DayModel getDayModel() {
		return dayModel;
	}

	public void setDayModel(DayModel dayModel) {
		this.dayModel = dayModel;
	}

	public DayModel[] getDayModels() {
		return dayModels;
	}

	public void setDayModels(DayModel[] dayModels) {
		this.dayModels = dayModels;
	}
	
	public void addAppointmentModel(AppointmentModel appointmentModel) {
		Calendar c = Calendar.getInstance();
		c.setTime(appointmentModel.getFromDate());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		dayModels[dayOfWeek-1].addAppointmentModel(appointmentModel);
	}

/*	public void addTimeWindowModel(TimeWindowModel timeWindowModel) {
		Calendar c = Calendar.getInstance();
		c.setTime(timeWindowModel.getFromDate());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		dayModels[dayOfWeek-1].addTimeWindowModel(timeWindowModel);
	}*/


/*	public void addDayModel(DayModel dayModel) {
		dayModels.add(dayModel);
	}*/
}
