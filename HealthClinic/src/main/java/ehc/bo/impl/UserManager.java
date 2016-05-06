package ehc.bo.impl;

import java.util.Date;

import ehc.bo.UserDao;
import ehc.util.Util;

public class UserManager {
	private UserDao userDao;
	
	public UserManager() {
		super();
	}
		
	public UserManager(User superUser) {
		super();
		userDao = new UserDaoImpl();
	}
	
	public void addUser(User user) {
		String psw = user.getPassword();
		String encryptedPsw = new Util().cryptWithMD5(psw);
		user.setPassword(encryptedPsw);	
		Date currentDate = new Date();
		user.setCreatedOn(currentDate);
	/*	user.setCreatedBy(null);*/
		
		userDao.addUser(user);
	}
	

	public User login(String login, String password) {	
		User user = new User();
		user.setLogin(login);
		String encryptedPsw = new Util().cryptWithMD5(password);
		user.setPassword(encryptedPsw);
		return userDao.findUserByLogin(user);
	}

}
