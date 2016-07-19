package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "appointment_state")
@Inheritance(strategy=InheritanceType.JOINED) 
public class AppointmentState extends ModifiableObject {
	AppointmentStateValue value = AppointmentStateValue.NEW;
	Appointment appointment;
	
	protected AppointmentState() {
		super();
	}
	
	public AppointmentState(User executor) {
		super(executor);
	}
	
	@OneToOne
	@PrimaryKeyJoinColumn
	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	@Enumerated(EnumType.STRING)
	public AppointmentStateValue getValue() {
		return value;
	}

	public void setValue(AppointmentStateValue value) {
		this.value = value;
	}
}
