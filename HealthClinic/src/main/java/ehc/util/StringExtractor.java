package ehc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringExtractor {

	public static String getStringBetweenMarkers(String searchedString,
			String startMarker, String endMarker) {

		Pattern pattern;
		if (endMarker != null)
			pattern = Pattern.compile(startMarker + "(.*?)" + endMarker);
		else
			pattern = Pattern.compile(startMarker + "(.*?)");

		Matcher matcher = pattern.matcher(searchedString);
		while (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	// NR 1 FM0006 M-NR 00210 5035 BEITRAG BIS 39/14 22,00 EINZUG REHA FIT
	// MANGOLD
	// BEITRAG BIS 39/14 22,00
	public static String getStringBetweenMarkersLeftInclusive(
			String searchedString, String startMarker, String endMarker) {

		String betweenMarkers = getStringBetweenMarkers(searchedString,
				startMarker, endMarker);
		if (betweenMarkers == null)
			return null;
		else
			return startMarker + betweenMarkers;
	}
	
	// NR 1 FM0006 M-NR 00210 5035 BEITRAG BIS 39/14 22,00 EINZUG REHA FIT
		// MANGOLD
		// BEITRAG BIS 39/14 22,00
	public static String getStringBetweenMarkersRightInclusive(
			String searchedString, String startMarker, String endMarker) {
		String endPart = null;
		String betweenMarkers = getStringBetweenMarkers(searchedString,
				startMarker, endMarker);

		Pattern pattern = Pattern
				.compile(endMarker);
		Matcher matcher = pattern.matcher(searchedString);
		while (matcher.find()) {
			endPart = matcher.group(0);
		}

		if (betweenMarkers == null)
			return null;
		else
			return betweenMarkers + endPart;
	}

	public static String getStringAfterRegexMarker(String searchedString,
			String regexMarker) {

		Pattern pattern = Pattern.compile(regexMarker);
		Matcher matcher = pattern.matcher(searchedString);
		while (matcher.find()) {
			return searchedString.substring(matcher.end(),
					searchedString.length());
		}
		return null;
	}

	public static String removeLastDecimal(String searchedString) {

		if (searchedString == null)
			return null;

		Pattern pattern = Pattern.compile("[0-9]+([,.][0-9]{1,2})?");
		Matcher matcher = pattern.matcher(searchedString);
		String answer = null;
		int endIndex = -1;
		while (matcher.find()) {
			answer = matcher.group(0);
			endIndex = matcher.start();
		}
		if (endIndex < 0)
			return searchedString;
		answer = searchedString.substring(0, endIndex);
		return answer;
	}

	public static String extractPeriod(String searchedString) {

		if (searchedString == null)
			return null;
		Pattern pattern = Pattern
				.compile("(([0-9][0-9]\\.[0-9][0-9])|([0-9][0-9]/[0-9][0-9]))");
		Matcher matcher = pattern.matcher(searchedString);
		String found = null;
		while (matcher.find()) {
			found = matcher.group(0);
		}
		return found;
	}

	public static String extractIban(String searchedString) {

		if (searchedString == null)
			return null;
		Pattern pattern = Pattern
				.compile("[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}");
		Matcher matcher = pattern.matcher(searchedString);
		String found = null;
		while (matcher.find()) {
			found = matcher.group(0);
		}
		return found;
	}
	
	public static String extractMatchedRegex(String searchedString,String regex) {

		if (searchedString == null)
			return null;
		Pattern pattern = Pattern
				.compile(regex);
		Matcher matcher = pattern.matcher(searchedString);
		String found = null;
		while (matcher.find()) {
			found = matcher.group(0);
		}
		return found;
	}
}
