package ehc.bo.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "executor_treatmenttype")
@Inheritance(strategy = InheritanceType.JOINED)
public class ExecutorProvision extends ModifiableObject {
	private ResourcePartyRole executor;
	private TreatmentType treatmentType;
	private double provisionAmount;
	
	protected ExecutorProvision() {
		super();
	}
	
	public ExecutorProvision(User executor, ResourcePartyRole treatmentTypeExecutor, TreatmentType treatmentType, double provisionAmount) {
		super(executor);
	    this.executor = treatmentTypeExecutor;
	    this.treatmentType = treatmentType;
	    this.provisionAmount = provisionAmount;
	}
	
	@Column(name = "provision")
	public double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	@ManyToOne
	@JoinColumn(name = "executor")
	public ResourcePartyRole getExecutor() {
		return executor;
	}
	public void setExecutor(ResourcePartyRole executor) {
		this.executor = executor;
	}
	
	@ManyToOne
	@JoinColumn(name = "treatmenttype")
	public TreatmentType getTreatmentType() {
		return treatmentType;
	}
	
	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}
}
