package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)  
public class Party extends ModifiableObject {
	String name;
	List<PartyRole> sourceRoles;
	List<PartyRole> targetRoles;
	
	
	protected Party() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Party(User executor, String name) {
		super(executor);
	    this.name = name;
	    sourceRoles = new ArrayList<PartyRole>();
	    targetRoles = new ArrayList<PartyRole>();
		// TODO Auto-generated constructor stub
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
	
	public void AddTargetRole(PartyRole role) {
		if (role == null) return;
		
		targetRoles.add(role);
		role.setTarget(this);
	}
	
	public void AddSourceRole(PartyRole role) {
		if (role == null) return;
		
		sourceRoles.add(role);
		role.setSource(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

}
