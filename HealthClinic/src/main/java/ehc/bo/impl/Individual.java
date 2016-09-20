package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

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
