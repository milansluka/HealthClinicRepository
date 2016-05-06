package ehc.bo;

import java.util.List;

import ehc.bo.impl.Appointment;

public interface AppointmentDao {
	void addAppointment(Appointment appointment);
	void deleteAppointment(Appointment appointment);
	void updateAppointment(Appointment appointment);
	List<Appointment> getAllAppointments();

}
