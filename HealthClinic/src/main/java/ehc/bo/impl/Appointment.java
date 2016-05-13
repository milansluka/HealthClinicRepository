package ehc.bo.impl;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
@AttributeOverride(name = "modifiedBy", column = @Column(name = "modified_by", insertable = false, updatable = false))
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
	}
	
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
