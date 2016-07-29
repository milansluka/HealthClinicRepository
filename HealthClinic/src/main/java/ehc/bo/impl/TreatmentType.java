package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "treatment_type")
public class TreatmentType extends BaseObject{
	
	String name;
	String info;
	String category;	
	double price;	
	double defaultProvision;
	int duration;
	List<ResourceType> resourceTypes = new ArrayList<>();
	List<Appointment> appointments = new ArrayList<>();
	List<Treatment> executedTreatments = new ArrayList<>();
	List<ExecutorProvision> executorProvisions = new ArrayList<>();
	
	protected TreatmentType() {
		super();
		appointments = new ArrayList<Appointment>();
	}
	
	public TreatmentType(User executor, String name, String category, double price, double defaultProvision, int duration) {
		super(executor);
		this.name = name;
		this.category = category;
		this.price = price;
		this.duration = duration;
		this.defaultProvision = defaultProvision;
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
	
    @OneToMany(mappedBy = "treatmentType")
	public List<Treatment> getExecutedTreatments() {
		return executedTreatments;
	}

	public void setExecutedTreatments(List<Treatment> executedTreatments) {
		this.executedTreatments = executedTreatments;
	}
	
	
    @OneToMany(mappedBy = "treatmentType")
	public List<ExecutorProvision> getExecutorProvisions() {
		return executorProvisions;
	}

	public void setExecutorProvisions(List<ExecutorProvision> executorProvisions) {
		this.executorProvisions = executorProvisions;
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

	@Column(name = "default_provision")
	public double getDefaultProvision() {
		return defaultProvision;
	}

	public void setDefaultProvision(double defaultProvision) {
		this.defaultProvision = defaultProvision;
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

	/*@OneToMany(mappedBy = "treatmentType")*/
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "treatmentTypes")
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
	
	public void addTreatment(Treatment treatment) {
		getExecutedTreatments().add(treatment);
	}
}
