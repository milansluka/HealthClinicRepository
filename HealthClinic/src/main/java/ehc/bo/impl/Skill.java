package ehc.bo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

@Entity
@Inheritance(strategy=InheritanceType.JOINED) 
public class Skill extends ModifiableObject {
	String name;
/*	List<PhysicianType> physicianTypes = new ArrayList<PhysicianType>();*/
	List<ResourceTypeWithSkills> resourceTypes = new ArrayList<>();
	
	protected Skill() {
		super();
	}
	
	public Skill(User executor, String name) {
		super(executor);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "skills")
	public List<ResourceTypeWithSkills> getResourceTypes() {
		return resourceTypes;
	}

	public void setResourceTypes(List<ResourceTypeWithSkills> resourceTypes) {
		this.resourceTypes = resourceTypes;
	}

	public void addResourceType(ResourceTypeWithSkills resourceType) {
		getResourceTypes().add(resourceType);
	}

/*	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "skills")
	public List<PhysicianType> getPhysicianTypes() {
		return physicianTypes;
	}

	public void setPhysicianTypes(List<PhysicianType> physicianTypes) {
		this.physicianTypes = physicianTypes;
	}

	public void addPhysicianType(PhysicianType physicianType) {
		getPhysicianTypes().add(physicianType);
	}*/
}
