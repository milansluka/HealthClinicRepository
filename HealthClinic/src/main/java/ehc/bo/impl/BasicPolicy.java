package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import ehc.bo.Resource;
import ehc.bo.SchedulingPolicy;
import ehc.util.DateUtil;

public class BasicPolicy extends SchedulingPolicy {
	private SchedulingUtil schedulingUtil = new SchedulingUtil();

	@Override
	public List<AppointmentProposal> getAppointmentProposals(AppointmentRequest request,
			SchedulingParameter param) {
		List<AppointmentProposal> appointmentProposals = new ArrayList<AppointmentProposal>();
		if (request == null) {
			return appointmentProposals;
		}
		long appointmentDuration = 0;
		if (param != null) {
			if (param.isCustomAppointmentLength()) {
				appointmentDuration = param.getCustomAppointmentLengthInMinutes() * 60;
			}
		} else {
			appointmentDuration = request.calculateAppointmentLengthInMinutes() * 60;
		}

		Date from = (Date) request.getFrom().clone();
		Date to = DateUtil.addSeconds(from, (int) appointmentDuration);
		int proposedAppointments = 0;
		while (proposedAppointments < param.getMaxCountOfFoundAppointmentProposals() && to.before(request.getTo())) {
			Map<ResourceType, SortedSet<Resource>> resources = schedulingUtil.getAvailableResources(from, to, request.getTreatmentTypes());

			if (resources != null) {
				AppointmentProposal appointmentProposal = new AppointmentProposal(resources, request.getTreatmentTypes(), from, to);
				appointmentProposals.add(appointmentProposal);
				proposedAppointments++;
			}

			from = DateUtil.addSeconds(from, param.getTimeGridInMinutes() * 60);
			to = DateUtil.addSeconds(to, param.getTimeGridInMinutes() * 60);
			from = schedulingUtil.moveFromIfOutOfWorkTime(from, to, param.getWorkTime());
			to = DateUtil.addSeconds(from, (int) appointmentDuration);
		}

		return appointmentProposals;
	}
}
