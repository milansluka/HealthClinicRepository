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
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import ehc.bo.Resource;

@Entity
public class Appointment extends BaseObject {
	Date from;
	Date to;
	AppointmentState state;
	Individual individual;
	PatientBill patientBill;
	List<TreatmentType> plannedTreatmentTypes = new ArrayList<TreatmentType>();
	List<Resource> resources = new ArrayList<Resource>();
	List<Treatment> executedTreatments = new ArrayList<Treatment>();
	/* List<Payment> payments = new ArrayList<>(); */
	/* List<PatientReceipt> receipts = new ArrayList<>(); */
	/* List<PatientBill> bills = new ArrayList<>(); */
	/* PatientBill patientBill; */
	Appointment previous;
	Appointment next;
	//double averageExpectedDelay;

	protected Appointment() {
		super();
	}

	public Appointment(User executor, Date from, Date to, Individual individual) {
		super(executor);
		this.from = from;
		this.to = to;
		assignIndividual(individual);
		state = new AppointmentState(executor);
		state.setAppointment(this);
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = ResourceImpl.class)
	@JoinTable(name = "appointment_resource", joinColumns = {
			@JoinColumn(name = "appointment") }, inverseJoinColumns = { @JoinColumn(name = "resource") })
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

	/*
	 * @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL) public
	 * List<Payment> getPayments() { return payments; }
	 * 
	 * public void setPayments(List<Payment> payments) { this.payments =
	 * payments; }
	 */

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
	
	
/*	@Transient
	public Date getDelayedFrom() {
		return from;
	}

	@Transient
	public Date getDelayedTo() {
		return to;
	}*/
	

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
	@JoinColumn(name = "individual")
	public Individual getIndividual() {
		return individual;
	}

	public void setIndividual(Individual person) {
		this.individual = person;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable(name = "appointment_treatmenttype", joinColumns = {
			@JoinColumn(name = "appointment") }, inverseJoinColumns = { @JoinColumn(name = "treatmenttype") })
	public List<TreatmentType> getPlannedTreatmentTypes() {
		return plannedTreatmentTypes;
	}

	public void setPlannedTreatmentTypes(List<TreatmentType> treatmentTypes) {
		this.plannedTreatmentTypes = treatmentTypes;
	}

	@OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
	public PatientBill getPatientBill() {
		return patientBill;
	}

	public void setPatientBill(PatientBill patientBill) {
		this.patientBill = patientBill;
	}

	/*
	 * @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL) public
	 * List<PatientReceipt> getReceipts() { return receipts; }
	 * 
	 * public void setReceipts(List<PatientReceipt> receipts) { this.receipts =
	 * receipts; }
	 */

	/*
	 * @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL) public
	 * List<PatientBill> getBills() { return bills; }
	 * 
	 * public void setBills(List<PatientBill> bills) { this.bills = bills; }
	 */

	public void assignPerson(Individual person) {
		if (person != null) {
			setIndividual(person);
			person.addAppointment(this);
		}
	}

	public void addTreatmentType(TreatmentType treatmentType) {
		if (treatmentType != null) {
			getPlannedTreatmentTypes().add(treatmentType);
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

	/*
	 * public void addPayment(Payment payment) { getPayments().add(payment); }
	 */

	public void removeIndividual() {
		getIndividual().removeAppointment(this);
	}

	public void removeResource(Resource resource) {
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

	/*
	 * public void addPatientBill(PatientBill patientBill) {
	 * getBills().add(patientBill); }
	 */

	@Transient
	public boolean isPayed() {
		return getPatientBill().isPaid();
	}

	/*
	 * @Transient public boolean isPayed() { Money paidAmount = new Money();
	 * Money requiredAmount = new Money();
	 * 
	 * for (Payment payment : getPayments()) {
	 * paidAmount.add(payment.getPaidAmount()); } for (Treatment treatment :
	 * getExecutedTreatments()) { requiredAmount.add(treatment.getPrice()); }
	 * 
	 * return paidAmount.greaterThanOrEqual(requiredAmount); }
	 */

	/*
	 * @Transient public boolean isPayed() { Money paidAmount = new Money();
	 * Money requiredAmount = new Money();
	 * 
	 * for (PatientReceipt patientReceipt : getReceipts()) {
	 * paidAmount.add(patientReceipt.getTotalPayedPrice()); } for (PatientBill
	 * patientBill : getBills()) {
	 * requiredAmount.add(patientBill.getTotalPrice()); }
	 * 
	 * return paidAmount.greaterThanOrEqual(requiredAmount) &&
	 * getUnpaidBills().isEmpty(); }
	 */

	/*
	 * @Transient public List<PatientBill> getUnpaidBills() { List<PatientBill>
	 * unpaidBills = new ArrayList<>();
	 * 
	 * for (PatientBill patientBill : getBills()) { if (patientBill.getPayment()
	 * == null) { unpaidBills.add(patientBill); } } return unpaidBills; }
	 */

	/*
	 * @Transient public List<PatientBill> getPaidBills() { List<PatientBill>
	 * paidBills = new ArrayList<>();
	 * 
	 * for (PatientBill patientBill : getBills()) { if (patientBill.getPayment()
	 * != null) { paidBills.add(patientBill); } } return paidBills; }
	 */

	@Transient
	public List<PatientBillItem> getUnpaidBillItems() {
		List<PatientBillItem> unpaidBillItems = new ArrayList<PatientBillItem>();

		for (PatientBillItem patientBillItem : getPatientBill().getItems()) {
			if (patientBillItem.getPayment() == null) {
				unpaidBillItems.add(patientBillItem);
			}
		}
		return unpaidBillItems;
	}

	@Transient
	public int getDurationInSeconds() {
		return (int) (getTo().getTime() - getFrom().getTime()) / 1000;
	}

	/*
	 * @Transient public List<Treatment> getUnpaidTreatments() { List<Treatment>
	 * unpaidTreatments = new ArrayList<>();
	 * 
	 * for (Treatment treatment : getExecutedTreatments()) { if
	 * (treatment.getPayment() == null) { unpaidTreatments.add(treatment); } }
	 * return unpaidTreatments; }
	 */
}
