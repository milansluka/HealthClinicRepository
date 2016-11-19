package milansluka.HealthClinicWeb.components;

import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import entities.ScheduleModel;
import entities.WeekModel;

@Import(library = { "context:mybootstrap/js/test.js" }, stylesheet = {
		"context:mybootstrap/css/scheduleWeekViewStyle.css" })
public class AppointmentScheduleWeekView {
	@Parameter
	private ScheduleModel model;

	@Parameter
	private String name;

	@Property
	@Persist
	private int currentWeekIndex;
	
	@Component
	private AppointmentView appointmentView;
	
	private WeekModel currentWeek;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	public WeekModel getCurrentWeek() {
		return getCurrentWeekModel();
	}

	public void setCurrentWeek(WeekModel currentWeek) {
		this.currentWeek = currentWeek;
	}

	public ScheduleModel getModel() {
		return model;
	}
	
	public WeekModel getCurrentWeekModel() {
		return model.getWeekModels().get(currentWeekIndex);
	}

	public void setModel(ScheduleModel model) {
		this.model = model;
	}
	
	public String getFrom() {
		WeekModel currentWeek = model.getWeekModels().get(currentWeekIndex);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY");
		return simpleDateFormat.format(currentWeek.getFrom());
	}

	public String getTo() {
		WeekModel currentWeek = model.getWeekModels().get(currentWeekIndex);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY");
		return simpleDateFormat.format(currentWeek.getTo());
	}

	public boolean hasNextWeek() {
		return currentWeekIndex + 1 < model.getWeekModels().size();
	}

	public boolean hasPreviousWeek() {
		return currentWeekIndex > 0;
	}

	public void nextWeek() {
		if (hasNextWeek()) {
			currentWeekIndex++;
		}
	}

	public void previousWeek() {
		if (hasPreviousWeek()) {
			currentWeekIndex--;
		}
	}
}
