package ehc.bo.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "communication_channel")
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
    @JoinColumn(name = "party_id")
	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	@Column(name = "valid_from")
	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	@Column(name = "valid_to")
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
	
	public void assignParty(Party party) {
		if (party == null) {
			return;
		}
		setParty(party);
		party.addCommunicationChannel(this);
	}

}
