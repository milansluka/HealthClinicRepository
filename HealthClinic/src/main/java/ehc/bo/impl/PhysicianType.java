package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "physician_type")
@PrimaryKeyJoinColumn(name = "id")
public class PhysicianType extends ResourceType {
/*	List<Skill> skills;*/
	
	protected PhysicianType() {
		super();
	}
	
	public PhysicianType(User executor) {
		super(executor);
	}
	

/*	@ManyToMany
	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}


	public void addSkill(Skill skill) {
		if (skill == null) {
			return;
		}
	}	*/	
}
