package entities;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateUtil;

public class ScheduleModel {
	private ScheduleConfiguration configuration;
	/*
	 * private int grid; private List<String> roomNames = new
	 * ArrayList<String>();
	 */
	private List<String> times = new ArrayList<String>();
	/* private List<WorkDay> workDays = new ArrayList<WorkDay>(); */
	private List<MonthModel> monthModels = new ArrayList<MonthModel>();
	private List<WeekModel> weekModels = new ArrayList<WeekModel>();
	// private List<TimeWindowModel> timeWindowModels = new
	// ArrayList<TimeWindowModel>();
	private String roomName;
	private int roomsCount;
	private WorkDay workDay;
	private String time;
	private String firstTime;
	private int timeIndex;
	private int workDayIndex;
	private int roomIndex;
	/*
	 * private int startTimeInMinutes; private int endTimeInMinutes;
	 */

	public ScheduleModel(ScheduleConfiguration configuration) {
		super();
		/*
		 * this.grid = grid; this.roomNames = roomNames; this.workDays =
		 * workDays; this.startTimeInMinutes = startTimeInMinutes;
		 * this.endTimeInMinutes = endTimeInMinutes;
		 */
		this.configuration = configuration;
		/* this.weekModels = weekModels; */
		createTimes();
		setupWorkdaysOffsetsAndLimits();
		roomsCount = getRoomNames().size();
	}

/*	public void addTimeWindowModel(int weekIndex, TimeWindowModel timeWindowModel) {
		getWeekModels().get(weekIndex).addTimeWindowModel(timeWindowModel);
	}*/
	
	public void addAppointmentModel(int weekIndex, AppointmentModel appointmentModel) {
		getWeekModels().get(weekIndex).addAppointmentModel(appointmentModel);
	}
	
	public void next() {
		
	}

	public void addWeekModel(Date from) {
		from = DateUtil.normDays(from);
		
		WeekModel weekModel = new WeekModel(from, configuration.getWorkDays(), configuration.getRoomNames());
		weekModels.add(weekModel);
	}

	private void setupWorkdaysOffsetsAndLimits() {
		for (WorkDay workDay : configuration.getWorkDays()) {
			int offset = (workDay.getStartWorkInMinutes() - configuration.getStartTimeInMinutes())
					/ configuration.getGrid();
			if (offset == 0) {
				offset = 1;
			}
			workDay.setOffset(offset);
			workDay.setLimit(
					(configuration.getEndTimeInMinutes() - workDay.getEndWorkInMinutes()) / configuration.getGrid());
		}
	}

	private void createTimes() {
		/* LocalTime localTime = LocalTime.of(0, 0); */

		/* SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm"); */
		/*
		 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		 * localTime.plusMinutes(getStartTimeInMinutes());
		 */
		for (int i = getStartTimeInMinutes(); i <= getEndTimeInMinutes(); i += getGrid()) {
			/*
			 * times.add(localTime.format(formatter));
			 * localTime.plusMinutes(getGrid());
			 */
			times.add("" + i);
		}
	}

	public int getGrid() {
		return configuration.getGrid();
	}

	public void setGrid(int grid) {
		configuration.setGrid(grid);
	}

	public String getRoomName() {
		return roomName;
	}

	public ScheduleConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ScheduleConfiguration configuration) {
		this.configuration = configuration;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public void setRoomsCount(int roomsCount) {
		this.roomsCount = roomsCount;
	}

	public WorkDay getWorkDay() {
		return workDay;
	}

	public String getFormattedTime(int time) {
		LocalTime localTime = LocalTime.of(0, 0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		localTime = localTime.plusMinutes(time);
		return localTime.format(formatter);
	}

	public List<String> getTimes() {
		List<String> ret = new ArrayList<String>(times);
		ret.remove(0);
		return ret;
	}

	public boolean timeIndexIsNotZero() {
		return timeIndex != 0;
	}
	
	public boolean roomIndexIsZero(int roomIndex) {
		return roomIndex == 0;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setTimes(List<String> times) {
		this.times = times;
	}

	public String getTime() {
		return time;
	}

	public int getTimeIndex() {
		return timeIndex;
	}

	public void setTimeIndex(int timeIndex) {
		this.timeIndex = timeIndex;
	}
	
	

	public int getRoomIndex() {
		return roomIndex;
	}

	public void setRoomIndex(int roomIndex) {
		this.roomIndex = roomIndex;
	}

	public void setWorkDay(WorkDay workDay) {
		this.workDay = workDay;
	}

	public List<String> getRoomNames() {
		return configuration.getRoomNames();
	}

	public void setRoomNames(List<String> roomNames) {
		configuration.setRoomNames(roomNames);
	}

	public int getRoomsCount() {
		return configuration.getRoomNames().size();
	}

	public List<WorkDay> getWorkDays() {
		return configuration.getWorkDays();
	}

	public void setWorkDays(List<WorkDay> workDays) {
		this.configuration.setWorkDays(workDays);
	}
	
	public int getWorkDayIndex() {
		return workDayIndex;
	}

	public void setWorkDayIndex(int workDayIndex) {
		this.workDayIndex = workDayIndex;
	}

	public List<WeekModel> getWeekModels() {
		return weekModels;
	}

	public void setWeekModels(List<WeekModel> weekModels) {
		this.weekModels = weekModels;
	}

	public String getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(String firstTime) {
		this.firstTime = firstTime;
	}

	public int getStartTimeInMinutes() {
		return getConfiguration().getStartTimeInMinutes();
	}

	public void setStartTimeInMinutes(int startTimeInMinutes) {
		configuration.setStartTimeInMinutes(startTimeInMinutes);
	}

	public int getEndTimeInMinutes() {
		return configuration.getEndTimeInMinutes();
	}

	public void setEndTimeInMinutes(int endTimeInMinutes) {
		configuration.setEndTimeInMinutes(endTimeInMinutes);
	}

}
