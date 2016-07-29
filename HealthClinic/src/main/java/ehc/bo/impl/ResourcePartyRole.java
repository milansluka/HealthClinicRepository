package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "resource_party_role")
public abstract class ResourcePartyRole extends ResourceImpl {
	Party source;
	Party target;
	List<ExecutorProvision> treatmentTypeProvisions = new ArrayList<>();

	protected ResourcePartyRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResourcePartyRole(User executor, Party source, Party target) {
		super(executor);
		this.source = source;
		this.target = target;

		if (source != null && target != null) {
			source.addReservableSourceRole(this);
			target.addReservableTargetRole(this);
		}
	}

	@OneToMany(mappedBy = "executor", cascade = CascadeType.ALL)
	public List<ExecutorProvision> getTreatmentTypeProvisions() {
		return treatmentTypeProvisions;
	}

	public void setTreatmentTypeProvisions(List<ExecutorProvision> treatmentTypeProvisions) {
		this.treatmentTypeProvisions = treatmentTypeProvisions;
	}

	@ManyToOne
	@JoinColumn(name = "source")
	public Party getSource() {
		return source;
	}

	public void setSource(Party source) {
		this.source = source;
	}

	@ManyToOne
	@JoinColumn(name = "target")
	public Party getTarget() {
		return target;
	}

	public void setTarget(Party target) {
		this.target = target;
	}
	
	public void addProvision(User executor, TreatmentType treatmentType, double provisionAmount) {
		if (provisionAmount < treatmentType.getDefaultProvision()) {
			return;
		}
		ExecutorProvision executorProvision = new ExecutorProvision(executor, this, treatmentType, provisionAmount);
		treatmentTypeProvisions.add(executorProvision);
	}

	@Override
	public boolean isAvailable(Date from, Date to) {
		for (ResourcePartyRole partyRole : getSource().getReservableSourceRoles()) {
			for (Appointment appointment : partyRole.getResourceAppointments()) {
				if (appointment.getState().getValue() == AppointmentStateValue.NEW) {
					if (isCollision(from, to, appointment.getFrom(), appointment.getTo())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public abstract boolean isSuitable(ResourceType resourceType);
}
