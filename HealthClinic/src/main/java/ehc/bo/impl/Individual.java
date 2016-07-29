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
@PrimaryKeyJoinColumn(name = "id")
public class Individual extends Party {
	private String firstName;
	private List<Appointment> appointments = new ArrayList<>();

	protected Individual() {
		super();
		appointments = new ArrayList<Appointment>();
	}

	public Individual(User executor, String firstName, String lastName) {
		super(executor, lastName);
		this.firstName = firstName;
	
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


	public void addAppointment(Appointment appointment) {
		getAppointments().add(appointment);
	}
	
	public void removeAppointment(Appointment appointment) {
		getAppointments().remove(appointment);
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
	
	@Override
	public int compareTo(Party o) {
		int ret = super.compareTo(o);
		if (ret == 0) {
			Individual individual = (Individual)o;
			return getFirstName().compareTo(individual.getFirstName());		
		}
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Individual other = (Individual) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		return true;
	}
}
