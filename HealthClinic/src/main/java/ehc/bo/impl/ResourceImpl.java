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
	public boolean isAvailable(Date from, Date to) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSuitable(ResourceType resourceType) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
