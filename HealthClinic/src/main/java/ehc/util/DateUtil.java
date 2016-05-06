package ehc.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

	public static final int MILLISECONDS_PER_HOUR = 1000 * 3600;
	public static final int MILLISECONDS_PER_DAY = MILLISECONDS_PER_HOUR * 24;
	public static final Date MAX_SQL_DATE = maxSqlDate();
	public static final int MAX_SQL_DATE_YYYYMMDD = 99991231;
	public static final Date MIN_SQL_DATE = minSqlDate();
	public static final int MIN_SQL_DATE_YYYYMMDD = 17530101;

	private static final Pattern DATE_YYYY_MM_DD_PATTERN = Pattern
			.compile("\\d{4}-\\d{2}-\\d{2}");
	private static final String DATE_YYYY_MM_DD_FORMAT = "yyyy-MM-dd";

	private static final Pattern DATE_DD_MM_YYYY_PATTERN = Pattern
			.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
	private static final String DATE_DD_MM_YYYY_FORMAT = "dd.MM.yyyy";
	private static final DatatypeFactory datatypeFactory;

	static {
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new Error(e);
		}
	}

	private static Date maxSqlDate() {
		try {
			return new SimpleDateFormat("yyyyMMdd").parse(Integer
					.toString(MAX_SQL_DATE_YYYYMMDD));
		} catch (ParseException cannotHappen) {
			throw new RuntimeException("Unexpected exception", cannotHappen);
		}
	}

	private static Date minSqlDate() {
		try {
			return new SimpleDateFormat("yyyyMMdd").parse(Integer
					.toString(MIN_SQL_DATE_YYYYMMDD));
		} catch (ParseException cannotHappen) {
			throw new RuntimeException("Unexpected exception", cannotHappen);
		}
	}

	/**
	 * copies the <code>Date</code> since <code>Date</code> is mutable, so one
	 * might want to have a save copy. Another use case is to wrap a
	 * java.sql.Timestamp into a java.util.Date for comparing it with another
	 * java.util.Date.
	 * 
	 * @param date
	 *            the date to copy
	 * @return a copy of the given date
	 */
	public static Date copy(Date date) {
		if (date == null)
			return null;
		return new Date(date.getTime());
	}

	public static GregorianCalendar today() {
		GregorianCalendar today = new GregorianCalendar();
		today.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
		today.set(GregorianCalendar.HOUR, 0);
		today.set(GregorianCalendar.MINUTE, 0);
		today.set(GregorianCalendar.SECOND, 0);
		today.set(GregorianCalendar.MILLISECOND, 0);

		return today;
	}

	public static Date todayAsDate() {
		return today().getTime();
	}

	public static Date now() {
		return new Date();
	}

	public static int hoursSince(Date d) {
		return hoursBetween(d, now());
	}

	public static int daysSince(Calendar c) {
		return daysBetween(c, today());
	}

	public static int daysSince(Date d) {
		return daysBetween(d, todayAsDate());
	}

	public static int yearsSince(Calendar c) {
		return fullYearsBetween(c, today());
	}

	public static int yearsSince(Date d) {
		return fullYearsBetween(d, todayAsDate());
	}

	public static int fullYearsBetween(Date earlier, Date later) {
		GregorianCalendar cEarlier = new GregorianCalendar();
		GregorianCalendar cLater = new GregorianCalendar();
		cEarlier.setTime(earlier);
		cLater.setTime(later);
		return fullYearsBetween(cEarlier, cLater);
	}

	public static int fullYearsBetween(Calendar earlier, Calendar later) {
		if (earlier.after(later))
			return -fullYearsBetween(later, earlier);

		int yearsBetween = later.get(Calendar.YEAR)
				- earlier.get(Calendar.YEAR);
		int monthsDiff = later.get(Calendar.MONTH)
				- earlier.get(Calendar.MONTH);
		if (monthsDiff < 0
				|| (monthsDiff == 0 && later.get(Calendar.DAY_OF_MONTH) < earlier
						.get(Calendar.DAY_OF_MONTH)))
			yearsBetween--;

		return yearsBetween;
	}

	public static int fullMonthsBetween(Calendar earlier, Calendar later) {
		if (earlier.after(later))
			return -fullMonthsBetween(later, earlier);

		int yearsBetweenInMonths = (later.get(Calendar.YEAR) - earlier
				.get(Calendar.YEAR)) * 12;
		int monthsBetween = yearsBetweenInMonths + later.get(Calendar.MONTH)
				- earlier.get(Calendar.MONTH);
		int dayDiff = later.get(Calendar.DAY_OF_MONTH)
				- earlier.get(Calendar.DAY_OF_MONTH);
		if (dayDiff < 0)
			monthsBetween--;
		return monthsBetween;
	}

	public static int fullMonthsBetween(Date earlier, Date later) {
		GregorianCalendar cEarlier = new GregorianCalendar();
		GregorianCalendar cLater = new GregorianCalendar();
		cEarlier.setTime(earlier);
		cLater.setTime(later);
		return fullMonthsBetween(cEarlier, cLater);
	}

	public static int daysBetween(Calendar earlier, Calendar later) {
		return daysBetweenMillis(earlier.getTimeInMillis(),
				later.getTimeInMillis());
	}

	public static int daysBetween(Date earlier, Date later) {
		return daysBetweenMillis(earlier.getTime(), later.getTime());
	}

	public static int hoursBetween(Date earlier, Date later) {
		return hoursBetweenMillis(earlier.getTime(), later.getTime());
	}

	private static int daysBetweenMillis(long earlierMillis, long laterMillis) {
		return (int) ((laterMillis - earlierMillis) / MILLISECONDS_PER_DAY);
	}

	private static int hoursBetweenMillis(long earlierMillis, long laterMillis) {
		return (int) ((laterMillis - earlierMillis) / MILLISECONDS_PER_HOUR);
	}

	/**
	 * @param ddmmyyyyhhmm
	 * @return null if yyyy is not <= 9999
	 */
	public static GregorianCalendar yyyymmddToCalendar(int yyyymmdd) {
		GregorianCalendar c = yyyymmdd == 0 ? null : new GregorianCalendar(
				yyyymmdd / 10000, (yyyymmdd % 10000) / 100 - 1, yyyymmdd % 100);
		return c != null && isValidSQLDate(c.getTimeInMillis()) ? c : null;
	}

	/**
	 * @param ddmmyyyyhhmm
	 * @return null if yyyy is not <= 9999
	 */
	public static Date yyyymmddToDate(int yyyymmdd) {
		GregorianCalendar calendar = yyyymmddToCalendar(yyyymmdd);
		if (calendar == null)
			return null;
		return calendar.getTime();
	}

	/**
	 * @param yyyymmdd
	 * @return null if input is not a valid date with year <= 9999
	 */
	public static Date yyyymmddToDate(String yyyymmdd) {
		return parseAndValidateDateString(yyyymmdd, "yyyyMMdd");
	}

	/**
	 * @param yyyymmdd
	 * @return null if input is not a valid date with year <= 9999
	 */
	public static GregorianCalendar yyyymmddToCalendar(String yyyymmdd) {
		Date d = parseAndValidateDateString(yyyymmdd, "yyyyMMdd");
		if (d != null) {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(d);
			return c;
		} else
			return null;
	}

	/**
	 * @param ddmmyyyy
	 * @return null if input is not a valid date with year <= 9999
	 */
	public static Date ddmmyyyyToDate(String ddmmyyyy) {
		return parseAndValidateDateString(ddmmyyyy,
				ddmmyyyy.indexOf('.') != -1 ? "dd.MM.yyyy" : "ddMMyyyy");
	}

	/**
	 * @param ddmmyyyyhhmm
	 * @return null if input is not a valid date with year <= 9999
	 */
	public static Date ddmmyyyyhhmmToDate(String ddmmyyyyhhmm) {
		return parseAndValidateDateString(ddmmyyyyhhmm, "dd.MM.yyyy HH:mm");
	}

	private static Date parseAndValidateDateString(String dateString,
			String dateFormat) {
		SimpleDateFormat f = new SimpleDateFormat(dateFormat);
		f.setLenient(false);
		try {
			Date d = f.parse(dateString);
			if (isValidSQLDate(d))
				return d;
		} catch (ParseException e) {
		}
		return null;
	}

	public static GregorianCalendar calendar(int year, int month, int day) {
		return new GregorianCalendar(year, month - 1, day);
	}

	public static GregorianCalendar calendar(int year, int month, int day,
			int hour, int minute, int second) {
		return new GregorianCalendar(year, month - 1, day, hour, minute, second);
	}

	public static Date date(int year, int month, int day) {
		return new GregorianCalendar(year, month - 1, day).getTime();
	}

	public static Date date(int year, int month, int day, int hour, int minute,
			int second) {
		return new GregorianCalendar(year, month - 1, day, hour, minute, second)
				.getTime();
	}

	public static Date date(String date) {
		return parse(date);
	}

	public static Date parse(String date) throws IllegalArgumentException {
		if (date == null)
			return null;

		// TODO currently the BusinessUnitHistory date format is "dd.MM.yyyy"
		// but maybe we change it someday to "yyyy-MM-dd".
		Matcher matcher = DATE_DD_MM_YYYY_PATTERN.matcher(date);
		if (matcher.matches()) {
			try {
				return new SimpleDateFormat(DATE_DD_MM_YYYY_FORMAT).parse(date);
			} catch (ParseException e) {
				// should not happen since pattern is checked
			}
		}
		matcher = DATE_YYYY_MM_DD_PATTERN.matcher(date);
		if (matcher.matches()) {
			try {
				return new SimpleDateFormat(DATE_YYYY_MM_DD_FORMAT).parse(date);
			} catch (ParseException e) {
				// should not happen since pattern is checked
			}
		}
		throw new IllegalArgumentException("Illegal argument date [" + date
				+ "]; allowed formats: yyyy-MM-dd and dd.MM.yyyy");
	}

	private static boolean isValidSQLDate(Date d) {
		return !(d.before(MIN_SQL_DATE) || d.after(MAX_SQL_DATE));
	}

	private static boolean isValidSQLDate(long millis) {
		return millis >= MIN_SQL_DATE.getTime()
				&& millis <= MAX_SQL_DATE.getTime();
	}

	public static String prettyPrintSeconds(long milliseconds) {
		long seconds = (milliseconds + 500) / 1000;
		return prettyPrintMillis(null, seconds);
	}

	public static String prettyPrintMilliseconds(long milliseconds) {
		long seconds = milliseconds / 1000;
		return prettyPrintMillis(milliseconds, seconds);
	}

	private static String prettyPrintMillis(Long milliseconds, long seconds) {
		String result = "";
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;

		if (milliseconds != null) {
			milliseconds = milliseconds % 60;
			result = milliseconds + "ms" + result;
			if (seconds == 0)
				return result;
			else
				result = " " + ((milliseconds < 100) ? "0" : "")
						+ ((milliseconds < 10) ? "0" : "") + result;
		}

		seconds = seconds % 60;
		result = seconds + "s" + result;
		if (minutes == 0)
			return result;
		else
			result = " " + ((seconds < 10) ? "0" : "") + result;

		minutes = minutes % 60;
		result = minutes + "m" + result;
		if (hours == 0)
			return result;
		else
			result = " " + ((minutes < 10) ? "0" : "") + result;

		hours = hours % 24;
		result = hours + "h" + result;
		if (days == 0)
			return result;
		else
			result = " " + ((hours < 10) ? "0" : "") + result;

		result = days + "d" + result;
		return result;
	}

	private static Date addTimeUnits(Date startingPoint, int timeUnits,
			int count) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startingPoint);
		calendar.add(timeUnits, count);
		return calendar.getTime();
	}

	public static Date addYears(Date startingPoint, int count) {
		return addTimeUnits(startingPoint, Calendar.YEAR, count);
	}

	public static Date addYears(int count) {
		return addYears(todayAsDate(), count);
	}

	public static Date addMonths(Date startingPoint, int count) {
		return addTimeUnits(startingPoint, Calendar.MONTH, count);
	}

	public static Date addMonths(int count) {
		return addMonths(todayAsDate(), count);
	}

	public static Date addDays(Date startingPoint, int count) {
		return addTimeUnits(startingPoint, Calendar.DATE, count);
	}

	public static Date addSeconds(Date startingPoint, int count) {
		return addTimeUnits(startingPoint, Calendar.SECOND, count);
	}

	public static Date addDays(int count) {
		return addDays(todayAsDate(), count);
	}

	public static int compareDates(Date first, Date second) {
		Date now = DateUtil.now();
		if (first == null)
			first = now;
		if (second == null)
			second = now;
		return first.compareTo(second);
	}

	public static Date normDays(Date date) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		return cal.getTime();
	}

	public static Date yesterday(Date current) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public static int dateToYyyymmdd(Date date) {
		if (date == null) {
			return 0;
		}
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return (cal.get(Calendar.YEAR) * 10000)
				+ ((cal.get(Calendar.MONTH) + 1) * 100)
				+ cal.get(Calendar.DAY_OF_MONTH);
	}

	public static Date convertToDate(XMLGregorianCalendar xmlGregorianCalendar){
		return xmlGregorianCalendar == null ? null : xmlGregorianCalendar
				.toGregorianCalendar().getTime();
	}

	public static XMLGregorianCalendar convertToXmlGregorianCalendar(Date date) {
		if (date != null) {
			GregorianCalendar lastUpdateOn = new GregorianCalendar();
			lastUpdateOn.setTime(date);
			return datatypeFactory.newXMLGregorianCalendar(lastUpdateOn);
		} else
			return null;
	}

	public static XMLGregorianCalendar nowAsXMLGregorianCalendar() {
		return convertToXmlGregorianCalendar(now());
	}

	public static boolean isNowBetween(Date minDate, Date maxDate) {
		return (maxDate == null && minDate == null)
				|| (maxDate == null && daysSince(minDate) >= 0)
				|| (minDate == null && daysSince(maxDate) <= 0)
				|| (daysSince(minDate) >= 0 && daysSince(maxDate) <= 0);
	}

	public static boolean isDateBetween(Date date, Date minDate, Date maxDate) {
		return (maxDate == null && minDate == null)
				|| (maxDate == null && daysBetween(minDate, date) >= 0)
				|| (minDate == null && daysBetween(date, maxDate) >= 0)
				|| (daysBetween(minDate, date) >= 0 && daysBetween(date,
						maxDate) >= 0);
	}

	public static Date toDate(LocalDate localDate) {
		if (localDate == null)
			return null;
		Instant instant = localDate.atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	
	}
}