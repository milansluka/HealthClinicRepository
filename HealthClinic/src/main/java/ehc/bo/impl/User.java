package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "`user`")
@PrimaryKeyJoinColumn(name="id") 
public class User extends PartyRole {
/*	List<PermissionProfile> permissionProfiles;*/
	String login;
	String password;
	
	protected User() {
		super();
	/*	permissionProfiles = new ArrayList<PermissionProfile>();*/
	}

	public User(User executor, String login, String password, Party source, Party target) {
		super(executor, source, target);
		/*permissionProfiles = new ArrayList<PermissionProfile>();*/
		this.login = login;
		this.password = password;
	}

/*	@ManyToMany
	@JoinTable(name = "assigned_permission_profile", joinColumns = @JoinColumn(name = "user_id") , inverseJoinColumns = @JoinColumn(name = "permission_profile_id") )
	public List<PermissionProfile> getPermissionProfiles() {
		return permissionProfiles;
	}

	public void setPermissionProfiles(List<PermissionProfile> permissionProfiles) {
		this.permissionProfiles = permissionProfiles;
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
	
/*	public void assignPermissionProfile(PermissionProfile permissionProfile) {
		if (permissionProfile != null) {
			permissionProfile.getUsers().add(this);
			getPermissionProfiles().addAll(permissionProfiles);
		}
	}*/
	
}
