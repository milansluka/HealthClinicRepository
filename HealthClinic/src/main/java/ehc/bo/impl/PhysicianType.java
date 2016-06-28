package ehc.bo.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "physician_type")
@Inheritance(strategy=InheritanceType.JOINED)  
public class PhysicianType extends ModifiableObject {
	List<SkillAssignment> skillAssignments;
	
	protected PhysicianType() {
		super();
	}
	
	public PhysicianType(User executor) {
		super(executor);
	}

	@OneToMany(mappedBy = "physicianType")
	public List<SkillAssignment> getSkillAssignments() {
		return skillAssignments;
	}

	public void setSkillAssignments(List<SkillAssignment> skillAssignments) {
		this.skillAssignments = skillAssignments;
	}
	
	public void addSkillAssignment(SkillAssignment skillAssignment) {
		if (skillAssignment == null) {
			return;
		}
		
		getSkillAssignments().add(skillAssignment);
		skillAssignment.setPhysicianType(this);
	}
	
		
}
