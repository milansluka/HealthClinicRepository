package ehc.bo.impl;

import java.util.Comparator;

public class DeviceSuitabilityComparator implements Comparator<Device> {

	@Override
	public int compare(Device device1, Device device2) {
	    return -1;
		/*return device1.getType().getPossibleTreatmentTypes().size() - device2.getType().getPossibleTreatmentTypes().size();*/
	}

}
