package ehc.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormater {
	private DecimalFormat currencyFormater;

	public CurrencyFormater() {
		currencyFormater = (DecimalFormat) NumberFormat
				.getCurrencyInstance(Locale.GERMANY);
		currencyFormater.setMinimumFractionDigits(2);
		currencyFormater.setMaximumFractionDigits(2);
	}

	public String format(double number) {
		return currencyFormater.format(number);
	}

	public String format(long number) throws Exception {
		throw new Exception(
				"!!! PROBLEM WITH CURRENCY FORMATING STILL NOT SOLVED !!!");
	}

	public Object format(BigDecimal number) {
		return currencyFormater.format(number);
	}
}
