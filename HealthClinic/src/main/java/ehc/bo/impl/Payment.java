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
	

	/*private Appointment appointment;*/
   /* private List<Treatment> treatments = new ArrayList<>();*/
/*    private List<PatientBill> bills = new ArrayList<>();*/
	private List<PatientBillItem> billItemsToPay = new ArrayList<PatientBillItem>();
    private Money paidAmount;
    private PaymentChannel paymentChannel;
    private PatientReceipt receipt;
      
    protected Payment() {
		super();
	}
    
	public Payment(User executor, List<PatientBillItem> billItemsToPay, PaymentChannel paymentChannel, Money paidAmount) {
		super(executor);
		this.paidAmount = new Money(paidAmount);
		addBillItemsToPay(billItemsToPay);
		assignPaymentChannel(paymentChannel);
	}
	
/*	public Payment(User executor, Appointment appointment, List<Treatment> treatmentsToPay, PaymentChannel paymentChannel, Money paidAmount) {
		super(executor);
		this.paidAmount = paidAmount;
		addTreatments(treatmentsToPay);
		assignAppointment(appointment);
		assignPaymentChannel(paymentChannel);
	}*/

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "paidamount")
	public Money getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Money paidAmount) {
		this.paidAmount = paidAmount;
	}

/*	@ManyToOne
    @JoinColumn(name = "appointment")
	public Appointment getAppointment() {
		return appointment;
	}
	
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}*/
	
	@Transient
	public Party getPayer() {
		return getPaymentChannel().getParty();
	}

	@ManyToOne
	@JoinColumn(name = "paymentchannel")
	public PaymentChannel getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(PaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
	}	

/*	@OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
	public List<Treatment> geTreatments() {
		return treatments;
	}
	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}*/
	
/*	@OneToMany(mappedBy = "payment")
	public List<PatientBill> getBills() {
		return bills;
	}

	public void setBills(List<PatientBill> bills) {
		this.bills = bills;
	}*/

	@OneToMany(mappedBy = "payment")
	public List<PatientBillItem> getBillItemsToPay() {
		return billItemsToPay;
	}

	public void setBillItemsToPay(List<PatientBillItem> billItemsToPay) {
		this.billItemsToPay = billItemsToPay;
	}

	@OneToOne
	@JoinColumn(name = "receipt")
	public PatientReceipt getReceipt() {
		return receipt;
	}

	public void setReceipt(PatientReceipt receipt) {
		this.receipt = receipt;
	}

	public void assignAppointment(Appointment appointment) {
/*		if (appointment == null) {
			return;
		}
		this.appointment = appointment;
		appointment.addPayment(this);*/	
	}
	
	public void assignPaymentChannel(PaymentChannel paymentChannel) {
		if (paymentChannel == null) {
			return;
		}
		this.paymentChannel = paymentChannel;
		paymentChannel.addPayment(this);	
	}
	
	public void addBillItem(PatientBillItem billItem) {
		if (billItem == null) {
			return;
		}
		getBillItemsToPay().add(billItem);
		billItem.setPayment(this);
	}
	
	public void addBillItemsToPay(List<PatientBillItem> patientBillItems) {
		for (PatientBillItem patientBillItem : patientBillItems) {
			addBillItem(patientBillItem);
		}
	}
	
/*	public void addTreatment(Treatment treatment) {
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
	}*/
	
	@Transient
	public boolean isSufficient() {
		Money neededAmount = new Money();
		for (PatientBillItem billItem : getBillItemsToPay()) {
			neededAmount.add(billItem.getPrice());		
		}	
		return paidAmount.greaterThanOrEqual(neededAmount);
	}
	
/*	@Transient
	public boolean isSufficient() {
		Money neededAmount = new Money();
		for (Treatment treatment : getTreatments()) {
			neededAmount.add(treatment.getPrice());		
		}
		
		return paidAmount.greaterThanOrEqual(neededAmount);
	}*/
	
	@Transient
	public Date getDateOfExecution() {
		return getCreatedOn();
	}
}
