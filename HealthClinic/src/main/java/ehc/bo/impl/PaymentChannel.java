package ehc.bo.impl;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_channel")
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
	@JoinColumn(name = "party_id")
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
