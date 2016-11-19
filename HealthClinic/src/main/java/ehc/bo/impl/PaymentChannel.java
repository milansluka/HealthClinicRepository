package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class PaymentChannel extends ModifiableObject {
	private Party party;
	private List<Payment> payments = new ArrayList<Payment>();
	private PaymentChannelType type;
	
	protected PaymentChannel() {
		super();
	}

	public PaymentChannel(User executor, Party party, PaymentChannelType paymentChannelType) {
		super(executor);
		this.party = party;
		this.type = paymentChannelType;
	}

	@ManyToOne
	@JoinColumn(name = "party")
	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}
	
	@OneToMany(mappedBy = "paymentChannel", cascade = CascadeType.ALL)
	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	
	@Enumerated(EnumType.STRING)
	public PaymentChannelType getType() {
		return type;
	}

	public void setType(PaymentChannelType paymentChannelType) {
		this.type = paymentChannelType;
	}

	public void addPayment(Payment payment) {
		getPayments().add(payment);
	}

	@Override
	public String toString() {
		return "" + type;
	}	
}
