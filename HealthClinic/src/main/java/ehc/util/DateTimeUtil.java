package ehc.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
	public Date toDate(String str) {
		DateFormat format = new SimpleDateFormat(Config.DATETIME_FORMAT);
		Date date = null;

		try {
			date = format.parse(str);
		} catch (ParseException e) {

		}

		return date;
	}
	
	public Date getCurrentDate() {
/*		DateFormat format = new SimpleDateFormat(Config.DATETIME_FORMAT);*/
		
		return new Date();
	}
}
