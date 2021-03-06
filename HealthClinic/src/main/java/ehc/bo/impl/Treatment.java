package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import ehc.bo.Resource;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Treatment extends ModifiableObject {
	private Date from;
	private Date to;
	private Appointment appointment;
	private TreatmentType treatmentType;
/*	private Payment payment;*/
	private List<Resource> resources = new ArrayList<Resource>();
	private List<Attachment> attachments = new ArrayList<Attachment>();
	private Money price;
	private PatientBillItem patientBillItem;
/*	private PatientReceiptItem patientReceiptItem;*/
	
	protected Treatment() {
		super();
	}
	
	public Treatment(User executor, Appointment appointment, TreatmentType treatmentType, Money price, Date from, Date to) {
		super(executor);
		addAppointment(appointment);
		addTreatmentType(treatmentType);
		this.price = new Money(price);
		this.from = from;
		this.to = to;
	}

	@ManyToOne
	@JoinColumn(name = "appointment")
	public Appointment getAppointment() {
		return appointment;
	}
	
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
		
	@ManyToOne
	@JoinColumn(name = "treatmenttype")
	public TreatmentType getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}
	
	
	
 /*   @ManyToOne
    @JoinColumn(name = "payment")
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}*/

	@OneToOne(mappedBy = "treatment")
	public PatientBillItem getPatientBillItem() {
		return patientBillItem;
	}

	public void setPatientBillItem(PatientBillItem patientBillItem) {
		this.patientBillItem = patientBillItem;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = ResourceImpl.class)
	@JoinTable(name = "resource_treatment", joinColumns = {@JoinColumn(name = "treatment")},
	inverseJoinColumns = {@JoinColumn(name = "resource")})
	public List<Resource> getResources() {
		return resources;
	}
	
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	
	@OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL)
	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "price")
	public Money getPrice() {
		return price;
	}

	public void setPrice(Money price) {
		this.price = price;
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
	
	
/*	@OneToOne(mappedBy = "treatment")*/
/*	@OneToOne
	@JoinColumn(name = "patientreceiptitem")
	public PatientReceiptItem getPatientReceiptItem() {
		return patientReceiptItem;
	}

	public void setPatientReceiptItem(PatientReceiptItem patientReceiptItem) {
		this.patientReceiptItem = patientReceiptItem;
	}*/

	public void addResource(Resource resource) {
		if (resource == null) {
			return;
		}
		getResources().add(resource);
		resource.addTreatment(this);
	}
	
	public void addAttachment(Attachment attachment) {
		getAttachments().add(attachment);
	}
	
	private void addAppointment(Appointment appointment) {
		if (appointment == null) {
			return;
		}
		this.appointment = appointment;
		appointment.addTreatment(this);
	}
	
	private void addTreatmentType(TreatmentType treatmentType) {
		if (treatmentType == null) {
			return;
		}
		this.treatmentType = treatmentType;
		treatmentType.addTreatment(this);
	}
	
	@Transient
	public boolean isPaid() {
		if (getPatientBillItem() == null) {
			return false;
		}
		return getPatientBillItem().isPaid();
	}
}
