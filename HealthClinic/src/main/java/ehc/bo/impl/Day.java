package ehc.bo.impl;

public class Day {
/*	String name;
	Date from;
	Date to;*/
	/*private int dayOfWeek;*/
	private String name;
	private int from;
	private int to;
/*	private Calendar c;*/
	
	public Day(String name, int fromHours, int fromMinutes, int toHours, int toMinutes) {
		this.name = name;
		from = fromHours*3600 + fromMinutes*60;
		to = toHours*3600 + toMinutes*60;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
}
