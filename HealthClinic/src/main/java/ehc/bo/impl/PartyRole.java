package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "party_role")
@Inheritance(strategy=InheritanceType.JOINED)  
public class PartyRole extends ModifiableObject {
	Party source;
	Party target;
	
	protected PartyRole() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PartyRole(User executor) {
		super(executor);
		// TODO Auto-generated constructor stub
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
}
