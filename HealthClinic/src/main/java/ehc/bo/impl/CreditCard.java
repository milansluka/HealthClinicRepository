package ehc.bo.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "credit_card")
@PrimaryKeyJoinColumn(name = "id")
public class CreditCard extends PaymentChannel {
	private String cardNumber;
	private Date cardExpiry;
	
	protected CreditCard() {
		super();
	}
	
	public CreditCard(User executor, Party party, String cardNumber, Date cardExpiry) {
		super(executor, party);
		this.cardNumber = cardNumber;
		this.cardExpiry = cardExpiry;
	}
	
	@Column(name = "card_number")
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	@Column(name = "card_expiry")
	public Date getCardExpiry() {
		return cardExpiry;
	}
	public void setCardExpiry(Date cardExpiry) {
		this.cardExpiry = cardExpiry;
	}
}
