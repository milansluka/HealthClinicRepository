package ehc.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormater {
	private DecimalFormat numberFormater;

	public NumberFormater(int decimalPlaces) {
		numberFormater = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
		numberFormater.setMinimumFractionDigits(decimalPlaces);
		numberFormater.setMaximumFractionDigits(decimalPlaces);
		numberFormater.setRoundingMode(RoundingMode.DOWN);
	}

	public String format(double number) {
		return numberFormater.format(number);
	}

	public String format(long number) {
		return numberFormater.format(number);
	}

	public String format(BigDecimal number) {
		return numberFormater.format(number);
	}
}
