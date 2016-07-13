package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.Session;

@Entity
@Table(name = "treatment_type")
public class TreatmentType extends BaseObject{
	
	String name;
	String info;
	String category;	
	double price;	
	int duration;
	List<ResourceType> resourceTypes;
/*	List<Device> requiredDevices;*/
	List<Appointment> appointments;
	
	protected TreatmentType() {
		super();
		appointments = new ArrayList<Appointment>();
	}
	
	public TreatmentType(User executor, String name, String category, double price, int duration) {
		super(executor);
		appointments = new ArrayList<Appointment>();
		resourceTypes = new ArrayList<>();
		this.name = name;
		this.category = category;
		this.price = price;
		this.duration = duration;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "resource_type_assignment", joinColumns = {@JoinColumn(name = "treatment_type_id")},
	inverseJoinColumns = {@JoinColumn(name = "resource_type_id")})
	public List<ResourceType> getResourceTypes() {
		return resourceTypes;
	}

	public void setResourceTypes(List<ResourceType> resourceTypes) {
		this.resourceTypes = resourceTypes;
	}

	public String getCategory() {
		return category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setCategory(String category) {
		this.category = category;
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
	
	public void addResourceType(ResourceType resourceType) {
		if (resourceType == null) {
			return;
		}
		getResourceTypes().add(resourceType);
		resourceType.addTreatmentType(this);
	}
	
/*	public static TreatmentType getTreatmentType(long id, Session session) {
		String hql = "FROM TreatmentType t WHERE t.id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);

		List results = query.list();
		
		return (TreatmentType) results.get(0);		
	}*/
	
/*	public static TreatmentType getTreatmentType(String type, Session session) {
		String hql = "FROM TreatmentType t WHERE t.type = :type";
		Query query = session.createQuery(hql);
		query.setParameter("type", type);

		List results = query.list();
		
		return (TreatmentType) results.get(0);		
	}*/
}
