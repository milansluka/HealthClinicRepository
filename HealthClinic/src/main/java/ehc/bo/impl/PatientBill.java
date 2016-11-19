package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class PatientBill extends BaseObject {
	private List<PatientBillItem> items = new ArrayList<PatientBillItem>();
	private Money totalPrice;
	private double VATrate;
	private Appointment appointment;
	
	protected PatientBill() {
		super();
	}
	
	public PatientBill(User executor, Money totalPrice, double vATrate, Appointment appointment) {
		super(executor);
		this.totalPrice = new Money(totalPrice);
		VATrate = vATrate;
		this.appointment = appointment;
		/*appointment.addPatientBill(this);*/
		appointment.setPatientBill(this);
	}

	@OneToMany(mappedBy = "patientBill", cascade = CascadeType.ALL)
	public List<PatientBillItem> getItems() {
		return items;
	}
	public void setItems(List<PatientBillItem> items) {
		this.items = items;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "totalprice")
	public Money getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Money totalPrice) {
		this.totalPrice = totalPrice;
	}
	public double getVATrate() {
		return VATrate;
	}
	public void setVATrate(double vATrate) {
		VATrate = vATrate;
	} 
	
	@OneToOne
	@JoinColumn(name = "appointment")
	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	
/*	@OneToOne
	@JoinColumn(name = "payment")
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}*/

	public void addItem(PatientBillItem item) {
		if (item == null) {
			return;
		}
		getItems().add(item);
		item.setPatientBill(this);
	}
	
	@Transient
	public boolean isPaid() {
		for (PatientBillItem patientBillItem : getItems()) {
			if (!patientBillItem.isPaid()) {
				return false;			
			}
		}
		return true;	
	}	
}
