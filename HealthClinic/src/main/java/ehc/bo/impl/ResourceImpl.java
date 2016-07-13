package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import ehc.bo.Resource;

@Entity
@Table(name = "resource")
@Inheritance(strategy=InheritanceType.JOINED) 
public class ResourceImpl extends ModifiableObject implements Resource {
	List<Appointment> resourceAppointments = new ArrayList<Appointment>();
		
	protected ResourceImpl() {
		super();
	}
	
	public ResourceImpl(User executor) {
		super(executor);
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "resources")
	public List<Appointment> getResourceAppointments() {
		return resourceAppointments;
	}

	public void setResourceAppointments(List<Appointment> appointments) {
		this.resourceAppointments = appointments;
	}
	
	public void addAppointment(Appointment appointment) {
/*		if (appointment == null) {
			return;
		}*/
		getResourceAppointments().add(appointment);
		/*appointment.addResource(this);*/
	}
	
	@Override
	public void removeAppointment(Appointment appointment) {
		getResourceAppointments().remove(appointment);
	}

	@Override
	public boolean isAvailable(Date from, Date to) {
		for (Appointment appointment : getResourceAppointments()) {
			if (isCollision(from, to, appointment.getFrom(), appointment.getTo())) {
				return false;
			}
		}	
		return true;
	}

	@Override
	public boolean isSuitable(ResourceType resourceType) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean isCollision(Date from1, Date to1, Date from2, Date to2) {

		return from1.after(from2) && from1.before(to2) || to1.after(from2) && to1.before(to2);
	}
}
