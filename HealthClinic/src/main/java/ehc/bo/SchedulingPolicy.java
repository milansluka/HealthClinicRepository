package ehc.bo;

import java.util.List;

import ehc.bo.impl.AppointmentProposal;
import ehc.bo.impl.AppointmentRequest;
import ehc.bo.impl.DeviceDao;
import ehc.bo.impl.NurseDao;
import ehc.bo.impl.PhysicianDao;
import ehc.bo.impl.SchedulingParameter;

public interface SchedulingPolicy {
	public abstract List<AppointmentProposal> getAppointmentProposals(AppointmentRequest request, SchedulingParameter param);	
}
