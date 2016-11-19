package milansluka.HealthClinicWeb.pages;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.HttpError;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.slf4j.Logger;
import org.testng.annotations.BeforeClass;

import ehc.bo.impl.HealthPoint;
import entities.AppointmentModel;
import entities.DayModel;
import entities.RoomLane;
import entities.ScheduleConfiguration;
import entities.ScheduleModel;
import entities.TimeWindowModel;
import entities.WeekModel;
import entities.WorkDay;
import milansluka.HealthClinicWeb.components.AppointmentScheduleWeekView;
import milansluka.HealthClinicWeb.pages.admin.Admin;
import utils.DateUtil;

/**
 * Start page of application HealthClinicWeb.
 */
public class Index {
	/* private AppointmentDao appointmentDao = AppointmentDao.getInstance(); */

	@Inject
	private Logger logger;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@InjectPage
	private Admin admin;

	@Component
	private AppointmentScheduleWeekView appointmentScheduleWeekView;

	@Persist
	@Property
	private ScheduleModel model;

	@Property
	@Inject
	@Symbol(SymbolConstants.TAPESTRY_VERSION)
	private String tapestryVersion;

	@InjectPage
	private About about;

	@Inject
	private Block block;

	@Property
	String name;

	// Handle call with an unwanted context
	Object onActivate(EventContext eventContext) {
		return eventContext.getCount() > 0 ? new HttpError(404, "Resource not found") : null;
	}

	/*
	 * void onActionFromConfigureClinic() { RootTestCase rootTestCase = new
	 * RootTestCase(); rootTestCase.setUpSystem2(); }
	 */

	/*
	 * ScheduleModelImpl getScheduleModel() { return new ScheduleModelImpl(15);
	 * }
	 */

	Object onActionFromAdmin() {

		return admin;
	}

	public int getWeeksCount() {
		if (model == null) {
			return 0;
		}
		return model.getWeekModels().size();
	}

	Object onActionFromNext() {
		if (!appointmentScheduleWeekView.hasNextWeek()) {
			Date lastWeekEndDate = model.getWeekModels().get(model.getWeekModels().size() - 1).getTo();
			Date nextWeekStartDate = DateUtil.addDays(lastWeekEndDate,
					ScheduleConfiguration.NUMBER_OF_DAYS_IN_WEEK - model.getConfiguration().getWorkDays().size() + 1);
			model.addWeekModel(nextWeekStartDate);
		}
		appointmentScheduleWeekView.nextWeek();
		return null;
	}

	Object onActionFromPrevious() {
		appointmentScheduleWeekView.previousWeek();
		return null;
	}

	@Log
	void onComplete() {
		logger.info("Complete call on Index page");
	}

	@Log
	void onAjax() {
		logger.info("Ajax call on Index page");
		ajaxResponseRenderer.addRender("middlezone", block);
	}

/*	List<WeekModel> createWeekModels() {
		List<DayModel> dayModels = new ArrayList<DayModel>();
		List<RoomLane> roomLanes = new ArrayList<RoomLane>();
		RoomLane roomLane = new RoomLane();
		roomLane.addTimeWindowModel(new AppointmentModel(DateUtil.date(2016, 11, 13, 9, 0, 0),
				DateUtil.date(2016, 11, 13, 10, 0, 0), "Epilacia"));
		roomLane.addTimeWindowModel(new AppointmentModel(DateUtil.date(2016, 11, 13, 9, 0, 0),
				DateUtil.date(2016, 11, 13, 10, 0, 0), "Epilacia"));
		roomLanes.add(roomLane);
		DayModel dayModel = new DayModel();
		dayModel.addRoomLane(roomLane);
		dayModels.add(dayModel);
		List<WeekModel> weekModels = new ArrayList<WeekModel>();
		WeekModel weekModel = new WeekModel();
		weekModel.addDayModel(dayModel);
		weekModels.add(weekModel);
		return weekModels;
	}*/

	private void createAppointments() {

	}

