/*package milansluka.HealthClinic.bo.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import milansluka.HealthClinic.bo.impl.UserRight;

@Entity
@Table(name="user")
public class UserOld {
	
	long id;
	List<UserRight> userRights;
	Set<AssignedRight> assignedRights = new HashSet<AssignedRight>();
	String login;
	String password;
		
	@Id
	@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="user_id_seq", name="user_id_seq")
	@GeneratedValue(generator="user_id_seq", strategy=GenerationType.SEQUENCE)
	@Column(name = "user_id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUserRights(List<UserRight> userRights) {
		this.userRights = userRights;
	}

	@Column(name = "login")
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    @OneToMany
	public Set<AssignedRight> getAssignedRights() {
		return assignedRights;
	}

	public void setAssignedRights(Set<AssignedRight> assignedRights) {
		this.assignedRights = assignedRights;
	}

	public void assignUserRight(UserRight userRight) {
		AssignedRight assignedRight = new AssignedRight();
		assignedRight.setRight(userRight);
		assignedRight.setUser(this);
		assignedRights.add(assignedRight);
	}

}
*/