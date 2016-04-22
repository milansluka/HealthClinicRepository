package milansluka.HealthClinic.bo.impl;

import org.hibernate.SessionFactory;

import ehc.bo.impl.User;
import ehc.bo.impl.UserRight;

public class UserRightsManager {
	private SessionFactory sessionFactory;

	public UserRightsManager(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}
	
	public void assignUserRightUser(User user, UserRight right) {
		
		
	}

}
