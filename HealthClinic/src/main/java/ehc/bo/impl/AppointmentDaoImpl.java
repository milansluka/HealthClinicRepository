package ehc.bo.impl;

import java.util.List;

import ehc.bo.AppointmentDao;

public class AppointmentDaoImpl extends Dao implements AppointmentDao {

	public void addAppointment(Appointment appointment) {
		openCurrentSessionWithTransaction();
	
		currentSession.save(appointment);
		
		closeCurrentSessionWithTransaction();
	}

	public void deleteAppointment(Appointment appointment) {
		// TODO Auto-generated method stub
		
	}

	public void updateAppointment(Appointment appointment) {
		// TODO Auto-generated method stub
		
	}

	public List<Appointment> getAllAppointments() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
