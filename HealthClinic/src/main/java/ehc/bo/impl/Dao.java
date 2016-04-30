package ehc.bo.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import ehc.util.Util;

public class Dao {
	protected Session currentSession;
	protected Transaction currentTransaction;
	
	protected void openCurrentSession() {
		SessionFactory sessionFactory = new Util().getSessionFactory();
		currentSession = sessionFactory.openSession();
	}
	
	protected void openCurrentSessionWithTransaction() {
		SessionFactory sessionFactory = new Util().getSessionFactory();
		currentSession = sessionFactory.openSession();
		currentTransaction = currentSession.beginTransaction();	
	}
	
	protected void closeCurrentSession() {
		currentSession.close();	
	
	}
	
	protected void closeCurrentSessionWithTransaction() {
		currentTransaction.commit();
		currentSession.close();	
	}

}
