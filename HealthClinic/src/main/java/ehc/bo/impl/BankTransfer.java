package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class BankTransfer extends PaymentChannel {
	private String sortCode;
	private String accountNumber;
	
	protected BankTransfer() {
		super();
	}
	public BankTransfer(User executor, Party party, String sortCode, String accountNumber) {
		super(executor, party);
		this.sortCode = sortCode;
		this.accountNumber = accountNumber;
	}
	
	public String getSortCode() {
		return sortCode;
	}
	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
}
