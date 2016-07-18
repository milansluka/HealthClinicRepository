package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "resource_party_role")
public abstract class ResourcePartyRole extends ResourceImpl {
	Party source;
	Party target;
	
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
	
	@Override
	public abstract boolean isSuitable(ResourceType resourceType);	
}
