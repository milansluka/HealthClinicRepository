package ehc.bo.impl;

import java.util.Comparator;

public class RoomSuitabilityComparator implements Comparator<Room> {

	@Override
	public int compare(Room room1, Room room2) {
		int comparisonResult = room1.getType().getPossibleTreatmentTypes().size() - room2.getType().getPossibleTreatmentTypes().size();
	    
		if (comparisonResult == 0) {
			return room1.getName().compareTo(room2.getName());
		}
		return comparisonResult;
	}
	

}
