package ehc.bo.impl;

import java.util.Date;

public class AccountUtil {
	public ExecutorReceipt createAccount(User accountCreator, ResourcePartyRole executor, Date from, Date to) {
		ExecutorReceipt executorAccount = new ExecutorReceipt(accountCreator, executor, from, to);
		
		for (Treatment treatment : executor.getTreatments()) {
			if ((treatment.getFrom().after(from) || from.equals(treatment.getFrom())) && 
					(treatment.getTo().before(to) || to.equals(treatment.getTo()))) {
				executorAccount.addAccountItem(accountCreator, treatment);			
			}		
		}
		return executorAccount;
	}
}
