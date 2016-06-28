package ehc.bo.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Skill extends ModifiableObject {
	String name;
	List<SkillAssignment> skillAssignments;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(mappedBy = "skill")
	public List<SkillAssignment> getSkillAssignments() {
		return skillAssignments;
	}
	
	public void setSkillAssignments(List<SkillAssignment> skillAssignments) {
		this.skillAssignments = skillAssignments;
	}
	
	
	
}
