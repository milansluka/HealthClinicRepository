package milansluka.testApp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MyUser {
	long id;
	String login;
	String password;
	Person source;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
		
	public void setId(long id) {
		this.id = id;
	}
	
	

	public MyUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyUser(String login, String password) {
		super();
		this.login = login;
		this.password = password;
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
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "sourceRole")
	public Person getSource() {
		return source;
	}
	public void setSource(Person source) {
		this.source = source;
	}
	
	
}
