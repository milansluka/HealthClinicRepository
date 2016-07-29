package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "nurse_type")
public class NurseType extends ResourceTypeWithSkills {
String name;
	protected NurseType() {
		super();
	}

	public NurseType(User executor) {
		super(executor);
	}
	
	public NurseType(User executor, String name) {
		super(executor);
		this.name = name;
	}
}
