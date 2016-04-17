package milansluka.HealthClinic.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import milansluka.HealthClinic.bo.Party;

@Entity
@Table(name = "person")
public class Person {
	
	private long id;
	
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	
	private List<Appointment> appointments;
	
	public Person() {
		super();
		appointments = new ArrayList<Appointment>();
	}

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "person_id_seq", name = "person_id_seq")
	@GeneratedValue(generator = "person_id_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "person_id")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@OneToMany(mappedBy = "person")
	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	@Column(name = "first_name")
	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name = "last_name")
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void addAppointment(Appointment appointment) {
		getAppointments().add(appointment);
	}
}
