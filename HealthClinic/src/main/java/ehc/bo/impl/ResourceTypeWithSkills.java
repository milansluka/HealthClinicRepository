package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "resource_type_with_skills")
@PrimaryKeyJoinColumn(name = "id")
public class ResourceTypeWithSkills extends ResourceType {
	List<Skill> skills;
	
	public ResourceTypeWithSkills() {
		super();
	}
	
	public ResourceTypeWithSkills(User executor) {
		super(executor);
		skills = new ArrayList<>();
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinTable(name = "skill_assignment", joinColumns = {@JoinColumn(name = "resource_type_with_skills_id")},
	inverseJoinColumns = {@JoinColumn(name = "skill_id")})
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
		getSkills().add(skill);
		skill.addResourceType(this);
	}
	
	public void addSkills(List<Skill> skills) {	
		if (skills == null) {
			return;
		}
		for (Skill skill : skills) {
			addSkill(skill);
		}	
	}
	
	public void removeSkill(Skill skill) {
		getSkills().remove(skill);
	}
	
	public boolean containsSkills(List<Skill> skills) {		
		return getSkills().containsAll(skills);
	}
}
