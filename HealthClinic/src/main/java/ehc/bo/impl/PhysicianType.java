package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class PhysicianType extends ResourceTypeWithSkills {	
	protected PhysicianType() {
		super();
	}
	
	public PhysicianType(User executor) {
		super(executor);
	}	
}
