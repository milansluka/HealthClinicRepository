package ehc.bo.impl;

import ehc.bo.PartyDao;
import ehc.bo.PartyRoleDao;

public class PartyRoleDaoImpl extends Dao implements PartyRoleDao {

	public void addPartyRole(PartyRole partyRole) {
		openCurrentSession();
		
		currentSession.save(partyRole);
		
		closeCurrentSession();
		
	}

	public void deletePartyRole(PartyRole partyRole) {
		// TODO Auto-generated method stub
		
	}
	

}
