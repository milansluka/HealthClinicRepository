package ehc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * {@code DateInterval} is a utility class modeling a ... date interval!
 * It contains static methods to get certain predefined intervals without the
 * time component of the {@code Date} class.
 *
 * @author m.krueger
 *
 */
public class DateInterval {

	private static final long DAY_IN_MILLISECONDS = 86400000; //1000*60*60*24 
	private static final SimpleDateFormat FORMAT_DATETIME_YEAR_FIRST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat FORMAT_DATETIME = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("dd.MM.yyyy");
	private Date begin;
	private Date end;
	
	public DateInterval() {
	}
	
	public DateInterval(Date begin, Date end) {
		this(begin, end, false);
	}
	
	public DateInterval(Date begin, Date end, boolean includeLastDay) {
		if ((begin == null && end == null) || (begin != null && end != null && begin.getTime() > end.getTime()))
			throw new IllegalArgumentException("Illegal interval boundaries.");
		this.begin = begin;
		if (includeLastDay && end != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(end);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			this.end = cal.getTime();
		}
		else
			this.end = end;
	}
	
	/**
	 * Creates an interval starting with the day of the begin {@code Date} at 00:00
	 * and ending on the day of the end {@code Date} at 24:00 (or the next day, 00:00).
	 *  
	 * @param begin  the full date (with time component) of the start day.
	 * @param end  the full date (with time component) of the end day.
	 * @return  the interval bounded by the start and end days (no time components).
	 */
	public static DateInterval getDays(Date begin, Date end) {
		return new DateInterval(normalise(begin, false), normalise(end, true));
	}
	
	public static DateInterval last30Days() {
		return lastXDays(30);
	}
	
	public static DateInterval last90Days() {
		return lastXDays(90);
	}
	
	public static DateInterval lastXDays(int days) {
		Date now = new Date();
		return new DateInterval(normaliseDays(now, -days).getTime(), normaliseDays(now, 1).getTime());
	}
	
	public static DateInterval currentMonth() {
		return currentMonth(new Date());
	}
	
	public static DateInterval currentMonth(Date now) {
		DateInterval interval = new DateInterval();
		Calendar cal = normaliseDays(now, null);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		interval.setBegin(cal.getTime());
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
		interval.setEnd(cal.getTime());
		return interval;
	}
	
