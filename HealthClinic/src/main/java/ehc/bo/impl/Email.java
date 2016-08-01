package ehc.bo.impl;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("1")
public class Email extends CommunicationChannel {
		
	protected Email() {
		super();
	}

	public Email(User executor, String value, Date validFrom, Party party) {
		super(executor, value, validFrom, party);
	}
	
	public void setEmailAddress(String address) {
		setValue(address);
	}

	@Transient
	public String getEmailAddress() {
		return getValue();
	}
}
