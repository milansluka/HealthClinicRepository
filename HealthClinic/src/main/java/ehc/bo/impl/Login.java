package ehc.bo.impl;

public class Login {
/*	 private boolean loginSuccess; */
	 private UserDao userDao = UserDao.getInstance();

	public Login() {
		super();
/*		loginSuccess = false;*/
	}
	
	public User login(String name, String password) {
		return userDao.findByNameAndPassword(name, password);
	}

/*	public boolean tryLogin(User user) {
		if (user != null) {

			Session session = HibernateUtil.getCurrentSession();
			
			String hql = "FROM User u WHERE u.login = :login and u.password = :password";
			Query query = session.createQuery(hql);
			query.setParameter("login", user.getName());
			query.setParameter("password", user.getPassword());
			List results = query.list();
			
			loginSuccess = true;
			
			return !results.isEmpty();
		}
		return false;
	}*/

/*	public boolean isLoginSuccess() {
		return loginSuccess;
	}*/	
}
