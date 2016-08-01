package ehc.bo.impl;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("2")
public class Phone extends CommunicationChannel {
		
	protected Phone() {
		super();
	}

	public Phone(User executor, String value, Date validFrom, Party party) {
		super(executor, value, validFrom, party);
	}
	
	public void setPhoneNumber(String phoneNumber) {
		setValue(phoneNumber);
	}
	
	@Transient
	public String getPhoneNumber() {
		return getValue();
	}
}
