package ehc.util;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class BigDecimalConverter {
	public static BigDecimal stringToBigDecimal(String formattedString, Locale locale) {
		DecimalFormatSymbols symbols;
		char notCorrectDecimalSeparatorChar;
		String notCorrectDecimalSeparator;
		char correctDecimalSeparatorChar;
		String correctDecimalSeparator;
		String fixedString;
		BigDecimal number;

		symbols = new DecimalFormatSymbols(locale);
		correctDecimalSeparatorChar = symbols.getDecimalSeparator();
		notCorrectDecimalSeparatorChar = (".".toCharArray()[0] == correctDecimalSeparatorChar ? ",".toCharArray()[0] : ".".toCharArray()[0]);

		if (correctDecimalSeparatorChar == '.') {
			correctDecimalSeparator = "\\" + correctDecimalSeparatorChar;
		} else {
			correctDecimalSeparator = Character.toString(correctDecimalSeparatorChar);
		}

		if (notCorrectDecimalSeparatorChar == '.') {
			notCorrectDecimalSeparator = "\\" + notCorrectDecimalSeparatorChar;
		} else {
			notCorrectDecimalSeparator = Character.toString(notCorrectDecimalSeparatorChar);
		}
		if (correctDecimalSeparatorChar == '.') {
			fixedString = formattedString.replaceAll(notCorrectDecimalSeparator, Character.toString(correctDecimalSeparatorChar));
		}
		else {
			fixedString = formattedString.replaceAll(correctDecimalSeparator, Character.toString(notCorrectDecimalSeparatorChar));
		}
		number = new BigDecimal(fixedString);

		return (number);
	}
}
