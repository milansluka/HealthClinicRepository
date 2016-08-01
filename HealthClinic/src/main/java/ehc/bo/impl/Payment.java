package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy=InheritanceType.JOINED) 
public class Payment extends ModifiableObject {
	private Appointment appointment;
    private List<Treatment> treatments = new ArrayList<>();
    private double paidAmount = 0;
      
    protected Payment() {
		super();
	}
    
	public Payment(User executor, Appointment appointment, List<Treatment> treatmentsForPay, double paidSum) {
		super(executor);
		assignAppointment(appointment);
		addTreatments(treatmentsForPay);
		this.paidAmount = paidSum;
	}
	
	@Column(name = "paid_amount")
	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	@ManyToOne
    @JoinColumn(name = "appointment_id")
	public Appointment getAppointment() {
		return appointment;
	}
	
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	
	@OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
	public List<Treatment> geTreatments() {
		return treatments;
	}
	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}
	
	public void assignAppointment(Appointment appointment) {
		if (appointment == null) {
			return;
		}
		this.appointment = appointment;
		appointment.addPayment(this);	
	}
	
	public void addTreatment(Treatment treatment) {
		if (treatment == null) {
			return;
		}
		geTreatments().add(treatment);
		treatment.setPayment(this);
	}
	
	public void addTreatments(List<Treatment> treatments) {
		for (Treatment treatment : treatments) {
			addTreatment(treatment);
		}
	}
	
	@Transient
	public boolean isSufficient() {
		double neededAmount = 0;
		for (Treatment treatment : geTreatments()) {
			neededAmount += treatment.getPrice();		
		}
		
		return paidAmount >= neededAmount;
	}
}
