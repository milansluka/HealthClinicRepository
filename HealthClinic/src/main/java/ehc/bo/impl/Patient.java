package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Patient extends PartyRole {
	private List<Voucher> vouchers = new ArrayList<>();

	protected Patient() {
		super();
	}

	public Patient(User executor, Party source, Party target) {
		super(executor, source, target);
	}

	public List<Voucher> getVouchers() {
		return vouchers;
	}
	
	@Transient
	public Money getDebt() {
		return null;	
	}

	public void setVouchers(List<Voucher> vouchers) {
		this.vouchers = vouchers;
	}
}
