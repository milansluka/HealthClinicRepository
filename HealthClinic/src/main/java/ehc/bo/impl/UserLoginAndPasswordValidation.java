package ehc.bo.impl;

public class UserLoginAndPasswordValidation { 
	private String login;
	private String password;
	private UserDao userDao;

	public UserLoginAndPasswordValidation(String login, String password) {
		super();
		this.login = login;
		this.password = password;
		userDao = UserDao.getInstance();
	}
	
	//checks if login name is unique
	public boolean loginIsValid() {
		if (!login.isEmpty())
		{		
			/*String hql = "FROM User u WHERE u.login = :login";
			Query query = session.createQuery(hql);
			query.setParameter("login", login);
			List results = query.list();*/
			
			User existingUser = userDao.findByLogin(login);

			return existingUser == null;
		}

		return false;

	}

	public boolean passwordIsValid() {
		if (password.isEmpty()) {
			return false;
		}
		
		return true;
	}
}
