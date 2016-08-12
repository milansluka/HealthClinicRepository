package ehc.bo.impl;

import java.math.BigDecimal;
import java.util.Currency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Money {
	private static int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

	private long id;
	private BigDecimal amount;
	private Currency currency;

	public Money() {
		this(0);
	}

	public Money(double amount) {
		this(new BigDecimal(amount));
	}

	public Money(BigDecimal amount) {
		this(amount, Currency.getInstance(HealthPoint.DEFAULT_CURRENCY_CODE));
	}

	public Money(double amount, Currency currency) {
		this(new BigDecimal(amount), currency);
	}

	public Money(BigDecimal amount, Currency currency) {
		super();
		int scale = currency.getDefaultFractionDigits();
		this.amount = amount.setScale(scale, ROUNDING_MODE);
		this.currency = currency;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	protected void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void add(Money money) {
		if (getCurrency().equals(money.getCurrency())) {
			setAmount(getAmount().add(money.getAmount()));
		}
	}

	public Money multiply(Money money) {
		if (getCurrency().equals(money.getCurrency())) {
			return new Money(getAmount().multiply(money.getAmount()), getCurrency());
			/* setAmount(getAmount().multiply(money.getAmount())); */
		}
		return null;
	}

	public Money getPercentage(double percentage) {
		return new Money(getAmount().multiply(new BigDecimal(percentage)), getCurrency());
	}

	public boolean greaterThanOrEqual(Money money) {
		if (getCurrency().equals(money.getCurrency())) {
			return getAmount().compareTo(money.getAmount()) >= 0;
		}
		return false;
	}

	public boolean lessThan(Money money) {
		if (getCurrency().equals(money.getCurrency())) {
			return getAmount().compareTo(money.getAmount()) < 0;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		Money money = (Money) obj;
		if (getCurrency().equals(money.getCurrency())) {
			return getAmount().equals(money.getAmount());
		}
		return false;
	}

}
