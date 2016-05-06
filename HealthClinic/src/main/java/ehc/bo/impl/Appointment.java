package ehc.bo.impl;

import java.util.Date;

import javax.persistence.CascadeType;
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
	
	Individual individual;
	TreatmentType treatmentType;
	
	
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

	
	@ManyToOne
	@JoinColumn(name = "individual_id")
	public Individual getIndividual() {
		return individual;
	}
	public void setIndividual(Individual person) {
		this.individual = person;
	}
	
	@ManyToOne
	@JoinColumn(name = "treatment_id")
	public TreatmentType getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}
	
	public void assignPerson(Individual person) {
		if (person != null) {
			setIndividual(person);
			person.addAppointment(this);
		}	
	}
	
	public void assignTreatmentType(TreatmentType treatmentType) {
		if (treatmentType != null) {
			setTreatmentType(treatmentType);
			treatmentType.addAppointment(this);
		}
		
	}
}
