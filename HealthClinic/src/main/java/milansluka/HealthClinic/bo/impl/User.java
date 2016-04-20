package milansluka.HealthClinic.bo.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	
	long id;
	
	List<UserRight> rights;

	String login;
	String password;
	
	public User() {
		super();
		rights = new ArrayList<UserRight>();
	}

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "user_id_seq", name = "user_id_seq")
	@GeneratedValue(generator = "user_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "user_id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE})
/*	@ManyToMany*/
	@JoinTable(name = "assigned_right", joinColumns = @JoinColumn(name = "user_id") , inverseJoinColumns = @JoinColumn(name = "right_id") )
	public List<UserRight> getRights() {
		return rights;
	}

	public void setRights(List<UserRight> userRights) {
		this.rights = userRights;
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
	
	public boolean hasRight(UserRight right) {
		return rights.contains(right);
		
	}
	
	public void assignRight(UserRight right) {
		if (right != null) {
			right.getUsers().add(this);
			getRights().add(right);
			
		}
	}
	
}
