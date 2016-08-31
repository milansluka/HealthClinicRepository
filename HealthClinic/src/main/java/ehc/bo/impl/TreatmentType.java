package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "treatment_type")
public class TreatmentType extends BaseObject {

	String name;
	String info;
/*	String category;*/
	/* double price; */
	Money price;
	double defaultProvision;
	
	//duration in seconds
	int duration;
	TreatmentGroup treatmentGroup;
	List<ResourceType> resourceTypes = new ArrayList<>();
	List<Appointment> appointments = new ArrayList<>();
	List<Treatment> executedTreatments = new ArrayList<>();
	List<ExecutorProvision> executorProvisions = new ArrayList<>();
	List<RoomType> possibleRoomTypes = new ArrayList<>();

	protected TreatmentType() {
		super();
		appointments = new ArrayList<Appointment>();
	}

	public TreatmentType(User executor, String name, Money price, double defaultProvision,
			int duration, TreatmentGroup treatmentGroup) {
		super(executor);
		this.name = name;
		this.price = price;
		this.duration = duration;
		this.defaultProvision = defaultProvision;
		this.treatmentGroup = treatmentGroup;
		treatmentGroup.addTreatmentType(this);
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "price")
	public Money getPrice() {
		return price;
	}

	public void setPrice(Money price) {
		this.price = price;
	}

	@ManyToOne
	@JoinColumn(name = "treatment_group_id")
	public TreatmentGroup getTreatmentGroup() {
		return treatmentGroup;
	}

	public void setTreatmentGroup(TreatmentGroup treatmentGroup) {
		this.treatmentGroup = treatmentGroup;
	}

	/*
	 * @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 * 
	 * @JoinTable(name = "resource_type_assignment", joinColumns =
	 * {@JoinColumn(name = "treatment_type_id")}, inverseJoinColumns =
	 * {@JoinColumn(name = "resource_type_id")})
	 */
	@OneToMany(mappedBy = "treatmentType", cascade = CascadeType.ALL)
	public List<ResourceType> getResourceTypes() {
		return resourceTypes;
	}

	public void setResourceTypes(List<ResourceType> resourceTypes) {
		this.resourceTypes = resourceTypes;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "possibleTreatmentTypes")
	public List<RoomType> getPossibleRoomTypes() {
		return possibleRoomTypes;
	}

	public void setPossibleRoomTypes(List<RoomType> possibleRoomTypes) {
		this.possibleRoomTypes = possibleRoomTypes;
	}

	@OneToMany(mappedBy = "treatmentType")
	public List<Treatment> getExecutedTreatments() {
		return executedTreatments;
	}

	public void setExecutedTreatments(List<Treatment> executedTreatments) {
		this.executedTreatments = executedTreatments;
	}

	@OneToMany(mappedBy = "treatmentType", cascade = CascadeType.ALL)
	public List<ExecutorProvision> getExecutorProvisions() {
		return executorProvisions;
	}

	public void setExecutorProvisions(List<ExecutorProvision> executorProvisions) {
		this.executorProvisions = executorProvisions;
	}

	/*
	 * public double getPrice() { return price; }
	 * 
	 * public void setPrice(double price) { this.price = price; }
	 */

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
		resourceType.setTreatmentType(this);
	}

	public void addTreatment(Treatment treatment) {
		getExecutedTreatments().add(treatment);
	}

	/*
	 * public void removeResourceType(ResourceType resourceType) {
	 * resourceType.removeTreatmentType(this); }
	 */

	public void removeResourceType(ResourceType resourceType) {
		getResourceTypes().remove(resourceType);
	}

	// private void removeResourceTypes() {
	// for (ResourceType resourceType : getResourceTypes()) {
	// resourceType.removeFromTreatmentTypes();
	// }
	// }

	/*
	 * public void prepareForDeleting() { removeResourceTypes(); }
	 */
}
