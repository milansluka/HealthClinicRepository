package ehc.bo.test;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import ehc.bo.TreatmentTypeDao;
import ehc.bo.impl.Appointment;
import ehc.bo.impl.AppointmentManager;
import ehc.bo.impl.Individual;
import ehc.bo.impl.TreatmentType;
import ehc.bo.impl.TreatmentTypeDaoImpl;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.DateUtil;
import junit.framework.TestCase;


public class CreateAppointmentTest extends TestCase {
	private SessionFactory sessionFactory;

	public void testApp() {

		sessionFactory = new Configuration().configure().buildSessionFactory();

		/* createUser(); */
		loginSuccessful();
		loginFailed();
		createAppointment();
		createAppointmentNewPerson();
	}

	public void createUser2() {

	}

	/*
	 * public void createUser() { Session session =
	 * sessionFactory.openSession(); session.beginTransaction();
	 * 
	 * User user = new User(); user.setLogin("admin4");
	 * 
	 * String psw = "12345"; String encryptedPsw = new Util().cryptWithMD5(psw);
	 * 
	 * user.setPassword(encryptedPsw);
	 * 
	 * UserValidation validation = new UserValidation(user, sessionFactory);
	 * 
	 * if (validation.loginIsValid() && validation.passwordIsValid()) {
	 * Permission right = new Permission();
	 * right.setType(UserRightType.CREATE_APPOINTMENT);
	 * 
	 * user.assignRight(right);
	 * 
	 * long userId = (Long) session.save(user);
	 * 
	 * session.getTransaction().commit(); session.close(); }
	 * 
	 * assertTrue(user.getLogin().equals("admin4") &&
	 * user.getPassword().equals(encryptedPsw)); }
	 */

	public void loginSuccessful() {
		/*
		 * Session session = new Util().getSession();
		 * session.beginTransaction();
		 * 
		 * User user = new User(); user.setLogin("admin3");
		 * 
		 * String psw = "12345"; String encryptedPsw = new
		 * Util().cryptWithMD5(psw);
		 * 
		 * user.setPassword(encryptedPsw);
		 * 
		 * Login login = new Login(session);
		 * 
		 * assertTrue(login.tryLogin(user));
		 * 
		 * session.close();
		 */
	}

	public void loginFailed() {
		/*
		 * Session session = new Util().getSession();
		 * session.beginTransaction();
		 * 
		 * User user = new User(); user.setLogin("aaaaa");
		 * user.setPassword("222");
		 * 
		 * Login login = new Login(session);
		 * 
		 * assertTrue(!login.tryLogin(user));
		 * 
		 * session.close();
		 */
	}

	public void createAppointmentNewPerson() {
		
		User executor = new User();


		Individual person = new Individual();
		person.setName("Milan2");
		person.setFirstName("Sluka2");
		person.setPhone("0000011");

		TreatmentType treatmentType = new TreatmentType();
		treatmentType.setName("some name");
		treatmentType.setInfo("some info");
		treatmentType.setType("some type");
		treatmentType.setCreatedOn(new Date());

		TreatmentTypeDao trDao = new TreatmentTypeDaoImpl();
		if (trDao.find(treatmentType) == null) {
			trDao.add(treatmentType);
		}

		Appointment appointment = new Appointment(executor);

		Date from = DateUtil.date(2016, 4, 20, 10, 0, 0);
		Date to = DateUtil.date(2016, 4, 20, 10, 30, 0);

		if (from == null || to == null) {
			assertTrue(false);
			return;
		}

		appointment.setFrom(from);
		appointment.setTo(to);

		appointment.assignPerson(person);
		appointment.assignTreatmentType(treatmentType);

		AppointmentManager manager = new AppointmentManager();
		manager.createAppointment(appointment);

	}

	public void createAppointment() {

		HibernateUtil.beginTransaction();
		User executor = new User();
		Appointment appointent = new Appointment(executor);
		HibernateUtil.save(appointent);

		HibernateUtil.commitTransaction();


	}

}
