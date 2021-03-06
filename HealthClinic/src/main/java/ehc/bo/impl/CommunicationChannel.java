package ehc.bo.impl;

import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator")
public class CommunicationChannel extends ModifiableObject {
	private Date validFrom;
	private Date validTo;
	private String value;
	private Party party;
	
	protected CommunicationChannel() {
		super();
	}

	public CommunicationChannel(User executor, String value, Date validFrom, Party party) {
		super(executor);
		this.value = value;
		this.validFrom = validFrom;
		this.party = party;
	}
	
	
    @ManyToOne
    @JoinColumn(name = "party")
	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
