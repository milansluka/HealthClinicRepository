package ehc.bo.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "company")
@PrimaryKeyJoinColumn(name = "id")
public class Company extends Party {
	private String registrationNumber;
	
	protected Company() {
		super();
	}

	public Company(User executor, String name) {
		super(executor, name);
	}

	@Column(name = "registration_number")
	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	
	

}
