package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity 
@PrimaryKeyJoinColumn(name = "id")
public class Party extends ResourceImpl {
	String name;
	List<PartyRole> sourceRoles = new ArrayList<PartyRole>();
	List<PartyRole> targetRoles = new ArrayList<PartyRole>();
	
	
	protected Party() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Party(User executor, String name) {
		super(executor);
	    this.name = name;
	}
		
    @OneToMany(mappedBy = "source", orphanRemoval = true)
	public List<PartyRole> getSourceRoles() {
		return sourceRoles;
	}

	public void setSourceRoles(List<PartyRole> roles) {
		this.sourceRoles = roles;
	}
	
    @OneToMany(mappedBy = "target", orphanRemoval = true)
	public List<PartyRole> getTargetRoles() {
		return targetRoles;
	}

	public void setTargetRoles(List<PartyRole> roles) {
		this.targetRoles = roles;
	}
	
	public void AddTargetRole(PartyRole role) {
		if (role == null) return;
		
		targetRoles.add(role);
		/*role.setTarget(this);*/
	}
	
	public void AddSourceRole(PartyRole role) {
		if (role == null) return;
		
		sourceRoles.add(role);
	/*	role.setSource(this);*/
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

}
