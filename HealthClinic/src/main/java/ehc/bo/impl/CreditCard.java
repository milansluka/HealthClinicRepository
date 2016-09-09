package ehc.bo.impl;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CreditCard extends PaymentChannel {
	private String cardNumber;
	private Date cardExpiry;
	
	protected CreditCard() {
		super();
	}
	
	public CreditCard(User executor, Party party, String cardNumber, Date cardExpiry) {
		super(executor, party, PaymentChannelType.CREDIT_CARD);
		this.cardNumber = cardNumber;
		this.cardExpiry = cardExpiry;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public Date getCardExpiry() {
		return cardExpiry;
	}
	public void setCardExpiry(Date cardExpiry) {
		this.cardExpiry = cardExpiry;
	}
}
