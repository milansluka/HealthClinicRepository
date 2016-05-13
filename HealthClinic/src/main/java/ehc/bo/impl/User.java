package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@PrimaryKeyJoinColumn(name="party_role_id") 
public class User extends PartyRole {
	

	List<PermissionProfile> permissionProfiles;
	
	String login;
	String password;
	
	protected User() {
		super();
		permissionProfiles = new ArrayList<PermissionProfile>();
	}
	
	public User(User executor, String login, String password) {
		super(executor);
		this.login = login;
		this.password = password;
	}
	

	@ManyToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinTable(name = "assigned_permission_profile", joinColumns = @JoinColumn(name = "user_id") , inverseJoinColumns = @JoinColumn(name = "permission_profile_id") )
	public List<PermissionProfile> getPermissionProfiles() {
		return permissionProfiles;
	}

	public void setPermissionProfiles(List<PermissionProfile> permissionProfiles) {
		this.permissionProfiles = permissionProfiles;
	}
	

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
	
}
