package ehc.bo.impl;

/*import javax.persistence.CascadeType;*/
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "physician_type")
@PrimaryKeyJoinColumn(name = "id")
public class PhysicianType extends ResourceTypeWithSkills {
/*	List<Skill> skills = new ArrayList<>();*/
	
	protected PhysicianType() {
		super();
	}
	
	public PhysicianType(User executor) {
		super(executor);
	}
	
/*	@ManyToMany(fetch = FetchType.LAZY)
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinTable(name = "skill_assignment", joinColumns = {@JoinColumn(name = "physician_type_id")},
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
		skill.addPhysicianType(this);
	}*/		
}
