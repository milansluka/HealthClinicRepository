package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "skill_assignment")
public class SkillAssignment extends BaseObject {
	PhysicianType physicianType;
	Skill skill;
	
	protected SkillAssignment() {
		super();
	}
	
	public SkillAssignment(User executor, PhysicianType physicianType, Skill skill) {
		super(executor);
		this.physicianType = physicianType;
		this.skill = skill;
	}
	
	@ManyToOne
	@JoinColumn(name = "physician_type_id")
	public PhysicianType getPhysicianType() {
		return physicianType;
	}
	public void setPhysicianType(PhysicianType physicianType) {
		this.physicianType = physicianType;
	}
	
	@ManyToOne
	@JoinColumn(name = "skill_id")
	public Skill getSkill() {
		return skill;
	}
	public void setSkill(Skill skill) {
		this.skill = skill;
	}
	
}
