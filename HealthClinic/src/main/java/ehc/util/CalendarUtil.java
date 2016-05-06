package ehc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarUtil {

	public static enum TypeOfDay {
		WORKING_DAY,
		SATURDAY,
		SUNDAY,
		PUBLIC_HOLIDAY;

		public boolean isWorkingDay() {
			return this == WORKING_DAY;
		}
	}

	public static interface PublicHoliday {
		public boolean isPublicHoliday(Calendar calendar);
	}

	public static class FixedPublicHoliday implements PublicHoliday {
		int month;
		int day;

		public FixedPublicHoliday(int month, int day) {
			this.month = month;
			this.day = day;
		}

		public boolean isPublicHoliday(Calendar calendar) {
			return month == calendar.get(Calendar.MONTH) && day == calendar.get(Calendar.DAY_OF_MONTH);
		}
	}

	public static class FloatingPublicHoliday implements PublicHoliday {
		int diff;

		public FloatingPublicHoliday(int diff) {
			this.diff = diff;
		}

		public boolean isPublicHoliday(Calendar calendar) {
			Calendar publicHoliday = easterSunday(calendar.get(Calendar.YEAR));
			publicHoliday.add(Calendar.DATE, diff);
			return publicHoliday.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && publicHoliday.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
		}
	}

	public static final List<PublicHoliday> publicHolidays = new ArrayList<PublicHoliday>();

	static {
		addPublicHoliday(new FixedPublicHoliday( 0,  1)); // Neujahr
		addPublicHoliday(new FixedPublicHoliday( 0,  6)); // Hl. 3 K�nige
		addPublicHoliday(new FixedPublicHoliday( 4,  1)); // Staatsfeiertag
		addPublicHoliday(new FixedPublicHoliday( 7, 15)); // Mari� Himmelfahrt
		addPublicHoliday(new FixedPublicHoliday( 9, 26)); // Nationalfeiertag
		addPublicHoliday(new FixedPublicHoliday(10,  1)); // Allerheiligen
		addPublicHoliday(new FixedPublicHoliday(11,  8)); // Mari� Empf�ngnis
//		addPublicHoliday(new FixedPublicHoliday(11, 24)); // Hl. Abend
		addPublicHoliday(new FixedPublicHoliday(11, 25)); // Weihnachten
		addPublicHoliday(new FixedPublicHoliday(11, 26)); // Stefani
//		addPublicHoliday(new FixedPublicHoliday(11, 31)); // Silvester

//		addPublicHoliday(new FloatingPublicHoliday(-2)); // Karfreitag
//		addPublicHoliday(new FloatingPublicHoliday(-1)); // Karsamstag
		addPublicHoliday(new FloatingPublicHoliday( 0)); // Ostersonntag
		addPublicHoliday(new FloatingPublicHoliday( 1)); // Ostermontag
		addPublicHoliday(new FloatingPublicHoliday(39)); // Christi Himmelfahrt
		addPublicHoliday(new FloatingPublicHoliday(49)); // Pfingstsonntag
		addPublicHoliday(new FloatingPublicHoliday(50)); // Pfingstmontag
		addPublicHoliday(new FloatingPublicHoliday(60)); // Fronleichnam
	}

	public static void addPublicHoliday(PublicHoliday publicHoliday) {
		publicHolidays.add(publicHoliday);
	}

	public static TypeOfDay typeOfDay(Calendar calendar) {
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			return TypeOfDay.SATURDAY;
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			return TypeOfDay.SUNDAY;
		for (PublicHoliday publicHoliday : publicHolidays) {
			if (publicHoliday.isPublicHoliday(calendar))
				return TypeOfDay.PUBLIC_HOLIDAY;
		}
		return TypeOfDay.WORKING_DAY;
	}

	public static Calendar easterSunday(int year) {
		int g = year % 19;

		int c = year / 100;
		int h = (c - c / 4 - ((8 * c + 13) / 25) + 19 * g + 15) % 30;
		int i = h - (h / 28) * (1 - (h / 28) * (29 / (h + 1)) * ((21 - g) / 11));
		int j = (year + (year / 4) + i + 2 - c + c / 4) % 7;

		int l = i - j;
		int month = 3 + ((l + 40) / 44);
		int day = l + 28 - 31 * (month / 4);

		return new GregorianCalendar(year, month - 1, day);
	}

	public static void main(String[] args) {
		for (int i = 1; i < 32; i++) {
			Calendar calendar = new GregorianCalendar(2012, 4, i);
			System.out.println(new SimpleDateFormat("dd.MM.yyyy ").format(calendar.getTime()) + typeOfDay(calendar));
		}
	}

}
