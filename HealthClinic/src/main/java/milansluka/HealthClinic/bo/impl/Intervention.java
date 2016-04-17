package milansluka.HealthClinic.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "intervention")
public class Intervention {
	
	long id;
	
	String name;
	String info;
	
	List<Appointment> appointments;
	
	
    
	public Intervention() {
		super();
		appointments = new ArrayList<Appointment>();
	}
	@OneToMany(mappedBy = "intervention")
	public List<Appointment> getAppointments() {
		return appointments;
	}
	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
	
	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "intervention_id_seq", name = "intervention_id_seq")
	@GeneratedValue(generator = "intervention_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "intervention_id")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
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
