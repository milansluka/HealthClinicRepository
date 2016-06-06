package ehc.bo.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class PermissionProfile {
	int id;
	List<Permission> permissions;
/*	List<User> users;*/
	String name;
	
	@Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
/*	@ManyToMany(mappedBy = "permissionProfiles") 
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}*/
	
	@OneToMany(mappedBy = "permissionProfile")
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}  
}
