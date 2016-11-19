package entities;

import java.util.ArrayList;
import java.util.List;

public class RoomLane {
	private String name;
/*	private List<TimeWindowModel> timeWindowModels = new ArrayList<TimeWindowModel>();*/
	private List<AppointmentModel> appointmentModels = new ArrayList<AppointmentModel>();
/*	private TimeWindowModel timeWindowModel;*/
	private AppointmentModel appointmentModel;
	
	public RoomLane(String name) {
		super();
		this.name = name;
	}

/*	public void addTimeWindowModel(TimeWindowModel timeWindowModel) {
		timeWindowModels.add(timeWindowModel);
	}*/
	
	public void addAppointmentModel(AppointmentModel appointmentModel) {
		appointmentModels.add(appointmentModel);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AppointmentModel> getAppointmentModels() {
		return appointmentModels;
	}

	public void setAppointmentModels(List<AppointmentModel> appointmentModels) {
		this.appointmentModels = appointmentModels;
	}

	public AppointmentModel getAppointmentModel() {
		return appointmentModel;
	}

	public void setAppointmentModel(AppointmentModel appointmentModel) {
		this.appointmentModel = appointmentModel;
	}

/*	public TimeWindowModel getTimeWindowModel() {
		return timeWindowModel;
	}

	public void setTimeWindowModel(TimeWindowModel timeWindowModel) {
		this.timeWindowModel = timeWindowModel;
	}

	public List<TimeWindowModel> getTimeWindowModels() {
		return timeWindowModels;
	}

	public void setTimeWindowModels(List<TimeWindowModel> timeWindowModels) {
		this.timeWindowModels = timeWindowModels;
	}*/
}
