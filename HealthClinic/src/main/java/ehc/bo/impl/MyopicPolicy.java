package ehc.bo.impl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import ehc.bo.Resource;
import ehc.bo.SchedulingPolicy;
import ehc.util.DateUtil;

public class MyopicPolicy extends SchedulingPolicy {
	private final double noShowProbability = 0.15;
	private AppointmentDao appointmentDao = AppointmentDao.getInstance();
	private SchedulingUtil schedulingUtil = new SchedulingUtil();

	private double profit = 0;

	// revenue - cost

	@Override
	public List<AppointmentProposal> getAppointmentProposals(AppointmentRequest request, SchedulingParameter param) {
		Date currentTimeSlot = request.getFrom();
		Date bestTimeSlot = request.getFrom();
		int appointmentLength = 0;
		if (param.isCustomAppointmentLength()) {
			appointmentLength = param.getCustomAppointmentLengthInMinutes();
		} else {
			appointmentLength = (int) request.calculateAppointmentLengthInMinutes();
		}
		while (currentTimeSlot.before(request.getTo())) {
			double currentSlotProfit = calculateProfit(currentTimeSlot, request, appointmentLength);
			if (currentSlotProfit > profit) {
				profit = currentSlotProfit;
				bestTimeSlot = currentTimeSlot;
			}
			currentTimeSlot = DateUtil.addSeconds(currentTimeSlot, HealthPoint.DEFAULT_TIME_GRID_IN_MINUTES * 60);
		}

		Date appointmentFrom = bestTimeSlot;
		Date appointmentTo = DateUtil.addSeconds(appointmentFrom, appointmentLength * 60);
		List<AppointmentProposal> appointmentProposals = new ArrayList<AppointmentProposal>();
		Map<ResourceType, SortedSet<Resource>> resources = schedulingUtil.getAvailableResources(appointmentFrom,
				appointmentTo, request.getTreatmentTypes());
		if (resources == null) {
			resources = schedulingUtil.getSuitableResources(appointmentFrom, appointmentTo,
					request.getTreatmentTypes());
		}
		AppointmentProposal appointmentProposal = new AppointmentProposal(resources, request.getTreatmentTypes(),
				appointmentFrom, appointmentTo);
		appointmentProposals.add(appointmentProposal);

		return appointmentProposals;
	}

	private double calculateProfit(Date timeSlot, AppointmentRequest request, int appointmentLengthInMinutes) {
		double expectedRevenue = 0;
		for (TreatmentType treatmentType : request.getTreatmentTypes()) {
			expectedRevenue += treatmentType.getPrice().getAmount().doubleValue();
		}

		expectedRevenue = (1 - noShowProbability) * expectedRevenue;
		double expectedCost = calculateWaitingCost(timeSlot, request, appointmentLengthInMinutes);

		return expectedRevenue - expectedCost;
	}

	private BitSet intToBits(int value) {
		int temp = value;
		BitSet result = new BitSet();
		int index = 0;
		while (temp != 0) {
			boolean boolValue = temp % 2 == 1 ? true : false;
			result.set(index, boolValue);
			temp = temp / 2;
			index++;
		}
		return result;
	}

	private double calculateWaitingCost(Date timeSlot, AppointmentRequest request, int appointmentLengthInMinutes) {
		double costOfPatientWaiting = 0;
		Date to = DateUtil.addSeconds(timeSlot, appointmentLengthInMinutes * 60);
		Set<Appointment> conflicts = schedulingUtil.getConflictingAppointments(timeSlot, to,
				request.getTreatmentTypes());
		int n = conflicts.size() + 1;
		double[] noShowProbabilities = new double[n];

		determineNoShowProbabilities(noShowProbabilities);

		int numberOfCombinations = (int) Math.pow(2, n);
		double[][] waitTimes = new double[n][numberOfCombinations];
		double[] waitTimesProbabilities = new double[n];
		double[] combinationsProbabilities = new double[numberOfCombinations];

		calculateCombinationProbabilities(n, noShowProbabilities, numberOfCombinations, combinationsProbabilities);
	
		return 0;
	}

	private void calculateCombinationProbabilities(int n, double[] noShowProbabilities, int numberOfCombinations,
			double[] combinationsProbabilities) {
		for (int i = 0; i < numberOfCombinations; i++) {
			BitSet bits = intToBits(i);
			double p = 1;
			for (int j = 0; j < n; j++) {
				if (bits.get(j)) {
					p *= 1 - noShowProbabilities[j];
				} else {
					p *= noShowProbabilities[j];
				}
			}
			combinationsProbabilities[i] = p;
		}
	}

	private void determineNoShowProbabilities(double[] noShowProbabilities) {
		for (int i = 0; i < noShowProbabilities.length; i++) {
			noShowProbabilities[i] = noShowProbability;
		}
	}

	private double calculateCostOfPatientWaiting(Date timeSlot, AppointmentRequest request) {
		return 0;

	}
}
