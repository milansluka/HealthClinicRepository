package ehc.bo.impl;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ExecutorReceiptItem extends BaseObject {
	/* private TreatmentType treatmentType; */
	private Date from;
	private Date to;
	private Money treatmentPrice;
	private PaymentChannelType paymentChannelType;
	private double executorProvisionPercentage;

	private String subjectFirstName;
	private String subjectLastName;
	private String treatmentTypeName;
	private String treatmentGroupName;
	private boolean withDph;

	private ExecutorReceipt executorReceipt;

	protected ExecutorReceiptItem() {
		super();
	}

	public ExecutorReceiptItem(User accountItemCreator, Treatment treatment, ResourcePartyRole executor,
			ExecutorReceipt executorReceipt, boolean withDPH) {
		super(accountItemCreator);
		this.treatmentTypeName = treatment.getTreatmentType().getName();
		this.treatmentGroupName = treatment.getTreatmentType().getTreatmentGroup().getName();
		this.from = treatment.getFrom();
		this.to = treatment.getTo();
		this.treatmentPrice = new Money(treatment.getPrice());
		this.paymentChannelType = treatment.getPatientBillItem().getPayment().getPaymentChannel().getType();
		this.subjectFirstName = treatment.getAppointment().getIndividual().getFirstName();
		this.subjectLastName = treatment.getAppointment().getIndividual().getName();
		this.executorProvisionPercentage = executor.getProvisionFromTreatmentType(treatment.getTreatmentType());
		this.executorReceipt = executorReceipt;
		this.withDph = withDPH;
	}

	@ManyToOne
	@JoinColumn(name = "executorreceipt")
	public ExecutorReceipt getExecutorReceipt() {
		return executorReceipt;
	}

	public void setExecutorReceipt(ExecutorReceipt executorReceipt) {
		this.executorReceipt = executorReceipt;
	}

	public String getSubjectFirstName() {
		return subjectFirstName;
	}

	public void setSubjectFirstName(String subjectFirstName) {
		this.subjectFirstName = subjectFirstName;
	}

	public String getSubjectLastName() {
		return subjectLastName;
	}

	public void setSubjectLastName(String subjectLastName) {
		this.subjectLastName = subjectLastName;
	}

	public String getTreatmentTypeName() {
		return treatmentTypeName;
	}

	public void setTreatmentTypeName(String treatmentTypeName) {
		this.treatmentTypeName = treatmentTypeName;
	}

	public String getTreatmentGroupName() {
		return treatmentGroupName;
	}

	public void setTreatmentGroupName(String treatmentGroupName) {
		this.treatmentGroupName = treatmentGroupName;
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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "treatmentprice")
	public Money getTreatmentPrice() {
		return treatmentPrice;
	}

	public void setTreatmentPrice(Money treatmentPrice) {
		this.treatmentPrice = treatmentPrice;
	}

	@Enumerated(EnumType.STRING)
	public PaymentChannelType getPaymentChannelType() {
		return paymentChannelType;
	}

	public void setPaymentChannelType(PaymentChannelType paymentChannelType) {
		this.paymentChannelType = paymentChannelType;
	}

	public double getExecutorProvisionPercentage() {
		return executorProvisionPercentage;
	}

	public void setExecutorProvisionPercentage(double executorProvisionPercentage) {
		this.executorProvisionPercentage = executorProvisionPercentage;
	}

	@Transient
	public Money getExecutorProvisionAmount() {
		return new Money(treatmentPrice.getPercentage(getExecutorProvisionPercentage()));
	}

	public boolean isWithDPH() {
		return withDph;
	}

	public void setWithDPH(boolean withDPH) {
		this.withDph = withDPH;
	}
}
