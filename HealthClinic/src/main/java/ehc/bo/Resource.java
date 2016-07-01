package ehc.bo;

import java.util.Date;

import ehc.bo.impl.ResourceType;

public interface Resource {
	boolean isAvailable(Date from, Date to);
	boolean isSuitable(ResourceType resourceType);

}
