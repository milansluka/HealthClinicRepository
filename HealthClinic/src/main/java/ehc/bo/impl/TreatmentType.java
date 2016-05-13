package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.Session;

@Entity
@Table(name = "treatment_type")
public class TreatmentType extends BaseObject{
	
	String name;
	String info;
	String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	List<Appointment> appointments;
	    
	protected TreatmentType() {
		super();
		appointments = new ArrayList<Appointment>();
	}
	
	public TreatmentType(User executor, String name, String type) {
		super(executor);
		appointments = new ArrayList<Appointment>();
		this.name = name;
		this.type = type;
	}
	
	@OneToMany(mappedBy = "treatmentType")
	public List<Appointment> getAppointments() {
		return appointments;
	}
	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public void addAppointment(Appointment appointment) {
		getAppointments().add(appointment);
	}
	
	public static TreatmentType getTreatmentType(long id, Session session) {
		String hql = "FROM TreatmentType t WHERE t.id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);

		List results = query.list();
		
		return (TreatmentType) results.get(0);		
	}
	
	public static TreatmentType getTreatmentType(String type, Session session) {
		String hql = "FROM TreatmentType t WHERE t.type = :type";
		Query query = session.createQuery(hql);
		query.setParameter("type", type);

		List results = query.list();
		
		return (TreatmentType) results.get(0);		
	}
}
