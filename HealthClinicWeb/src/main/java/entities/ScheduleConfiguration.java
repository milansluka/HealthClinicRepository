package entities;

import java.util.List;

public class ScheduleConfiguration {
	public static final int NUMBER_OF_DAYS_IN_WEEK = 7;
	private int grid;
	private List<String> roomNames;
	private List<WorkDay> workDays;
	private int startTimeInMinutes;
	private int endTimeInMinutes;
	
	public ScheduleConfiguration(int grid, List<String> roomNames, List<WorkDay> workDays, int startTimeInMinutes,
			int endTimeInMinutes) {
		super();
		this.grid = grid;
		this.roomNames = roomNames;
		this.workDays = workDays;
		this.startTimeInMinutes = startTimeInMinutes;
		this.endTimeInMinutes = endTimeInMinutes;
	}
	public int getGrid() {
		return grid;
	}
	public void setGrid(int grid) {
		this.grid = grid;
	}
	public List<String> getRoomNames() {
		return roomNames;
	}
	public void setRoomNames(List<String> roomNames) {
		this.roomNames = roomNames;
	}
	public List<WorkDay> getWorkDays() {
		return workDays;
	}
	public void setWorkDays(List<WorkDay> workDays) {
		this.workDays = workDays;
	}
	public int getStartTimeInMinutes() {
		return startTimeInMinutes;
	}
	public void setStartTimeInMinutes(int startTimeInMinutes) {
		this.startTimeInMinutes = startTimeInMinutes;
	}
	public int getEndTimeInMinutes() {
		return endTimeInMinutes;
	}
	public void setEndTimeInMinutes(int endTimeInMinutes) {
		this.endTimeInMinutes = endTimeInMinutes;
	}
	
	
}
