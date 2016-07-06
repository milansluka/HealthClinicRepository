package ehc.bo.test;

import ehc.bo.impl.Login;
import ehc.bo.impl.User;
import ehc.hibernate.HibernateUtil;
import ehc.util.Util;
import junit.framework.TestCase;

public class UserLoginWrongLoginOrPassword extends TestCase {

	public void testApp() {
		String userLogin = "";
		String userPassword = "admin";
		
		Login login = new Login();
	
		HibernateUtil.beginTransaction();
	
		User loggedUser;
		String userPasswordMaybeCrypted = null;
		
		//default user password is not crypted	
		if (userPassword.equals("admin")) {
			loggedUser = login.login(userLogin, userPassword);	
			userPasswordMaybeCrypted = userPassword;
		} else {
			String userPasswordCrypted = (new Util()).cryptWithMD5(userPassword);
			loggedUser = login.login(userLogin, userPasswordCrypted);
			
			userPasswordMaybeCrypted = userPasswordCrypted;
		}
		
		HibernateUtil.commitTransaction();
		
		assertNull(loggedUser);
		
	}

}
