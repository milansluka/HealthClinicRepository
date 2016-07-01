package ehc.bo.impl;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Nurse extends PartyRole{
	NurseType type;
	List<Appointment> appointments;
	
	protected Nurse() {
		super();
	}
	
	public Nurse(User executor, NurseType type, Party source, Party target) {
		super(executor, source, target);
		this.type = type;
	}
		
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "nurse_type_id")
	public NurseType getType() {
		return type;
	}
		
	public void setType(NurseType type) {
		this.type = type;
	}
	
	@OneToMany(mappedBy = "nurse")
	public List<Appointment> getAppointments() {
		return appointments;
	}
	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
	
	public void addAppointment(Appointment appointment) {
		getAppointments().add(appointment);
	}
}
