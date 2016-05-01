package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@PrimaryKeyJoinColumn(name="party_role_id") 
public class User extends PartyRole {
	

	List<PermissionProfile> permissionProfiles;
/*	List<BaseObject> createdObjects;
	List<ModificableObject> modifiedObjects;*/
	
	String login;
	String password;
	
	public User() {
		super();
		permissionProfiles = new ArrayList<PermissionProfile>();
	}

	@ManyToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinTable(name = "assigned_permission_profile", joinColumns = @JoinColumn(name = "user_id") , inverseJoinColumns = @JoinColumn(name = "permission_profile_id") )
	public List<PermissionProfile> getPermissionProfiles() {
		return permissionProfiles;
	}

	public void setPermissionProfiles(List<PermissionProfile> permissionProfiles) {
		this.permissionProfiles = permissionProfiles;
	}
	
	
/*    @OneToMany(mappedBy = "createdBy")
	public List<BaseObject> getCreatedObjects() {
		return createdObjects;
	}

	public void setCreatedObjects(List<BaseObject> createdObjects) {
		this.createdObjects = createdObjects;
	}*/
	
	
/*	@OneToMany(mappedBy = "modifiedBy")
	public List<ModificableObject> getModifiedObjects() {
		return modifiedObjects;
	}

	public void setModifiedObjects(List<ModificableObject> modifiedObjects) {
		this.modifiedObjects = modifiedObjects;
	}*/

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean hasPermission(Permission permission) {
	/*	return permissions.contains(right);*/
		
		return true;
		
	}
	
	public void assignPermissionProfile(PermissionProfile permissionProfile) {
		if (permissionProfile != null) {
			permissionProfile.getUsers().add(this);
			getPermissionProfiles().addAll(permissionProfiles);
		}
	}
	
/*	public void assignRight(Permission right) {
		if (right != null) {
			right.getUsers().add(this);
			getRights().add(right);
			
		}
	}*/
	
}
