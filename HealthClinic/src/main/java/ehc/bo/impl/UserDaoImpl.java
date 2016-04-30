package ehc.bo.impl;

import java.util.List;



import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import ehc.bo.UserDao;
import ehc.util.Util;

public class UserDaoImpl extends Dao implements UserDao{

	public void addUser(User user) {
		openCurrentSessionWithTransaction();		
		currentSession.save(user);	
		closeCurrentSessionWithTransaction();
		
	}

	public void deleteUser(User user) {
		currentSession.delete(user);
		
	}

	public User findUserByLogin(User user) {
		openCurrentSession();
		String login = user.getLogin();
		String password = user.getPassword();
		
		
		String hql = "FROM User u WHERE u.login = :login and u.password = :password";
		Query query = currentSession.createQuery(hql);
		query.setParameter("login", user.getLogin());
		query.setParameter("password", user.getPassword());
		
		List results = query.list();
		
		closeCurrentSession();
		
		if (!results.isEmpty()) {
			return (User)results.get(0);
			
		}
		
		return null;
	}

	public void updateUser(User user) {
		long id = user.getId();
		String hql = "UPDATE User u set u.login = :login and u.password = :password" + 
		"WHERE u.id = :id";
		Query query = currentSession.createQuery(hql);
		query.setParameter("id", user.getId());
	    query.executeUpdate();		
	}

	public List<User> getAllUsers() {
		String hql = "FROM User";
		Query query = currentSession.createQuery(hql);
		List results = query.list();
		return results;
	}
}
