package ehc.bo;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import ehc.bo.impl.Appointment;
import ehc.bo.impl.ResourceType;
import ehc.bo.impl.Treatment;

@MappedSuperclass
public interface Resource {
	boolean isAvailable(Date from, Date to);
	boolean isNotBusy(Date from, Date to);
	boolean isSuitable(ResourceType resourceType);
	void addAppointment(Appointment appointment);
	void removeAppointment(Appointment appointment);
	void addTreatment(Treatment treatment);
}
