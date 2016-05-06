package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "treatment_type")
public class TreatmentType extends ModificableObject{
	
	String name;
	String info;
	String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	List<Appointment> appointments;
	    
	public TreatmentType() {
		super();
		appointments = new ArrayList<Appointment>();
	}
	
	@OneToMany(mappedBy = "treatmentType")
	public List<Appointment> getAppointments() {
		return appointments;
	}
	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public void addAppointment(Appointment appointment) {
		getAppointments().add(appointment);
	}
}
