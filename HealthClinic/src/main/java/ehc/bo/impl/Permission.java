package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission {

	long id;
	
/*	List<User> users;*/
	UserPermissionType type;
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

	@Enumerated(EnumType.STRING)
	public UserPermissionType getType() {
		return type;
	}

	public void setType(UserPermissionType type) {
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
