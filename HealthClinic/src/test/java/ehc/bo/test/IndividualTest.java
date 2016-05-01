package ehc.bo.test;

import java.util.Date;

import ehc.bo.IndividualDao;
import ehc.bo.impl.Individual;
import ehc.bo.impl.IndividualDaoImpl;
import junit.framework.TestCase;

public class IndividualTest extends TestCase {
	public void testApp() {
		Individual person = new Individual();
		
		person.setName("Sluka");
		person.setFirstName("Milan");
		person.setPhone("0910333084");
		person.setCreatedOn(new Date());
		
		IndividualDao iDao = new IndividualDaoImpl();
		iDao.addIndividual(person);
		
		person = new Individual();
		person.setName("Sluka");
		person.setFirstName("Milan");
		person.setPhone("0910333084");
		person.setCreatedOn(new Date());
		
		Individual foundPerson = new Individual();
		
		foundPerson = iDao.findIndividual(person);
		
		assertNotNull(foundPerson);
		
		
	}

}
