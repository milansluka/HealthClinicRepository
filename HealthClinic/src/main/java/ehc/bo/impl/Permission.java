package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission {

	long id;
	
/*	List<User> users;*/
	UserRightType type;
	PermissionProfile permissionProfile;
	
	public Permission() {
		super();
	}

	@Id
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	
/*    @ManyToMany(mappedBy = "rights")  
	public List<User> getUsers() {
		return users;
	}
*/
/*	public void setUsers(List<User> users) {
		this.users = users;
	}*/

	@Enumerated(EnumType.STRING)
	public UserRightType getType() {
		return type;
	}

	public void setType(UserRightType type) {
		this.type = type;
	}

	@ManyToOne
	@JoinColumn(name = "permission_profile_id")
	public PermissionProfile getPermissionProfile() {
		return permissionProfile;
	}

	public void setPermissionProfile(PermissionProfile permissionProfile) {
		this.permissionProfile = permissionProfile;
	}
	
	
	
	

}
