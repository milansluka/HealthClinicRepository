package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import ehc.bo.Resource;

@Entity
@Table(name = "appointment")
public class Appointment extends BaseObject {
	Date from;
	Date to;
	AppointmentState state;
	Individual individual;
	List<TreatmentType> treatmentTypes = new ArrayList<>();
	List<Resource> resources = new ArrayList<>();
	List<Treatment> executedTreatments = new ArrayList<>();
	List<Payment> payments = new ArrayList<>();
	Appointment previous = null;
	Appointment next = null;

	protected Appointment() {
		super();
	}

/*	public Appointment(User executor, Date from, Date to, TreatmentType treatmentType, Individual individual) {
		super(executor);
		this.from = from;
		this.to = to;
		 assignTreatmentType(treatmentType); 
		addTreatmentType(treatmentType);
		assignIndividual(individual);
		state = new AppointmentState(executor);
		state.setAppointment(this);
	}*/

	public Appointment(User executor, Date from, Date to, Individual individual) {
		super(executor);
		this.from = from;
		this.to = to;
		/* assignTreatmentType(treatmentType); */
		/*addTreatmentTypes(treatmentTypes);*/
		assignIndividual(individual);
		state = new AppointmentState(executor);
		state.setAppointment(this);
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = ResourceImpl.class)
	@JoinTable(name = "appointment_resource", joinColumns = {
			@JoinColumn(name = "appointment_id") }, inverseJoinColumns = { @JoinColumn(name = "resource_id") })
	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	@OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
	public List<Treatment> getExecutedTreatments() {
		return executedTreatments;
	}

	public void setExecutedTreatments(List<Treatment> executedTreatments) {
		this.executedTreatments = executedTreatments;
	}

	@OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
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

	@OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
	public AppointmentState getState() {
		return state;
	}

	public void setState(AppointmentState state) {
		this.state = state;
	}

	@OneToOne
	@JoinColumn(name = "previous")
	public Appointment getPrevious() {
		return previous;
	}

	public void setPrevious(Appointment previous) {
		this.previous = previous;
	}

	@OneToOne
	@JoinColumn(name = "next")
	public Appointment getNext() {
		return next;
	}

	public void setNext(Appointment next) {
		this.next = next;
	}

	@ManyToOne
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "individual_id")
	public Individual getIndividual() {
		return individual;
	}

	public void setIndividual(Individual person) {
		this.individual = person;
	}

	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "treatment_id") public TreatmentType
	 * getTreatmentType() { return treatmentType; }
	 * 
	 * public void setTreatmentType(TreatmentType treatmentType) {
	 * this.treatmentType = treatmentType; }
	 */

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable(name = "appointment_treatment_type", joinColumns = {
			@JoinColumn(name = "appointment_id") }, inverseJoinColumns = { @JoinColumn(name = "treatment_type_id") })
	public List<TreatmentType> getTreatmentTypes() {
		return treatmentTypes;
	}

	public void setTreatmentTypes(List<TreatmentType> treatmentTypes) {
		this.treatmentTypes = treatmentTypes;
	}

	public void assignPerson(Individual person) {
		if (person != null) {
			setIndividual(person);
			person.addAppointment(this);
		}
	}

	public void addTreatmentType(TreatmentType treatmentType) {
		if (treatmentType != null) {
			getTreatmentTypes().add(treatmentType);
			treatmentType.addAppointment(this);
		}
	}

	public void addTreatmentTypes(List<TreatmentType> treatmentTypes) {
		if (treatmentTypes == null) {
			return;
		}
		for (TreatmentType treatmentType : treatmentTypes) {
			addTreatmentType(treatmentType);
		}
	}

	/*
	 * public void assignTreatmentType(TreatmentType treatmentType) { if
	 * (treatmentType != null) { setTreatmentType(treatmentType);
	 * treatmentType.addAppointment(this); } }
	 */

	public void assignIndividual(Individual individual) {
		if (individual != null) {
			setIndividual(individual);
			individual.addAppointment(this);
		}
	}

	public void addResource(Resource resource) {
		if (resource == null) {
			return;
		}
		getResources().add(resource);
		resource.addAppointment(this);
	}
	
	public void addPayment(Payment payment) {
		getPayments().add(payment);
	}

	public void removeIndividual() {
		getIndividual().removeAppointment(this);
	}

	public void removeResource(Resource resource) {
		/*
		 * if (resource == null) { return; } getResources().remove(resource);
		 */
		resource.removeAppointment(this);
	}

	public void addResources(List<Resource> resources) {
		if (resources == null) {
			return;
		}
		for (Resource resource : resources) {
			addResource(resource);
		}
	}

	public void removeResources() {
		for (Resource resource : getResources()) {
			removeResource(resource);
		}
	}

	public void prepareForDeleting() {
		 removeResources(); 
		 removeIndividual(); 
		removeFromNext();
		removeFromPrevious();
	}

	public void removeFromNext() {
		if (getNext() == null) {
			return;
		}
		getNext().setPrevious(null);
	}

	public void removeFromPrevious() {
		if (getPrevious() == null) {
			return;
		}
		getPrevious().setNext(null);
	}

	public void setState(User executor, AppointmentStateValue value) {
		getState().setModifiedBy(executor);
		getState().setValue(value);
	}

	public void addTreatment(Treatment treatment) {
		getExecutedTreatments().add(treatment);
	}
}
