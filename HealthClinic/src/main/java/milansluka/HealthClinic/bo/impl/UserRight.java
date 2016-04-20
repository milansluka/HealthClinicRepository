package milansluka.HealthClinic.bo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "right")
public class UserRight {

	long id;
	
	List<User> users;
	UserRightType type;
	
	public UserRight() {
		super();
		users = new ArrayList<User>();
	}

	@Id
	@Column(name = "right_id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	
    @ManyToMany(mappedBy = "rights")  
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Enumerated(EnumType.STRING)
	public UserRightType getType() {
		return type;
	}

	public void setType(UserRightType type) {
		this.type = type;
	}
	
	

}
