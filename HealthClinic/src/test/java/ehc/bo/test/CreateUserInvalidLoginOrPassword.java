package ehc.bo.test;

import ehc.bo.impl.UserLoginAndPasswordValidation;
import ehc.hibernate.HibernateUtil;
import junit.framework.TestCase;

public class CreateUserInvalidLoginOrPassword extends TestCase {

	private String newUserLogin = "admin";
	private String newUserPassword = "admin";

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testApp() {
		createUserWithNonUniqueLogin();
	}

	private void createUserWithNonUniqueLogin() {
		HibernateUtil.beginTransaction();

		UserLoginAndPasswordValidation validation = new UserLoginAndPasswordValidation(newUserLogin, newUserPassword);

		assertFalse(validation.loginIsValid());

		HibernateUtil.commitTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
