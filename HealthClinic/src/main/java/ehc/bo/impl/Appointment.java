package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ehc.bo.Resource;

@Entity
@Table(name = "appointment")
public class Appointment extends BaseObject {
	Date from;
	Date to;
	AppointmentState state = AppointmentState.PROPOSED;
	Individual individual;
	TreatmentType treatmentType;
	List<Resource> resources = new ArrayList<Resource>();

	protected Appointment() {
		super();
	}

	public Appointment(User executor, Date from, Date to, TreatmentType treatmentType, Individual individual) {
		super(executor);
		this.from = from;
		this.to = to;
		assignTreatmentType(treatmentType);
		this.individual = individual;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = ResourceImpl.class)
	@JoinTable(name = "appointment_resource", joinColumns = {@JoinColumn(name = "appointment_id")},
	inverseJoinColumns = {@JoinColumn(name = "resource_id")})
	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

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
	
	public void addResource(Resource resource) {
		if (resource == null) {
			return;
		}
		getResources().add(resource);
		resource.addAppointment(this);
	}
}
