package ehc.bo.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;

import ehc.bo.TreatmentTypeDao;

public class TreatmentTypeDaoImpl extends Dao implements TreatmentTypeDao {

	public TreatmentType find(TreatmentType treatmentType) {
		openCurrentSession();

		String name = treatmentType.getName();

		String hql = "FROM TreatmentType t WHERE t.name = :name";
		Query query = currentSession.createQuery(hql);
		query.setParameter("name", name);

		List results = query.list();
		TreatmentType tt = null;
		
		if (results.size() > 1) {
			tt = (TreatmentType) results.get(0);
			System.out.println("More treatment types with the same name");
		} else if (results.size() == 1) {
			tt = (TreatmentType) results.get(0);
		}
		
		if (tt != null) {
			Hibernate.initialize(tt.getAppointments());
		}
		
		closeCurrentSession();
		
		return tt;
		

/*		if (results.size() > 1) {
			
			return (TreatmentType) results.get(0);
		} else if (results.size() == 1) {
			TreatmentType tt = (TreatmentType) results.get(0);
			Hibernate.initialize(tt);
			return tt;
		}
		System.out.println("No treatment with given name");
		return null;*/
	}

	public TreatmentType get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void add(TreatmentType treatmentType) {
		openCurrentSessionWithTransaction();

		currentSession.save(treatmentType);

		closeCurrentSessionWithTransaction();

	}

}
