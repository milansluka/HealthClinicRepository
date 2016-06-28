package ehc.bo.impl;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Device extends ModifiableObject {
	Date availableFrom;
	Date availableTo;
	
	

}