	public static DateInterval lastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		return currentMonth(cal.getTime());
	}
	
	public static DateInterval currentQuarter() {
		return currentQuarter(new Date());
	}
	
	public static DateInterval currentQuarter(Date now) {
		DateInterval interval = new DateInterval();
		Calendar cal = normaliseDays(now, null);
		int month = cal.get(Calendar.MONTH);
		switch (month) {
			case 0 :
			case 1 :
			case 2 :
				cal.set(Calendar.MONTH, 0);
				break;
			case 3 :
			case 4 :
			case 5 :
				cal.set(Calendar.MONTH, 3);
				break;
			case 6 :
			case 7 :
			case 8 :
				cal.set(Calendar.MONTH, 6);
				break;
			case 9 :
			case 10 :
			case 11 :
				cal.set(Calendar.MONTH, 9);
		}
		cal.set(Calendar.DAY_OF_MONTH, 1);
		interval.setBegin(cal.getTime());
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+3);
		interval.setEnd(cal.getTime());
		return interval;
	}
	
	public static DateInterval currentYear() {
		return currentYear(new Date());
	}
	
	public static DateInterval currentYear(Date now) {
		DateInterval interval = new DateInterval();
		Calendar cal = normaliseDays(now, null);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 0);
		interval.setBegin(cal.getTime());
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
		interval.setEnd(cal.getTime());
		return interval;
	}
	
	public static DateInterval lastTwelveMonths() {
		DateInterval interval = new DateInterval();
		Calendar cal = normaliseDays(new Date(), null);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, -11);
		interval.setBegin(cal.getTime());
		cal.add(Calendar.MONTH, 12);
		interval.setEnd(cal.getTime());
		return interval;
	}
	
	public static Date normalise(Date date) {
		return normalise(date, false);
	}
	
	public static Date normalise(Date date, boolean includeDay) {
		Calendar normalised = normaliseDays(date, includeDay ? 1 : null);
		return normalised == null ? null : normalised.getTime();
	}
	
	public static Calendar normaliseDays(Date date, Integer dayOffset) {
		if (date == null) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		if (dayOffset != null)
			cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+dayOffset);
		return cal;
	}
	
	public static int getYears(Date start, Date end) {
		Calendar sc = normaliseDays(start, null);
		Calendar ec = normaliseDays(end, 1);
		long diff = (ec.getTimeInMillis() - sc.getTimeInMillis())/DAY_IN_MILLISECONDS;
		return (int) Math.round(diff/365.25d);
	}
	
	public boolean isIntervalWithinYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(begin);
		int startYear = cal.get(Calendar.YEAR);
		cal.setTime(end);
		int endYear = cal.get(Calendar.YEAR);
		return startYear == endYear;
	}
	
	public boolean isNormalised() {
		Calendar cal = Calendar.getInstance();
		boolean normalised = true;
		if (begin != null) {
			cal.setTime(begin);
			normalised = cal.get(Calendar.HOUR_OF_DAY) == 0 & cal.get(Calendar.MINUTE) == 0 & cal.get(Calendar.SECOND) == 0 & cal.get(Calendar.MILLISECOND) == 0;
		}
		if (end != null) {
			cal.setTime(end);
			normalised = normalised & cal.get(Calendar.HOUR_OF_DAY) == 0 & cal.get(Calendar.MINUTE) == 0 & cal.get(Calendar.SECOND) == 0 & cal.get(Calendar.MILLISECOND) == 0;
		}
		return normalised;
	}
	
	public Date getBegin() {
		return begin;
	}
	
	public String getBeginAsString() {
		return begin == null ? null : FORMAT_DATETIME_YEAR_FIRST.format(begin);
	}
	
	public void setBegin(Date begin) {
		if (end != null && begin.getTime() > end.getTime())
			throw new IllegalArgumentException("Begin date after end date.");
		this.begin = begin;
	}
	
	public Date getEnd() {
		return end;
	}
	
	public Date getEnd(int dayOffset) {
		if (end == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(end);
		cal.add(Calendar.DAY_OF_MONTH, dayOffset);
		return cal.getTime();
	}
	
	public String getEndAsString() {
		return end == null ? null : FORMAT_DATETIME_YEAR_FIRST.format(end);
	}
	
	public void setEnd(Date end) {
		if (begin != null && end.getTime() < begin.getTime())
			throw new IllegalArgumentException("End date before begin date.");
		this.end = end;
	}
	
	public boolean containsDate(Date date, boolean open) {
		if (date == null)
			throw new IllegalArgumentException("Cannot check null.");
		return open ? (begin == null || begin.getTime() < date.getTime()) && (end == null || date.getTime() < end.getTime())
					: (begin == null || begin.getTime() <= date.getTime()) && (end == null || date.getTime() <= end.getTime());
	}
	
	public boolean contains(DateInterval other, boolean open) {
		if ((begin != null && other.getBegin() == null) || (end != null && other.getEnd() == null))
			return false;
		return open ? (begin == null || begin.getTime() < other.getBegin().getTime())
						&& (end == null || end.getTime() > other.getEnd().getTime())
					: (begin == null || begin.getTime() <= other.getBegin().getTime())
						&& (end == null || end.getTime() >= other.getEnd().getTime());
	}
	
	/**
	 * This method tests if the interval intersects with a given other interval.
	 * It assumes open intervals {@code (x,y)}, a value of {@code null} means positive (end) or negative (begin) infinity.
	 * 
	 * @param other  the interval to test intersection.
	 * @return  {@code true} if this interval intersects with the other, {@code false} otherwise.
	 */
	public boolean intersects(DateInterval other) {
		if (contains(other, false) || other.contains(this, false))
			return false;
		if ((begin == null && other.getEnd() == null) || (end == null && other.getBegin() == null))
			return begin == null ? end.getTime() > other.getBegin().getTime() : begin.getTime() < other.getEnd().getTime();
		// one endpoint null (only possibility of null value left)
		if (begin == null || other.getBegin() == null || end == null || other.getEnd() == null)
			return begin == null ? end.getTime() > other.getBegin().getTime() :
				other.getBegin() == null ? other.getEnd().getTime() > begin.getTime() :
					end == null ? begin.getTime() < other.getEnd().getTime() : other.getBegin().getTime() < end.getTime();
		// remaining: no null values
		return (other.containsDate(begin, true) && !other.containsDate(end, true))
			|| (!other.containsDate(begin, true) && other.containsDate(end, true));
	}
	
	/**
	 * This method tests if the interval is disjoint to a given other interval.
	 * It assumes open intervals {@code (x,y)}, i.e. {@code (x,y)} and {@code (y,z)} are disjoint.
	 * A value of {@code null} means positive (end) or negative (begin) infinity.
	 * 
	 * @param other  the other interval for testing.
	 * @return  {@code true} if the intervals are disjoint, {@code false} otherwise.
	 */
	public boolean isDisjoint(DateInterval other) {
		return !other.contains(this, false) && !contains(other, false) && !intersects(other);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof DateInterval))
			return false;
		DateInterval otherI = (DateInterval) other;
		return ((begin == null && otherI.getBegin() == null) || (begin != null && otherI.getBegin() != null && begin.equals(otherI.getBegin())))
				&& ((end == null && otherI.getEnd() == null) || (end != null && otherI.getEnd() != null && end.equals(otherI.getEnd())));
	}
	
	@Override
	public int hashCode() {
		return (begin == null ? -1 : begin.hashCode()) * (end == null ? -1 : end.hashCode());
	}
	
	@Override
	public String toString() {
		if (isNormalised()) {
			Date normEnd = end;
			if (end != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(end);
				cal.add(Calendar.DAY_OF_MONTH, -1);
				normEnd = cal.getTime();
			}
			return String.format("%s - %s", begin == null ? "" : FORMAT_DATE.format(begin), normEnd == null ? "" : FORMAT_DATE.format(normEnd));
		}
		else
			return String.format("%s - %s", begin == null ? "" : FORMAT_DATETIME.format(begin), end == null ? "" : FORMAT_DATETIME.format(end));
	}
}
