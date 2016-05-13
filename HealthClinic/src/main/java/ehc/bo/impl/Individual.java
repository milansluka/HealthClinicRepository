package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.Session;

@Entity
@Table(name = "individual")
@PrimaryKeyJoinColumn(name = "party_id")
public class Individual extends Party {

	private String firstName;
	private String email;
	private String phone;

	private List<Appointment> appointments;

	protected Individual() {
		super();
		appointments = new ArrayList<Appointment>();
	}

	public Individual(User executor, String firstName, String lastName, String phone) {
		super(executor, lastName);
		this.firstName = firstName;
		this.phone = phone;
	}

	@OneToMany(mappedBy = "individual")
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

	public static List<Individual> getIndividuals(String firstName, String name, String phone, Session session) {
		String hql = "FROM Individual i WHERE i.firstName = :firstName and i.name = :name " + "and i.phone = :phone";
		Query query = session.createQuery(hql);
		query.setParameter("firstName", firstName);
		query.setParameter("name", name);
		query.setParameter("phone", phone);

		return query.list();
	}
	
	public static Individual getPerson(long id, Session session) {
		String hql = "FROM Individual i WHERE i.ide = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		List results = query.list();
		return (Individual)results.get(0);
	}
}
