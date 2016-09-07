package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
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
	@Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE})
	@JoinTable(name = "skillassignment", joinColumns = {@JoinColumn(name = "resourcetypewithskills")},
	inverseJoinColumns = {@JoinColumn(name = "skill")})
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((skills == null) ? 0 : skills.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ResourceTypeWithSkills)) {
			return false;		
		}
		ResourceTypeWithSkills resourceType = (ResourceTypeWithSkills)obj;
		if (resourceType.getSkills().size() != getSkills().size()) {
			return false;
		}
		
		return getSkills().containsAll(resourceType.getSkills());
	}
	
	
}
