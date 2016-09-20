package ehc.bo.impl;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class PatientBillItem extends BaseObject {
	private PatientBill patientBill;
	private String name;
	private Money price;
	private Payment payment;
	private Treatment treatment;

	
	protected PatientBillItem() {
		super();
	}
		
	public PatientBillItem(User executor, String name, Money price, Treatment treatment) {
		super(executor);
		this.name = name;
		this.price = new Money(price);
		this.treatment = treatment;
		treatment.setPatientBillItem(this);
	}

	@ManyToOne
	@JoinColumn(name = "patientbill")
	public PatientBill getPatientBill() {
		return patientBill;
	}
	public void setPatientBill(PatientBill patientBill) {
		this.patientBill = patientBill;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "price")
	public Money getPrice() {
		return price;
	}
	public void setPrice(Money price) {
		this.price = price;
	}

	@ManyToOne
	@JoinColumn(name = "payment")
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	@OneToOne
	@JoinColumn(name = "treatment")
	public Treatment getTreatment() {
		return treatment;
	}

	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}
	
	@Transient
	public boolean isPaid() {
		if (getPayment() == null) {
			return false;
		}
		return getPayment().isSufficient();
	}
}
