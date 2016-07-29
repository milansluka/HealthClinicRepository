package ehc.bo.impl;

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
	String name;
	List<PartyRole> sourceRoles = new ArrayList<>();
	List<PartyRole> targetRoles = new ArrayList<>();
	List<ResourcePartyRole> reservableSourceRoles = new ArrayList<>();
	List<ResourcePartyRole> reservableTargetRoles = new ArrayList<>();

	protected Party() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Party(User executor, String name) {
		super(executor);
		this.name = name;
	}

	/*@OneToMany(mappedBy = "source", orphanRemoval = true)*/
	@OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
	public List<PartyRole> getSourceRoles() {
		return sourceRoles;
	}

	public void setSourceRoles(List<PartyRole> roles) {
		this.sourceRoles = roles;
	}

/*	@OneToMany(mappedBy = "target", orphanRemoval = true)*/
	@OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
	public List<PartyRole> getTargetRoles() {
		return targetRoles;
	}

	public void setTargetRoles(List<PartyRole> roles) {
		this.targetRoles = roles;
	}

/*	@OneToMany(mappedBy = "source", orphanRemoval = true)*/
	@OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
	public List<ResourcePartyRole> getReservableSourceRoles() {
		return reservableSourceRoles;
	}

	public void setReservableSourceRoles(List<ResourcePartyRole> reservableSourceRoles) {
		this.reservableSourceRoles = reservableSourceRoles;
	}

/*	@OneToMany(mappedBy = "target", orphanRemoval = true)*/
	@OneToMany(mappedBy = "target", cascade = CascadeType.ALL)
	public List<ResourcePartyRole> getReservableTargetRoles() {
		return reservableTargetRoles;
	}

	public void setReservableTargetRoles(List<ResourcePartyRole> reservableTargetRoles) {
		this.reservableTargetRoles = reservableTargetRoles;
	}

	public void addTargetRole(PartyRole role) {
		if (role == null)
			return;

		targetRoles.add(role);
		/* role.setTarget(this); */
	}

	public void addSourceRole(PartyRole role) {
		if (role == null)
			return;

		sourceRoles.add(role);
		/* role.setSource(this); */
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
