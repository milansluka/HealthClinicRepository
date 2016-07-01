package ehc.bo.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "nurse_type")
public class NurseType extends ResourceType {
	List<Skill> skills;

	protected NurseType() {
		super();
	}

	public NurseType(User executor) {
		super(executor);
	}

}
