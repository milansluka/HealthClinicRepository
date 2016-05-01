package ehc.bo.impl;

import ehc.bo.PartyDao;
import ehc.bo.PartyRoleDao;

public class PartyManager {
	PartyDao partyDao;
	PartyRoleDao partyRoleDao;
	
	public PartyManager() {
		super();
		partyDao = new PartyDaoImpl();
		partyRoleDao = new PartyRoleDaoImpl();
	}

	public void addPartyRole(long source, long target, PartyRole partyRole) {
		if (partyRole == null) {
			return;
		}
		
		Party sourceParty = partyDao.getParty(source);
		Party targetParty = partyDao.getParty(target);
		
		partyRole.setSource(sourceParty);
		partyRole.setTarget(targetParty);
		
		partyRoleDao.addPartyRole(partyRole);
	}
	
	public void addUserRole(long partyId, User user) {
		PartyDao partyDao = new PartyDaoImpl();
		Party party = partyDao.getParty(partyId);
		
		if (party == null) {
			return;
		}
		
		user.setSource(party);
		user.setTarget(party);	
	}

}
