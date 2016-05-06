package ehc.bo.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import ehc.bo.PartyDao;

public class PartyDaoImpl extends Dao implements PartyDao {

	public void AddParty(Party party) {
		party.setCreatedOn(new Date());
		openCurrentSessionWithTransaction();
		
		currentSession.save(party);

		closeCurrentSessionWithTransaction();

	}

	public void deleteParty(Party party) {
		// TODO Auto-generated method stub

	}

	public Party getParty(long id) {
		openCurrentSessionWithTransaction();

		String hql = "FROM Party p WHERE p.id = :id";
		Query query = currentSession.createQuery(hql);
		query.setParameter("id", id);

		List results = query.list();

		closeCurrentSessionWithTransaction();
		
		return (Party)results.get(0);

	}

}
