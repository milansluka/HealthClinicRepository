package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class PatientReceipt extends BaseObject {
	private String physicianFirstName;
	private String physicianLastName;
	private List<PatientReceiptItem> items = new ArrayList<PatientReceiptItem>();
	private PaymentChannelType paymentChannelType;
	
	protected PatientReceipt() {
		super();
	}
	
	public PatientReceipt(User executor, String physicianFirstName, String physicianLastName, Appointment appointment,
			PaymentChannelType paymentChannelType) {
		super(executor);
		this.physicianFirstName = physicianFirstName;
		this.physicianLastName = physicianLastName;
		this.paymentChannelType = paymentChannelType;
	}

	@Transient
	public Money getTotalPayedPrice() {
		Money totalPricePayed = new Money();
		
		for (PatientReceiptItem patientReceiptItem : getItems()) {
			totalPricePayed = totalPricePayed.add(patientReceiptItem.getPaidTreatmentPrice());
		}
		return totalPricePayed;
	}

	public String getPhysicianFirstName() {
		return physicianFirstName;
	}

	public void setPhysicianFirstName(String physicianFirstName) {
		this.physicianFirstName = physicianFirstName;
	}

	public String getPhysicianLastName() {
		return physicianLastName;
	}

	public void setPhysicianLastName(String physicianLastName) {
		this.physicianLastName = physicianLastName;
	}

	@OneToMany(mappedBy = "patientReceipt", cascade = CascadeType.ALL)
	public List<PatientReceiptItem> getItems() {
		return items;
	}

	public void setItems(List<PatientReceiptItem> items) {
		this.items = items;
	}

	@Enumerated(EnumType.STRING)
	public PaymentChannelType getPaymentChannelType() {
		return paymentChannelType;
	}

	public void setPaymentChannelType(PaymentChannelType paymentChannelType) {
		this.paymentChannelType = paymentChannelType;
	}
	
	public void addItem(PatientReceiptItem patientReceiptItem) {
		if (patientReceiptItem == null) {
			return;
		}
		getItems().add(patientReceiptItem);	
		patientReceiptItem.setPatientReceipt(this);
	}
	
	public void addItems(List<PatientReceiptItem> items) {
		for (PatientReceiptItem patientReceiptItem : items) {
			addItem(patientReceiptItem);
		}
	}
	
	public void addPaidBills(List<PatientBillItem> billItems) {
		for (PatientBillItem patientBillItem : billItems) {
			PatientReceiptItem patientReceiptItem = new PatientReceiptItem(getCreatedBy(), 
					patientBillItem.getPrice(), patientBillItem.getName());
			addItem(patientReceiptItem);
		}
	}
}
