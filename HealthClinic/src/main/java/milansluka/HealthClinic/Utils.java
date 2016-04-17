package milansluka.HealthClinic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Utils {
	public Date toDate(String str) {
		DateFormat format = new SimpleDateFormat(Config.DATETIME_FORMAT);
		Date date = null;

		try {
			date = format.parse(str);
		} catch (ParseException e) {

		}
		
		return date;
	}
	
	public Session getSession() {
		SessionFactory sessionFactory = new Configuration().configure()
				.buildSessionFactory();
		return sessionFactory.openSession();		
	}
}
