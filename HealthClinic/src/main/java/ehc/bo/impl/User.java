package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "systemuser")
@PrimaryKeyJoinColumn(name="id") 
public class User extends PartyRole {
/*	List<PermissionProfile> permissionProfiles;*/
	String name;
	String password;
	
	protected User() {
		super();
	/*	permissionProfiles = new ArrayList<PermissionProfile>();*/
	}

	public User(User executor, String name, String password, Party source, Party target) {
		super(executor, source, target);
		/*permissionProfiles = new ArrayList<PermissionProfile>();*/
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	
}
