package ehc.bo.impl;

public enum PaymentChannelType {
	CASH("cash"), BANK_TRANSFER("bank transfer"), CREDIT_CARD("credit card"); 
	
	private String name;
	
	private PaymentChannelType(String name) {
		this.name = name;	
	}
	
	@Override
	public String toString() {
		return name;
	}
}
