package ehc.bo.impl;

import java.util.Comparator;

public class RoomSuitabilityComparator implements Comparator<Room> {

	@Override
	public int compare(Room room1, Room room2) {
	    
		return room1.getType().getPossibleTreatmentTypes().size() - room2.getType().getPossibleTreatmentTypes().size();
	}
	

}
