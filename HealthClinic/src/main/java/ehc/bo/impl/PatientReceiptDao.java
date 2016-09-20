package ehc.bo.impl;

import org.hibernate.Session;

import ehc.hibernate.HibernateUtil;

public class PatientReceiptDao {
	private static PatientReceiptDao instance = new PatientReceiptDao();
	
	private PatientReceiptDao() {
	}

	public static PatientReceiptDao getInstance() {
		return instance;
	}
	
	public PatientReceipt findById(long id) {
		Session session = HibernateUtil.getCurrentSession();	
		PatientReceipt patientReceipt = session.get(PatientReceipt.class, id);	
		return patientReceipt;
	}
}
