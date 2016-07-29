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

import ehc.bo.Resource;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Treatment extends ModifiableObject {
	private Date from;
	private Date to;
	private Appointment appointment;
	private TreatmentType treatmentType;
	private List<Resource> resources = new ArrayList<>();
	private double price;
	
	protected Treatment() {
		super();
	}
	
	public Treatment(User executor, Appointment appointment, TreatmentType treatmentType, double price, Date from, Date to) {
		super(executor);
		addAppointment(appointment);
		addTreatmentType(treatmentType);
		this.price = price;
		this.from = from;
		this.to = to;
	}

	@ManyToOne
	@JoinColumn(name = "appointment_id")
	public Appointment getAppointment() {
		return appointment;
	}
	
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
		
	@ManyToOne
	@JoinColumn(name = "treatment_type_id")
	public TreatmentType getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = ResourceImpl.class)
	@JoinTable(name = "resource_treatment", joinColumns = {@JoinColumn(name = "treatment_id")},
	inverseJoinColumns = {@JoinColumn(name = "resource_id")})
	public List<Resource> getResources() {
		return resources;
	}
	
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
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
	
	public void addResource(Resource resource) {
		if (resource == null) {
			return;
		}
		getResources().add(resource);
		resource.addTreatment(this);
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
}
