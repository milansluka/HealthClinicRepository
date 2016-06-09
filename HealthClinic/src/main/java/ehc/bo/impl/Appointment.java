package ehc.bo.impl;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment extends BaseObject {
	
	protected Appointment() {
		super();
	}

	public Appointment(User executor, Date from, Date to, TreatmentType treatmentType, Individual individual) {
		super(executor);
		this.from = from;
		this.to = to;
		this.treatmentType = treatmentType;
		this.individual = individual;
		this.state = AppointmentState.PROPOSED;
	}
	
	Date from;
	Date to;
	
	AppointmentState state;
	
	Individual individual;
	TreatmentType treatmentType;
	
	@Enumerated(EnumType.STRING)
	public AppointmentState getState() {
		return state;
	}

	public void setState(AppointmentState state) {
		this.state = state;
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
	

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "individual_id", nullable = false)
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
