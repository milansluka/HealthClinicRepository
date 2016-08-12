package ehc.bo.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "bank_transfer")
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
	
	@Column(name = "sort_code")
	public String getSortCode() {
		return sortCode;
	}
	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}
	
	@Column(name = "account_number")
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
}
