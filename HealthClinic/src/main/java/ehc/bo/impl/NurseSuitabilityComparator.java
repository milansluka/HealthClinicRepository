package ehc.bo.impl;

import java.util.Comparator;

public class NurseSuitabilityComparator implements Comparator<Nurse> {

	@Override
	public int compare(Nurse nurse1, Nurse nurse2) {
		return nurse1.getType().getSkills().size() - nurse2.getType().getSkills().size();
	}

}
