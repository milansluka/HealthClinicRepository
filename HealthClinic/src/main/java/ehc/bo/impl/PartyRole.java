package ehc.bo.impl;

import javax.persistence.Entity;
import javax.persistence.Table;

import ehc.bo.Party;

@Entity
@Table(name = "party_role")
public class PartyRole {
	long id;
	Party source;
	Party target;

}
