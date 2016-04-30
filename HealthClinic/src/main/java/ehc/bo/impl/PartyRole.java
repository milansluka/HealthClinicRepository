package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "party_role")
public class PartyRole {
	long id;
	Party source;
	Party target;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Party getSource() {
		return source;
	}
	public void setSource(Party source) {
		this.source = source;
	}
	public Party getTarget() {
		return target;
	}
	public void setTarget(Party target) {
		this.target = target;
	}
}
