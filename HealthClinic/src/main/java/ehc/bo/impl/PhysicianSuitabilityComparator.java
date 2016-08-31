package ehc.bo.impl;

import java.util.Comparator;

public class PhysicianSuitabilityComparator implements Comparator<Physician> {
	@Override
	public int compare(Physician physician1, Physician physician2) {
		int ret = physician1.getType().getSkills().size() - physician2.getType().getSkills().size();
		
		if (ret == 0) {
			Party party1 = physician1.getSource();
			Party party2 = physician2.getSource();
			Comparator<Party> comparator = new PartySuitabilityComparator();
			ret = comparator.compare(party1, party2);
			if (ret == 0) {
				if (party1 instanceof Individual && party2 instanceof Individual) {
					Individual individual1 = (Individual)party1;
					Individual individual2 = (Individual)party2;
					return individual1.compareTo(individual2);
				} 
				return party1.compareTo(party2);
			}
		}
		return ret;
	}
}
