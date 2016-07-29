package ehc.bo.impl;

import java.util.Comparator;

public class PartySuitabilityComparator implements Comparator<Party> {

	@Override
	public int compare(Party o1, Party o2) {
		return o1.getReservableSourceRoles().size() - o2.getReservableSourceRoles().size();
	}
}
