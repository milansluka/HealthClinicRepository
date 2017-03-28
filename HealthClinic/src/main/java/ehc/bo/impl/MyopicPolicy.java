package ehc.bo.impl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import ehc.bo.Resource;
import ehc.bo.SchedulingPolicy;
import ehc.util.DateUtil;

public class MyopicPolicy implements SchedulingPolicy {
	private final double noShowProbability = 0.15;
	private SchedulingUtil schedulingUtil = new SchedulingUtil();
	private double profit = 0;	

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
		Date to = DateUtil.addSeconds(timeSlot, appointmentLengthInMinutes * 60);
		Set<Appointment> conflicts = schedulingUtil.getConflictingAppointments(timeSlot, to,
				request.getTreatmentTypes());
		List<AppointmentSchedulingInfo> conflictsList = new ArrayList<AppointmentSchedulingInfo>();
		for (Appointment appointment : conflicts) {
			conflictsList.add(new AppointmentSchedulingInfo(appointment.getFrom(), appointment.getTo(),
					appointment.getPlannedTreatmentTypes()));
		}
		conflictsList.add(new AppointmentSchedulingInfo(timeSlot, to, request.getTreatmentTypes()));

		int n = conflicts.size() + 1;
		double[] noShowProbabilities = new double[n];
		determineNoShowProbabilities(noShowProbabilities);

		int numberOfCombinations = (int) Math.pow(2, n);
		// double[][] waitTimes = new double[n][numberOfCombinations];
		double[] combinationsProbabilities = new double[numberOfCombinations];
		double[] averageWaitTimesForCombinations = new double[numberOfCombinations];
		calculateCombinationProbabilities(n, noShowProbabilities, numberOfCombinations, combinationsProbabilities);
		calculateWaitTimesForCombinations(conflictsList, n, numberOfCombinations, averageWaitTimesForCombinations);

		double averageWaitTime = 0;
		for (int i = 0; i < numberOfCombinations; i++) {
			averageWaitTime += combinationsProbabilities[i] * averageWaitTimesForCombinations[i];
		}
		double costOfPatientWaiting = 0;
		for (AppointmentSchedulingInfo conflict : conflictsList) {
			Date start = conflict.getTo();
			Date nextFrom = DateUtil.addSeconds(conflict.getFrom(), (int) averageWaitTime);
			Date nextTo = DateUtil.addSeconds(conflict.getTo(), (int) averageWaitTime);		
			costOfPatientWaiting += calculateDelayCostOfConflictingAppointments(start, nextFrom, nextTo,
					request.getTreatmentTypes(), (int) averageWaitTime);		
		}
		
		return costOfPatientWaiting + averageWaitTime;
	}

	private void calculateWaitTimesForCombinations(List<AppointmentSchedulingInfo> conflictsList, int n,
			int numberOfCombinations, double[] averageWaitTimesForCombinations) {
		List<AppointmentSchedulingInfo> choosenConflicts = new ArrayList<AppointmentSchedulingInfo>();
		for (int i = 0; i < numberOfCombinations; i++) {
			BitSet bits = intToBits(i);
			choosenConflicts.clear();
			// if there is requested appointment in combination (binary number
			// starting with 1)
			if (bits.get(n - 1)) {
				for (int j = 0; j < n; j++) {
					if (bits.get(j)) {
						choosenConflicts.add(conflictsList.get(j));
					}
				}
				Collections.sort(choosenConflicts);
				// there is only requested appointment info
				if (choosenConflicts.size() == 1) {
					averageWaitTimesForCombinations[i] = 0;
				} else {
					double waitingTimesSum = 0;
					for (int j = 1; j < choosenConflicts.size(); j++) {
						waitingTimesSum += schedulingUtil.getDifferenceInSeconds(choosenConflicts.get(j - 1).getTo(),
								choosenConflicts.get(j).getFrom()) + waitingTimesSum;

					}
					averageWaitTimesForCombinations[i] = waitingTimesSum / choosenConflicts.size();
				}
			}
		}
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

	private double calculateDelayCostOfConflictingAppointments(Date start, Date from, Date to,
			List<TreatmentType> treatmentTypes, int delayInSeconds) {
		Set<Appointment> conflicts = schedulingUtil.getConflictingAppointmentsStartingFrom(start, from, to,
				treatmentTypes);
		double cost = conflicts.size() * delayInSeconds;

		if (!conflicts.isEmpty()) {
			for (Appointment conflict : conflicts) {
				Date nextStart = conflict.getTo();
				Date nextFrom = DateUtil.addSeconds(conflict.getFrom(), delayInSeconds);
				Date nextTo = DateUtil.addSeconds(conflict.getTo(), delayInSeconds);
				cost += calculateDelayCostOfConflictingAppointments(nextStart, nextFrom, nextTo,
						conflict.getPlannedTreatmentTypes(), delayInSeconds);
			}
		}
		return cost;
	}
}
