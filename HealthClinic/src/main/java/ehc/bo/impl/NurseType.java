package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "nurse_type")
public class NurseType extends ResourceTypeWithSkills {

	protected NurseType() {
		super();
	}

	public NurseType(User executor) {
		super(executor);
	}
}
