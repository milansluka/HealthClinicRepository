package entities;

import java.util.Date;

public class TimeWindowModel {
	private Date fromDate;
/*	private Date toDate;*/
	private String roomName;
	
	private int from;
	private int to;
	
	private String info;
	
	public TimeWindowModel(int from, int to, Date fromDate, String roomName) {
		super();
		this.from = from;
		this.to = to;
		this.fromDate = fromDate;
		this.roomName = roomName;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public String getInfo() {
		String info = "";
		info += "Time window";
		info += "from: " + from + "to: " + to;
		info += "roomName: " + roomName;
		return info;
	}
	
	
	
	
	
/*	public TimeWindowModel(Date from, Date to) {
		super();
		this.from = from;
		this.to = to;
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
	public void setTo(Date to) {
		this.to = to;
	}*/
	
}
