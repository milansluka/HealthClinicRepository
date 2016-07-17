package ehc.bo.impl;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Nurse extends ResourcePartyRole {
	NurseType type;
	
	
	protected Nurse() {
		super();
	}
	
	public Nurse(User executor, NurseType type, Party source, Party target) {
		super(executor, source, target);
		this.type = type;
	}
		
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "nurse_type_id")
	public NurseType getType() {
		return type;
	}
		
	public void setType(NurseType type) {
		this.type = type;
	}
	
	public void addSkill(Skill skill) {
		getType().addSkill(skill);
	}

/*	@Override
	public boolean isAvailable(Date from, Date to) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSuitable(ResourceType resourceType) {
		// TODO Auto-generated method stub
		return false;
	}*/
}
