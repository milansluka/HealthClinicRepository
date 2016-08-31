package ehc.bo.impl;

//TestAndG.dot

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Party extends ModifiableObject implements Comparable<Party> {
	private String name;
	private List<PartyRole> sourceRoles = new ArrayList<>();
	private List<PartyRole> targetRoles = new ArrayList<>();
	private List<ResourcePartyRole> reservableSourceRoles = new ArrayList<>();
	private List<ResourcePartyRole> reservableTargetRoles = new ArrayList<>();
	private List<CommunicationChannel> communicationChannels = new ArrayList<>();
	private List<PaymentChannel> paymentChannels = new ArrayList<>();

	protected Party() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Party(User executor, String name) {
		super(executor);
		this.name = name;
	}

	@OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
	public List<PartyRole> getSourceRoles() {
		return sourceRoles;
	}

	public void setSourceRoles(List<PartyRole> roles) {
		this.sourceRoles = roles;
	}

	@OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
	public List<PartyRole> getTargetRoles() {
		return targetRoles;
	}

	public void setTargetRoles(List<PartyRole> roles) {
		this.targetRoles = roles;
	}

	@OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
	public List<ResourcePartyRole> getReservableSourceRoles() {
		return reservableSourceRoles;
	}

	public void setReservableSourceRoles(List<ResourcePartyRole> reservableSourceRoles) {
		this.reservableSourceRoles = reservableSourceRoles;
	}

	@OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
	public List<ResourcePartyRole> getReservableTargetRoles() {
		return reservableTargetRoles;
	}

	public void setReservableTargetRoles(List<ResourcePartyRole> reservableTargetRoles) {
		this.reservableTargetRoles = reservableTargetRoles;
	}
	
	@OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
	public List<CommunicationChannel> getCommunicationChannels() {
		return communicationChannels;
	}

	public void setCommunicationChannels(List<CommunicationChannel> communicationChannels) {
		this.communicationChannels = communicationChannels;
	}
	
	@OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
	public List<PaymentChannel> getPaymentChannels() {
		return paymentChannels;
	}

	public void setPaymentChannels(List<PaymentChannel> paymentChannels) {
		this.paymentChannels = paymentChannels;
	}

	public void addTargetRole(PartyRole role) {
		if (role == null)
			return;

		targetRoles.add(role);
	}

	public void addSourceRole(PartyRole role) {
		if (role == null)
			return;

		sourceRoles.add(role);
	}

	public void addReservableTargetRole(ResourcePartyRole role) {
		if (role == null)
			return;
		reservableTargetRoles.add(role);
	}

	public void addReservableSourceRole(ResourcePartyRole role) {
		if (role == null)
			return;
		reservableSourceRoles.add(role);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addCommunicationChannel(CommunicationChannel communicationChannel) {
		getCommunicationChannels().add(communicationChannel);
	}
	
	public void addPaymentChannel(PaymentChannel paymentChannel) {
		getPaymentChannels().add(paymentChannel);
	}

	@Override
	public int compareTo(Party o) {
		return getName().compareTo(o.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Party other = (Party) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
