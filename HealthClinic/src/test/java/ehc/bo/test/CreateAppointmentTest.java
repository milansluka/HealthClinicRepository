package ehc.bo.test;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentManager;
import ehc.bo.impl.Treatment;
import ehc.bo.impl.Login;
import ehc.bo.impl.Individual;
import ehc.bo.impl.User;
import ehc.bo.impl.Permission;
import ehc.bo.impl.UserRightType;
import ehc.bo.impl.UserValidation;
import ehc.util.Util;
import junit.framework.TestCase;

public class CreateAppointmentTest extends TestCase {
	private SessionFactory sessionFactory;

	public void testApp() {

		sessionFactory = new Configuration().configure().buildSessionFactory();

	/*	createUser();*/
		loginSuccessful();
		loginFailed();
		createAppointment();
		createAppointmentNewPerson();
	}
	
	public void createUser2() {
		
	}

/*	public void createUser() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		User user = new User();
		user.setLogin("admin4");
		
		String psw = "12345";
		String encryptedPsw = new Util().cryptWithMD5(psw);
		
		user.setPassword(encryptedPsw);

		UserValidation validation = new UserValidation(user, sessionFactory);

		if (validation.loginIsValid() && validation.passwordIsValid()) {
			Permission right = new Permission();
			right.setType(UserRightType.CREATE_APPOINTMENT);

			user.assignRight(right);

			long userId = (Long) session.save(user);

			session.getTransaction().commit();
			session.close();
		}

		assertTrue(user.getLogin().equals("admin4") && user.getPassword().equals(encryptedPsw));
	}*/

	public void loginSuccessful() {
/*		Session session = new Util().getSession();
		session.beginTransaction();

		User user = new User();
		user.setLogin("admin3");
		
		String psw = "12345";
		String encryptedPsw = new Util().cryptWithMD5(psw);
		
		user.setPassword(encryptedPsw);

		Login login = new Login(session);

		assertTrue(login.tryLogin(user));

		session.close();*/
	}

	public void loginFailed() {
/*		Session session = new Util().getSession();
		session.beginTransaction();

		User user = new User();
		user.setLogin("aaaaa");
		user.setPassword("222");

		Login login = new Login(session);

		assertTrue(!login.tryLogin(user));

		session.close();*/
	}
	
	public void createAppointmentNewPerson() {

/*		Individual person = new Individual();
		person.setName("Milan2");
		person.setFirstName("Sluka2");
		person.setPhone("0000011");
		
		Session session = new Util().getSession();
		session.beginTransaction();
        session.save(person);
        
        session.getTransaction().commit();
		session.close();

		Treatment intervention = new Treatment();
		intervention.setName("some name");
		intervention.setInfo("some info");

		Appointment appointment = new Appointment();
		String strFrom = "20.4.2016 10:00";
		String strTo = "20.4.2016 10:30";

		Util utils = new Util();
		Date from = utils.toDate(strFrom);
		Date to = utils.toDate(strTo);

		if (from == null || to == null) {
			assertTrue(false);
			return;
		}

		appointment.setFrom(from);
		appointment.setTo(to);

		appointment.assignPerson(person);
		appointment.assignTreatment(intervention);

		AppointmentManager manager = new AppointmentManager();
		manager.createAppointment(appointment);*/

	}

	public void createAppointment() {

/*		Individual person = new Individual();
		person.setName("Milan");
		person.setFirstName("Sluka");
		person.setPhone("012345");

		Treatment intervention = new Treatment();
		intervention.setName("some name");
		intervention.setInfo("some info");

		Appointment appointment = new Appointment();
		String strFrom = "17.4.2016 10:00";
		String strTo = "17.4.2016 10:30";

		Util utils = new Util();
		Date from = utils.toDate(strFrom);
		Date to = utils.toDate(strTo);

		if (from == null || to == null) {
			assertTrue(false);
			return;
		}

		appointment.setFrom(from);
		appointment.setTo(to);

		appointment.assignPerson(person);
		appointment.assignTreatment(intervention);

		AppointmentManager manager = new AppointmentManager();
		manager.createAppointment(appointment);*/

	}

}
