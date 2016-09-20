package ehc.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public abstract class ResourcePartyRole extends ResourceImpl {
	Party source;
	Party target;
	List<ExecutorProvision> treatmentTypeProvisions = new ArrayList<>();
	List<ExecutorReceipt> executorAccounts = new ArrayList<>();

	protected ResourcePartyRole() {
		super();
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
	
    @OneToMany(mappedBy = "executor", cascade = CascadeType.ALL)
	public List<ExecutorReceipt> getExecutorAccounts() {
		return executorAccounts;
	}

	public void setExecutorAccounts(List<ExecutorReceipt> executorAccounts) {
		this.executorAccounts = executorAccounts;
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
	
	public void addExecutorAccount(ExecutorReceipt executorAccount) {
		getExecutorAccounts().add(executorAccount);
	}
	
	
	public void addProvision(User executor, TreatmentGroup treatmentGroup, double provisionAmount) {
		for (TreatmentType treatmentType : treatmentGroup.getTreatmentTypes()) {
			addProvision(executor, treatmentType, provisionAmount);
		}
	}
	
	public double getProvisionFromTreatmentType(TreatmentType treatmentType) {
		for (ExecutorProvision executorProvision : treatmentTypeProvisions) {
			if (executorProvision.getTreatmentType().equals(treatmentType)) {
				return executorProvision.getProvisionAmount();
			}
		}
		return treatmentType.getDefaultProvision();
	}

	@Override
	public boolean isNotBusy(Date from, Date to) {
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