	private void initModel() {
		List<WorkDay> workDays = new ArrayList<WorkDay>();
		workDays.add(new WorkDay("Monday", 7 * 60, 18 * 60));
		workDays.add(new WorkDay("Tuesday", 9 * 60, 18 * 60));
		workDays.add(new WorkDay("Wednesday", 8 * 60, 18 * 60));
		workDays.add(new WorkDay("Thursday", 7 * 60 + 30, 18 * 60));
		workDays.add(new WorkDay("Friday", 10 * 60, 18 * 60));
		List<String> roomNames = new ArrayList<String>();
		roomNames.add("room 1");
		roomNames.add("room 2");
		roomNames.add("room 3");
		ScheduleConfiguration scheduleConfiguration = new ScheduleConfiguration(
				HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES, roomNames, workDays, 7 * 60, 18 * 60);
		/* List<WeekModel> weekModels = createWeekModels(); */

		model = new ScheduleModel(scheduleConfiguration);
		Calendar c = Calendar.getInstance();
		Date currentDate = DateUtil.date(2016, 11, 17, 0, 0, 0);
		c.setTime(currentDate);
		int currentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int daysDiff = currentDayOfWeek - Calendar.MONDAY;
		Date weekStartDate = DateUtil.addDays(currentDate, -daysDiff);
		model.addWeekModel(weekStartDate);
		addAppointmentModels();
	}

	void onActivate() {
		if (model == null) {
			initModel();
		}
	}

	/*
	 * @BeforeClass void init() { if (model == null) { initModel(); }
	 * 
	 * }
	 */

	/* @BeforeRenderTemplate */
	void setup() {
		List<WorkDay> workDays = new ArrayList<WorkDay>();
		/*
		
		 */
		workDays.add(
				new WorkDay("Monday", DateUtil.date(2016, 10, 10, 7, 0, 0), DateUtil.date(2016, 10, 10, 18, 0, 0)));
		workDays.add(
				new WorkDay("Tuesday", DateUtil.date(2016, 10, 10, 7, 0, 0), DateUtil.date(2016, 10, 10, 18, 0, 0)));
		workDays.add(
				new WorkDay("Wednesday", DateUtil.date(2016, 10, 10, 7, 0, 0), DateUtil.date(2016, 10, 10, 18, 0, 0)));
		workDays.add(
				new WorkDay("Thursday", DateUtil.date(2016, 10, 10, 7, 0, 0), DateUtil.date(2016, 10, 10, 18, 0, 0)));
		workDays.add(
				new WorkDay("Friday", DateUtil.date(2016, 10, 10, 7, 0, 0), DateUtil.date(2016, 10, 10, 18, 0, 0)));
		List<String> roomNames = new ArrayList<String>();
		roomNames.add("room 1");
		roomNames.add("room 2");
		roomNames.add("room 3");
		ScheduleConfiguration scheduleConfiguration = new ScheduleConfiguration(
				HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES, roomNames, workDays, 7 * 60, 18 * 60);
				// List<WeekModel> weekModels = createWeekModels();

		/* model = new ScheduleModel(scheduleConfiguration); */
		model.addWeekModel(DateUtil.date(2016, 11, 10, 0, 0, 0));

		Date from = model.getWeekModels().get(0).getFrom();
		Date to = model.getWeekModels().get(0).getTo();

		/* List<Appointment> appointments = getAppointments(from, to); */
		/*
		 * TimeWindowModel appointmentModel = new AppointmentModel(9*60, 10*60,
		 * "Epilacia", from, "room 1");
		 */
		/* model.addTimeWindowModel(0, appointmentModel); */
	}

	public void addAppointmentModels() {
		AppointmentModel appointmentModel1 = new AppointmentModel(9 * 60, 10 * 60, "Epilacia", DateUtil.date(2016, 11, 14, 9, 0, 0), "room 1");
        model.addAppointmentModel(0, appointmentModel1);    
	}

	/*
	 * public List<Appointment> getAppointments(Date from, Date to) { return
	 * null; }
	 */

	public Date getCurrentTime() {
		return new Date();
	}
}
