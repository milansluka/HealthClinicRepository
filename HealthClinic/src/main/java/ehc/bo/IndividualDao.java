package ehc.bo;

import java.util.List;

import ehc.bo.impl.Individual;

public interface IndividualDao {
	void addIndividual(Individual individual);
	void deleteIndividual(Individual individual);
	Individual findIndividual(Individual individual);
	void updateIndividual(Individual individual);
	List<Individual> getAllIndividuals();

}
