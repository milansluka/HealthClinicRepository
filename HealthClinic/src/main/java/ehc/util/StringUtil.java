package ehc.util;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public abstract class StringUtil {

    private static final int SEPA_REMITTANCE_INFORMATION_MAX_LENGTH = 140;
    private static HashMap<String, String> GERMAN_SPECIAL_CHAR_MAP;
	private final static String regexChars = ".+*?()[]^$";

 
    public static String sanitizeSepaString(String remittance) {
        if (remittance == null)
            return null;
        remittance = deAccentExceptGermanChar(remittance);
        remittance = deAccent(remittance);
        remittance = remittance.replaceAll("[^a-zA-Z0-9\\s':?,\\-(+.)/]", " ");
        return remittance;

    }

    public static String sanitizeSepaRemittance(String remittance) {
        if (remittance == null)
            return null;
        remittance = sanitizeSepaString(remittance);
        remittance = remittance.length() > SEPA_REMITTANCE_INFORMATION_MAX_LENGTH ? remittance.substring(0, 140) : remittance;
        return remittance;

    }


	/**
	 * ueberpr�ft, ob eine CharSequence ausschliesslich aus Grossbuchstaben
	 * besteht.
	 *
	 * @param s
	 *            CharSequence
	 * @return true, wenn nur Grossbuchstaben vorhanden sind, false sonst
	 */
	public static boolean isUpperCase(CharSequence s, String charsToIgnore) {
		if (s != null && s.length() > 0) {
			for (int i = 0; i < s.length(); i++) {
				if (charsToIgnore.indexOf(s.charAt(i)) < 0
						&& !Character.isUpperCase(s.charAt(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Korrigiert die Gross- Kleinschreibung.
	 *
	 * @param s
	 *            String mit gemischter Gross-Kleinschreibung
	 * @return Korrigierter String
	 */
	public static String adjustUpperLowerCase(String s) {
		if (s != null && s.length() > 0) {
			StringBuffer b = new StringBuffer(s.toLowerCase().trim());
			b.setCharAt(0, Character.toUpperCase(b.charAt(0)));
			for (int i = 0; i < b.length(); i++) {
				char c = b.charAt(i);
				if ((c == ' ' || c == '-') && i + 1 < b.length()) {
					b.setCharAt(i + 1, Character.toUpperCase(b.charAt(i + 1)));
				}
			}
			s = b.toString();
		}

		return s;
	}

	/**
	 * teilt einen Sting der durch Begrenzungszeichen getrennt ist, in einzelne
	 * Tokens auf
	 *
	 * @param s
	 *            String mit Begrenzungszeichen
	 * @param splitchar
	 *            Begrenzungszeichen
	 * @return Tokens
	 */
	public static final String[] split(String s, char splitchar) {
		if (s != null && s.length() > 0) {
			String result[] = s.trim().split("" + splitchar);
			for (int i = 0; i < result.length; i++) {
				result[i] = result[i].trim();
			}
			return result;
		}

		return new String[] {};
	}

	public final static String append(String first, String second, String delim) {
		StringBuffer buffer = new StringBuffer();
		addString(buffer, first, delim);
		addString(buffer, second, delim);
		return buffer.toString();
	}

	public final static String append(String first, String second) {
		return append(first, second, " ");
	}

	public final static String getNotNullString(String s, String replace) {
		return s == null ? replace : s;
	}

	public final static String getNotNullString(String s) {
		return getNotNullString(s, "");
	}

	private final static void addString(StringBuffer buffer, String s,
			String delim) {
		if (isNotEmpty(s)) {
			if (buffer.length() > 0) {
				buffer.append(delim);
			}
			buffer.append(s);
		}
	}

	public final static boolean isEmpty(String s) {
		return s == null || s.trim().equals("");
	}

	public final static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

	public final static String truncate(String s, int maxNumOfChars) {
		if (s == null)
			return null;
		if (s.length() <= maxNumOfChars)
			return s;
		return s.substring(0, maxNumOfChars);
	}

	public final static String removeNotNumericChars(String input) {
		if (input == null)
			return null;
		StringBuffer buf = new StringBuffer(input.length());
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (Character.isDigit(c)) {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	public final static String removeWhitespaceChars(String input) {
		if (input == null)
			return null;
		StringBuffer buf = new StringBuffer(input.length());
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (!Character.isWhitespace(c)) {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	public final static boolean isNumeric(String value) {
		if (value == null)
			return false;
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public final static boolean hasLetter(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}

		for (int i = 0; i < value.length(); i++) {
			if (Character.isLetter(value.charAt(i))) {
				return true;
			}
		}

		return false;
	}

	public static void appendIntCollectionAsCommaSeparatedList(
			Collection<Integer> integerCollection, StringBuffer buffer) {
		for (Iterator<Integer> iterator = integerCollection.iterator(); iterator
				.hasNext();) {
			buffer.append(iterator.next());
			if (iterator.hasNext())
				buffer.append(",");
		}
	}

	public static void appendIntArrayAsCommaSeparatedList(int[] someArray,
			StringBuffer buffer) {
		for (int i = 0; i < someArray.length; i++) {
			if (i != 0) {
				buffer.append(", ");
			}
			buffer.append(someArray[i]);
		}
	}

	public static void appendColumns(StringBuffer sql, String alias,
			String[] columnNames) {
		for (int i = 0; i < columnNames.length; i++) {
			if (i > 0)
				sql.append(",");
			if (alias != null) {
				sql.append(alias);
				sql.append('.');
			}
			sql.append(columnNames[i]);
		}
	}

	/**
	 * Joins the tokens with the given separator string.
	 *
	 * @param separator
	 *            the separator for joining
	 * @param tokens
	 *            the tokens to be joined
	 * @return the string of joined tokens
	 */
	public static String join(String separator, String... tokens) {
		if (tokens.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (String str : tokens) {
			sb.append(str).append(separator);
		}
		sb.setLength(sb.length() - separator.length());
		return sb.toString();
	}

	/**
	 * Joins Objects with the given separator to a string. The tokens used for
	 * concatenation are the strings returned by the {@code valueOf()} method of
	 * the objects.
	 *
	 * @param separator
	 *            the separator for joining
	 * @param prefix
	 *            the string prefixed to each token
	 * @param suffix
	 *            the string appended to each token
	 * @param tokens
	 *            the Object tokens to be joined
	 * @return the string of joined tokens
	 */
	public static String join(String separator, String prefix, String suffix,
			Object... tokens) {
		if (tokens.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (Object obj : tokens) {
			if (prefix != null)
				sb.append(prefix);
			sb.append(obj);
			if (suffix != null)
				sb.append(suffix);
			sb.append(separator);
		}
		sb.setLength(sb.length() - separator.length());
		return sb.toString();
	}

	/**
	 * Joins Objects with the given separator to a string. The tokens used for
	 * concatenation are the strings returned by the {@code valueOf()} method of
	 * the objects.
	 *
	 * @param separator
	 *            the separator for joining
	 * @param tokens
	 *            the Object tokens to be joined
	 * @return the string of joined tokens
	 */
	public static String join(String separator, Object... tokens) {
		return join(separator, null, null, tokens);
	}

	public static String join(String separator, int[] tokens) {
		List<String> intList = new ArrayList<String>();
		for (int i : tokens)
			intList.add(Integer.toString(i));
		return join(separator, intList);
	}

	/**
	 * Joins the tokens with the given separator string.
	 *
	 * @param separator
	 *            the separator for joining
	 * @param tokens
	 *            the tokens to be joined
	 * @return the string of joined tokens
	 */
	public static String join(String separator, List<String> tokens) {
		if (tokens.isEmpty())
			return "";
		return join(separator, tokens.toArray(new String[] {}));
	}

	/**
	 * Joins Objects with the given separator to a string. The tokens used for
	 * concatenation are the strings returned by the {@code valueOf()} method of
	 * the objects. All objects are surrounded by single quotes.
	 *
	 * @param separator
	 *            the separator for joining
	 * @param tokens
	 *            the Object tokens to be joined
	 * @return the string of joined tokens
	 */
	public static String quoteAndJoin(String separator, List tokens) {
		return join(separator, "'", "'", tokens.toArray());
	}

	/**
	 * Splits a given string using a given delimiter and trims all tokens.
	 *
	 * @param src
	 *            the string to be split
	 * @param delimiter
	 *            the delimiter used for splitting
	 * @return an array of trimmed tokens
	 */
	public static String[] trimsplit(String src, String delimiter) {
		String[] strArray = (src).split(delimiter);
		for (int i = 0; i < strArray.length; i++) {
			strArray[i] = strArray[i].trim();
		}
		return strArray;
	}

	public static String[] surround(String[] data, String pre, String post) {
		for (int i = 0; i < data.length; i++)
			data[i] = pre + data[i] + post;
		return data;
	}

	/**
	 * Capitalizes the given string.
	 *
	 * @param str
	 *            the string to be capitalize
	 * @return the capitalized string
	 */
	public static String capitalize(String str) {
		return str.substring(0, 1).toUpperCase().concat(str.substring(1));
	}

	/**
	 * Removes characters from the input string.
	 *
	 * @param input
	 *            the string from which the characters will be removed
	 * @param chars
	 *            the character list as {@code String}
	 * @return the input string without the specified characters
	 */
	public static String removeChars(String input, String chars) {
		for (String c : chars.split("")) {
			if (!c.equals(""))
				input = input.replaceAll(regexChars.contains(c) ? "\\" + c : c,
						"");
		}
		return input;
	}

	public static String deAccent(String str) {
		if (str == null)
			return null;
		String nfdNormalizedString = Normalizer.normalize(str,
				Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public static String deAccentExceptGermanChar(String str) {
		if (str == null)
			return null;
		String answer = str;

		for (Entry<String, String> germanEntry : getGermanSpecialCharMap()
				.entrySet()) {
			answer = answer.replaceAll(germanEntry.getKey(),
					germanEntry.getValue());
		}
		return answer;
	}

	public static String nullSafe(String value) {
		return value != null ? value : "";
	}

	/**
	 * Returns the first non-null expression of the given list.
	 *
	 * @param expressions
	 *            a list of String expressions.
	 * @return the first non-null expression.
	 */
	public static String getNonNull(String... expressions) {
		for (String expr : expressions)
			if (expr != null)
				return expr;
		return null;
	}

	public static String formatStringToSql(String string) {
		if (string == null)
			return "null";
		return "'" + string + "'";
	}

	public static String formatDateToSql(Date date) {
		if (date == null)
			return "null";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return "'" + format.format(date) + "'";
	}

	public static String formatDateToSqlWithTime(Date date) {
		if (date == null)
			return "null";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "'" + format.format(date) + "'";
	}

	public static final String formatNumber(String val) {
		if (val.contains("") && val.contains(","))
			val = val.replaceAll("\\.", "");
		val = val.replaceAll(",", "");
		return isNumeric(val) ? val : null;
	}

	protected static HashMap<String, String> getGermanSpecialCharMap() {
		if (GERMAN_SPECIAL_CHAR_MAP == null) {
			// {'ü', 'ö', 'ä', 'ß'}; be replaced by "ue", "oe", "ae" or "ss"
			// respectively.
			GERMAN_SPECIAL_CHAR_MAP = new HashMap<String, String>(7);
			GERMAN_SPECIAL_CHAR_MAP.put("ü", "ue");
			GERMAN_SPECIAL_CHAR_MAP.put("ö", "oe");
			GERMAN_SPECIAL_CHAR_MAP.put("ä", "ae");
			GERMAN_SPECIAL_CHAR_MAP.put("ß", "ss");
			GERMAN_SPECIAL_CHAR_MAP.put("Ü", "UE");
			GERMAN_SPECIAL_CHAR_MAP.put("Ö", "OE");
			GERMAN_SPECIAL_CHAR_MAP.put("Ä", "AE");

		}
		return GERMAN_SPECIAL_CHAR_MAP;
	}

	/**
	 * Removes multiple white spaces from the input string.
	 *
	 * @param input
	 *            the string from which the multiple white spaces will be
	 *            removed
	 * @return the input string without multiple white spaces
	 */
	public static String removeMultipleWhitespaces(String input) {
		// ^_+ : any sequence of spaces at the beginning of the string
		// _+$ : any sequence of spaces at the end of the string
		// (_)+ : any sequence of spaces that matches none of the above, meaning
		// it's in the middle
		// Match and replace with $1, which captures a single space

		return input.replaceAll("^ +| +$|( )+", "$1");
	}

	public static String[] getArrayOfNumbersSeparatedInString(String input) {
		String[] separators = { "\\,", "\\.", "\\|", "\\/" };
		String[] akteReferenceArray = null;

		if ((null != input) && !input.isEmpty()) {
			for (String separator : separators) {
				akteReferenceArray = null;
				akteReferenceArray = input.replaceAll("\\s+", "").split(
						separator);
				if ((null != akteReferenceArray)
						&& (1 < akteReferenceArray.length)) {
					break;
				}
			}

		}

		return akteReferenceArray;
	}

	public static boolean isAlphaNumeric(String s) {
		String pattern = "^[a-zA-Z0-9]*$";
		return s != null && s.matches(pattern);
	}

}
