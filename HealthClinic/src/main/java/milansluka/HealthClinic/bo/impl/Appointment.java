package milansluka.HealthClinic.bo.impl;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment {
	
	long id;
	
	Date from;
	Date to;
/*	Date when;*/
	
	Person person;
	Intervention intervention;
	
	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "appointment_id_seq", name = "appointment_id_seq")
	@GeneratedValue(generator = "appointment_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "appointment_id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "\"from\"")
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	
	@Column(name = "\"to\"")
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
/*	public Date getWhen() {
		return when;
	}
	public void setWhen(Date when) {
		this.when = when;
	}*/
	
	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "intervention_id")
	public Intervention getIntervention() {
		return intervention;
	}
	public void setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}
	
	public void assignPerson(Person person) {
		if (person != null) {
			setPerson(person);
			person.addAppointment(this);
		}	
	}
	
	public void assignIntervention(Intervention intervention) {
		if (intervention != null) {
			setIntervention(intervention);
			intervention.addAppointment(this);
		}
		
	}
}
