package ehc.bo.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;

import ehc.bo.IndividualDao;

public class IndividualDaoImpl extends Dao implements IndividualDao {

	public void addIndividual(Individual individual) {
		openCurrentSessionWithTransaction();
		
		individual.setCreatedOn(new Date());
		currentSession.save(individual);

		closeCurrentSessionWithTransaction();

	}

	public void deleteIndividual(Individual individual) {
		openCurrentSessionWithTransaction();

		closeCurrentSessionWithTransaction();

	}

	public Individual findIndividual(Individual individual) {
		openCurrentSessionWithTransaction();
		String firstName = individual.getFirstName();
		String name = individual.getName();
		String phone = individual.getPhone();
		
		
		String hql = "FROM Individual i WHERE i.firstName = :firstName and i.name = :name " + 
		"and i.phone = :phone";
		Query query = currentSession.createQuery(hql);
		query.setParameter("firstName", firstName);
		query.setParameter("name", name);
		query.setParameter("phone", phone);
		
		List results = query.list();
		Individual i = null;
		
		if (results.size() > 1) {
			System.out.println("More individuals with the same first name, last name and phone number");
			i = (Individual)results.get(0);
		} else if (results.size() == 1) {
			i = (Individual)results.get(0);			
		}
		
		if (i != null) {
			Hibernate.initialize(i.getAppointments());
		}
		
		closeCurrentSessionWithTransaction();
		
		
		System.out.println("No individual with given first name, last name and phone number");
		return i;
	}

	public void updateIndividual(Individual individual) {
		openCurrentSessionWithTransaction();

		closeCurrentSessionWithTransaction();

	}

	public List<Individual> getAllIndividuals() {
		// TODO Auto-generated method stub
		return null;
	}

}
