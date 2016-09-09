package ehc.bo.impl;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Voucher extends BaseObject {
	private Money price;
	private Money value;
	private Date expirationDate;
	
	protected Voucher() {
		super();
	}
	
	public Voucher(User executor) {
		super(executor);
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "price")
	public Money getPrice() {
		return price;
	}
	
	public void setPrice(Money price) {
		this.price = price;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "value")
	public Money getValue() {
		return value;
	}
	
	public void setValue(Money value) {
		this.value = value;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}
	
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}	
}
