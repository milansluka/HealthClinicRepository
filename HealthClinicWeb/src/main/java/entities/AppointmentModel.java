package entities;

import java.util.Date;

public class AppointmentModel extends TimeWindowModel {
	private String treatmentName;
  /*  private String roomName;*/
	
	public AppointmentModel(int from, int to, String treatmentName, Date fromDate, String roomName) {
		super(from, to, fromDate, roomName);
		this.treatmentName = treatmentName;
	}
	
/*	public AppointmentModel(Date from, Date to, String treatmentName) {
		super(from, to);
		this.treatmentName = treatmentName;
	}*/

	public String getTreatmentName() {
		return treatmentName;
	}

	public void setTreatmentName(String treatmentName) {
		this.treatmentName = treatmentName;
	}
	
	

/*	public String getRoomName() {
		return roomName;
	}*/

/*	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}*/

	@Override
	public String getInfo() {
		String ret = super.getInfo();
		ret += "treatmentName: " + treatmentName;
		return ret;
	}
	
	

}
