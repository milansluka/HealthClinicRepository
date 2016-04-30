package ehc.bo.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import ehc.bo.UserDao;
import ehc.util.DateTimeUtil;
import ehc.util.Util;

public class UserManager {
	private UserDao userDao;
	private User superUser;
	
	public UserManager() {
		super();
	}
		
	public UserManager(User superUser) {
		super();
		userDao = new UserDaoImpl();
		this.superUser = superUser;
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
