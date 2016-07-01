package ehc.bo.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import ehc.bo.Resource;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class Physician extends PartyRole implements Resource {
	PhysicianType type;
	List<Appointment> appointments;
	
	protected Physician() {
		super();
	}
	
	public Physician(User executor, PhysicianType type, Party source, Party target) {
		super(executor, source, target);
		this.type = type;
	}

/*	@ManyToOne
	@JoinColumn(name = "physician_type_id")*/
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "physician_type_id")
	public PhysicianType getType() {
		return type;
	}

	public void setType(PhysicianType type) {
		this.type = type;
	}
	
	@OneToMany(mappedBy = "physician")
	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public void addAppointment(Appointment appointment) {
		getAppointments().add(appointment);
	}
	
	public boolean isCompetent(PhysicianType type) {
		return true;
	}
	
	public boolean isCompetent(TreatmentType treatmentType) {
		return true;
	}
	
	private boolean isCollision(Date from1, Date to1, Date from2, Date to2) {
		
		return from1.after(from2) && from1.before(to2) || to1.after(from2) && to1.before(to2);
	}

	@Override
	public boolean isAvailable(Date from, Date to) {
		for (Appointment appointment : appointments) {
			if (isCollision(from, to, appointment.getFrom(), appointment.getTo())) {
				return false;
			}				
		}
		
		return true;
	}

	@Override
	public boolean isSuitable(ResourceType resourceType) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
/*	List<Skill> skills;

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
	
	public void addSkill(Skill skill) {
		getSkills().add(skill);
	}*/
}
