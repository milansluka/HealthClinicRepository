package milansluka.HealthClinicWeb.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;

import ehc.bo.impl.HealthPoint;
import entities.AppointmentModel;

@Import(library = { "context:mybootstrap/js/test.js" }, stylesheet = {
		"context:mybootstrap/css/appointmentViewStyle.css" })
public class AppointmentView {
	private final double CSS_TOP = 8;
	private final double CSS_LEFT = 8.65;
	private final double CSS_WIDTH = 6.1;
	private final double CSS_HEIGHT = 4;
		
	private int xPosition;
	private int yPosition;
	private int startTimeInMinutes;
	private int grid;
	private int height;
	
	private double distanceFromTop;
	private double distanceFromLeft;
	private double h;
	private double w;
	
/*	private int from;
	private int to;*/
	
	@Parameter
	private int dayIndex;
	
	@Parameter
	private int roomIndex;

	@Parameter
	private AppointmentModel model;

	public AppointmentModel getModel() {
		return model;
	}

	public void setModel(AppointmentModel model) {
		this.model = model;
	}
	
	public int getGrid() {
		return HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES;
	}

	public void setGrid(int grid) {
		this.grid = grid;
	}

	public int getStartTimeInMinutes() {
		return 7*60;
	}

	public void setStartTimeInMinutes(int startTimeInMinutes) {
		this.startTimeInMinutes = startTimeInMinutes;
	}

	public int getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}

	public int getRoomIndex() {
		return roomIndex;
	}

	public void setRoomIndex(int roomIndex) {
		this.roomIndex = roomIndex;
	}

	public int getxPosition() {
		return (dayIndex-1) + roomIndex;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public int getyPosition() {
		return (model.getFrom()-getStartTimeInMinutes())/getGrid();
	}
	
    public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}
	
	public double getDistanceFromTop() {
		return CSS_TOP + getyPosition()*CSS_HEIGHT;
	}

	public void setDistanceFromTop(double distanceFromTop) {
		this.distanceFromTop = distanceFromTop;
	}

	public double getDistanceFromLeft() {
		return CSS_LEFT + getxPosition()*CSS_WIDTH;
	}

	public void setDistanceFromLeft(double distanceFromLeft) {
		this.distanceFromLeft = distanceFromLeft;
	}

	public double getH() {
		return getHeight()*CSS_HEIGHT;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getW() {
		return CSS_WIDTH;
	}

	public void setW(double w) {
		this.w = w;
	}

	public int getHeight() {
		return (model.getTo() - model.getFrom())/getGrid();
	}

	public void setHeight(int height) {
		this.height = height;
	}	
}
