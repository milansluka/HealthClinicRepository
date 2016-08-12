package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy=InheritanceType.JOINED) 
public class Payment extends ModifiableObject {
	private Appointment appointment;
    private List<Treatment> treatments = new ArrayList<>();
   /* private double paidAmount = 0;*/
    private Money paidAmount;
/*    private Party payer;*/
    private PaymentChannel paymentChannel;
      
    protected Payment() {
		super();
	}
    
/*	public Payment(User executor, Appointment appointment, List<Treatment> treatmentsForPay, PaymentChannel paymentChannel, double paidSum) {
		super(executor);
		assignAppointment(appointment);
		assignPaymentChannel(paymentChannel);
		addTreatments(treatmentsForPay);
		this.paidAmount = paidSum;
	}*/
	
	public Payment(User executor, Appointment appointment, List<Treatment> treatmentsForPay, PaymentChannel paymentChannel, Money paidAmount) {
		super(executor);
		assignAppointment(appointment);
		assignPaymentChannel(paymentChannel);
		addTreatments(treatmentsForPay);
		this.paidAmount = paidAmount;
	}
	
	
	
/*	@Column(name = "paid_amount")
	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}*/

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "paid_amount")
	public Money getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Money paidAmount) {
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
	
	@Transient
	public Party getPayer() {
		return getPaymentChannel().getParty();
	}

	@ManyToOne
	@JoinColumn(name = "payment_channel_id")
	public PaymentChannel getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(PaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
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
	
	public void assignPaymentChannel(PaymentChannel paymentChannel) {
		if (paymentChannel == null) {
			return;
		}
		this.paymentChannel = paymentChannel;
		paymentChannel.addPayment(this);	
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
		Money neededAmount = new Money();
		for (Treatment treatment : geTreatments()) {
			neededAmount.add(treatment.getPrice());		
		}
		
		return paidAmount.greaterThanOrEqual(neededAmount);
	}
	
	@Transient
	public Date getDateOfExecution() {
		return getCreatedOn();
	}
}
