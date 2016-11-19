package ehc.bo;

import java.util.Date;
import java.util.List;

public interface Schedule {
	void setTimeGridInMinutes(int timeGrid);
	int getTimeGridInMinutes();
    Date getStartWorkTime(Date day);
    Date getEndWorkTime(Date day);
    List<String> getRoomNames();
    
}
