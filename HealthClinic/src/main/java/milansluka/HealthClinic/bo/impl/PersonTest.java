/*package milansluka.HealthClinic.bo.impl;

import javax.persistence.Entity;

import milansluka.HealthClinic.bo.Individual;

@Entity
public class Person implements Individual {
	private long id;
	
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	
	
	

	public void assignPartyRole() {
		// TODO Auto-generated method stub	
	}
}*/

package milansluka.HealthClinic.bo.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PersonTest {

	long id;
	String firstName;
	String lastName;
	String email;
	
	

	public PersonTest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PersonTest(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

