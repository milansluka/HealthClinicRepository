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
@Inheritance(strategy = InheritanceType.JOINED)
public class PaymentChannel extends ModifiableObject {
	private Party party;
	private List<Payment> payments = new ArrayList<>();
	
	protected PaymentChannel() {
		super();
	}

	public PaymentChannel(User executor, Party party) {
		super(executor);
		this.party = party;
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

	public void addPayment(Payment payment) {
		getPayments().add(payment);
	}
}
