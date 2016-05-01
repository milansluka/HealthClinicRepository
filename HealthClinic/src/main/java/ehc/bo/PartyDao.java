package ehc.bo;

import ehc.bo.impl.Individual;
import ehc.bo.impl.Party;

public interface PartyDao {
	void AddParty(Party party);
	void deleteParty(Party party);
	Party getParty(long id);
	
}
