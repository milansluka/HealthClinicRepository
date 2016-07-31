package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy=InheritanceType.JOINED) 
public class Payment extends ModifiableObject {
	private Appointment appointment;
    private List<Treatment> payedTreatments = new ArrayList<>();
      
    protected Payment() {
		super();
	}
    
	public Payment(User executor, Appointment appointment) {
		super(executor);
		assignAppointment(appointment);
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
	public List<Treatment> getPayedTreatments() {
		return payedTreatments;
	}
	public void setPayedTreatments(List<Treatment> payedTreatments) {
		this.payedTreatments = payedTreatments;
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
		getPayedTreatments().add(treatment);
		treatment.setPayment(this);
	}
}
