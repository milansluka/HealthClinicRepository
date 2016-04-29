package ehc.bo.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment extends BaseObject {
	
	Date from;
	Date to;
/*	Date when;*/
	
	Individual person;
	Treatment treatment;
	
	
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
	public Individual getPerson() {
		return person;
	}
	public void setPerson(Individual person) {
		this.person = person;
	}
	
	@ManyToOne
	@JoinColumn(name = "intervention_id")
	public Treatment getTreatment() {
		return treatment;
	}
	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}
	
	public void assignPerson(Individual person) {
		if (person != null) {
			setPerson(person);
			person.addAppointment(this);
		}	
	}
	
	public void assignTreatment(Treatment treatment) {
		if (treatment != null) {
			setTreatment(treatment);
			treatment.addAppointment(this);
		}
		
	}
}
