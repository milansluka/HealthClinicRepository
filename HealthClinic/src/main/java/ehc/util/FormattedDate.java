package ehc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Formats the given <code>Date</code> in the <code>toString()</code> method. Designed for use with
 * a SLF4J logger to delay actual formatting so that expensive formatting only happens when the
 * date is actually logged:
 * <pre>
 *     LOGGER.debug("some date: {}", new FormattedDate(date));
 * </pre>
 */
public class FormattedDate {

	private String pattern = "dd.MM.yyyy";
	private Date date;

	public FormattedDate(Date date) {
		this.date = date;
	}

	public FormattedDate(String pattern, Date date) {
		this.pattern = pattern;
		this.date = date;
	}

	@Override
	public String toString() {
		if (date == null || pattern == null)
			return null;
		return new SimpleDateFormat(pattern).format(date);
	}

}
