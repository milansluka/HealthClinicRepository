package ehc.bo.impl;

import java.util.Date;
import java.util.List;

public class TimeWindow {
	private Date start;
	private Date end;
	private Room room;
    private List<AppointmentProposal> appointmentProposals;
    
/*    private String blockState;*/
    
    
	public TimeWindow(Date start, Date end, Room room) {
		super();
		this.start = start;
		this.end = end;
		this.room = room;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public List<AppointmentProposal> getAppointmentProposals() {
		return appointmentProposals;
	}
	public void setAppointmentProposals(List<AppointmentProposal> appointmentProposals) {
		this.appointmentProposals = appointmentProposals;
	}     
}
