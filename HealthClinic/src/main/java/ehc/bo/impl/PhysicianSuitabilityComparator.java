package ehc.bo.impl;

import java.util.Comparator;

public class PhysicianSuitabilityComparator implements Comparator<Physician> {
	@Override
	public int compare(Physician physician1, Physician physician2) {
		return physician1.getType().getSkills().size() - physician2.getType().getSkills().size();
	}
}
