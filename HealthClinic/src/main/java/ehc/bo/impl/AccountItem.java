package ehc.bo.impl;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class AccountItem extends BaseObject {
	/*private TreatmentType treatmentType;*/
	private Date from;
	private Date to;
	private Money treatmentPrice;
	private PaymentChannel paymentChannel;
	private double executorProvisionPercentage;
	
	private String subjectFirstName;
	private String subjectLastName;
	private String treatmentTypeName;
	private String treatmentGroupName;
	private boolean withDph;
	
	private ExecutorAccount executorAccount;
	
	protected AccountItem() {
		super();
	}
	
	public AccountItem(User accountItemCreator, Treatment treatment, ResourcePartyRole executor, ExecutorAccount executorAccount, boolean withDPH) {
		super(accountItemCreator);
		this.treatmentTypeName = treatment.getTreatmentType().getName();
		this.treatmentGroupName = treatment.getTreatmentType().getTreatmentGroup().getName();
		this.from = treatment.getFrom();
		this.to = treatment.getTo();
		this.treatmentPrice = new Money(treatment.getPrice());
		this.paymentChannel = treatment.getPayment().getPaymentChannel();
		this.subjectFirstName = treatment.getAppointment().getIndividual().getFirstName();
		this.subjectLastName = treatment.getAppointment().getIndividual().getName();
		this.executorProvisionPercentage = executor.getProvisionFromTreatmentType(treatment.getTreatmentType());
		this.executorAccount = executorAccount;
		this.withDph = withDPH;
	}
	
	@ManyToOne
	@JoinColumn(name = "executoraccount")
	public ExecutorAccount getExecutorAccount() {
		return executorAccount;
	}

	public void setExecutorAccount(ExecutorAccount executorAccount) {
		this.executorAccount = executorAccount;
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

	@OneToOne
	@JoinColumn(name = "paymentchannel")
	public PaymentChannel getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(PaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
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
