package ehc.bo.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "waitinglist")
@Inheritance(strategy = InheritanceType.JOINED)
public class WaitingIndividual extends ModifiableObject {
	Individual individual;
	TreatmentType treatmentType;
	Date from;
	Date to;

	protected WaitingIndividual() {
		super();
	}

	public WaitingIndividual(User executor, Individual individual, TreatmentType treatmentType, Date from, Date to) {
		super(executor);
		this.individual = individual;
		this.treatmentType = treatmentType;
		this.from = from;
		this.to = to;
	}

	@OneToOne
	@JoinColumn(name = "individual")
	public Individual getIndividual() {
		return individual;
	}

	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	@OneToOne
	@JoinColumn(name = "treatmenttype")
	public TreatmentType getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
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
}
