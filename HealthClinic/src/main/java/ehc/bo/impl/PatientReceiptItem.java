package ehc.bo.impl;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class PatientReceiptItem extends BaseObject {
	private PatientReceipt patientReceipt;
	private Money paidTreatmentPrice;
	private String treatmentTypeName;
	/*private Treatment treatment;*/
	
	protected PatientReceiptItem() {
		super();
	}
	
	public PatientReceiptItem(User executor, Money paidTreatmentPrice, String treatmentTypeName) {
		super(executor);
		this.paidTreatmentPrice = new Money(paidTreatmentPrice);
		this.treatmentTypeName = treatmentTypeName;
	}
	
	@ManyToOne
	@JoinColumn(name = "patientreceipt")
	public PatientReceipt getPatientReceipt() {
		return patientReceipt;
	}
	public void setPatientReceipt(PatientReceipt patientReceipt) {
		this.patientReceipt = patientReceipt;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "paidtreatmentprice")
	public Money getPaidTreatmentPrice() {
		return paidTreatmentPrice;
	}
	public void setPaidTreatmentPrice(Money paidTreatmentPrice) {
		this.paidTreatmentPrice = paidTreatmentPrice;
	}
	public String getTreatmentTypeName() {
		return treatmentTypeName;
	}
	public void setTreatmentTypeName(String treatmentTypeName) {
		this.treatmentTypeName = treatmentTypeName;
	}
	
/*	@OneToOne
	@JoinColumn(name = "treatment")
	public Treatment getTreatment() {
		return treatment;
	}
	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}*/
}
