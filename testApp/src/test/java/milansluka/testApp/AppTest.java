package milansluka.testApp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
		SessionFactory sessionFactory = new Configuration().configure()
				.buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
 
		Person person = new Person("Milan", "Sluka");
		
		long personId = (Long)session.save(person);
		
		session.getTransaction().commit();
		
		String hql = "FROM Person p WHERE p.id = :person_id";
		Query query = session.createQuery(hql);
		query.setParameter("person_id", personId);
		List results = query.list();
		
		Person milan = (Person)results.get(0);
		
		session.close();
		
		assertTrue(milan.getFirstName().equals("Milan") && milan.getLastName().equals("Sluka"));
		
    }
}
