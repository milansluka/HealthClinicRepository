package ehc.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ToStringBuilder {

	protected StringBuilder stringBuilder;
	protected boolean skipNulls = true;
	protected boolean appendComma = false;

	public ToStringBuilder(String name, Object object) {
		this.stringBuilder = new StringBuilder();
		stringBuilder.append(name).append("@").append(Integer.toHexString(System.identityHashCode(object))).append(" (");
	}

	public ToStringBuilder skipNulls(boolean skipNulls) {
		this.skipNulls = skipNulls;
		return this;
	}

	@Override
	public String toString() {
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	// *** Integer ********************************************************************************

	public ToStringBuilder append(String name, Integer value) {
		return append(name, value, skipNulls);
	}

	public ToStringBuilder appendNull(String name, Integer value) {
		return append(name, value, false);
	}

	private ToStringBuilder append(String name, Integer value, boolean skipNull) {
		if (skipNull && value == null)
			return this;
		if (appendComma)
			stringBuilder.append(", ");
		stringBuilder.append(name).append("=").append(value);
		appendComma = true;
		return this;
	}

	// *** Boolean ********************************************************************************

	public ToStringBuilder append(String name, Boolean value) {
		return append(name, value, skipNulls);
	}

	public ToStringBuilder appendNull(String name, Boolean value) {
		return append(name, value, false);
	}

	private ToStringBuilder append(String name, Boolean value, boolean skipNull) {
		if (skipNull && value == null)
			return this;
		if (appendComma)
			stringBuilder.append(", ");
		stringBuilder.append(name).append("=").append(value);
		appendComma = true;
		return this;
	}

	// *** Double *********************************************************************************

	public ToStringBuilder append(String name, Double value) {
		return append(name, value, skipNulls);
	}

	public ToStringBuilder appendNull(String name, Double value) {
		return append(name, value, false);
	}

	private ToStringBuilder append(String name, Double value, boolean skipNull) {
		if (skipNull && value == null)
			return this;
		if (appendComma)
			stringBuilder.append(", ");
		stringBuilder.append(name).append("=").append(value);
		appendComma = true;
		return this;
	}

	// *** BigDecimal *****************************************************************************

	public ToStringBuilder append(String name, BigDecimal value) {
		return append(name, value, skipNulls);
	}

	public ToStringBuilder appendNull(String name, BigDecimal value) {
		return append(name, value, false);
	}

	private ToStringBuilder append(String name, BigDecimal value, boolean skipNull) {
		if (skipNull && value == null)
			return this;
		if (appendComma)
			stringBuilder.append(", ");
		stringBuilder.append(name).append("=").append(value);
		appendComma = true;
		return this;
	}

	// *** BigInteger *****************************************************************************

	public ToStringBuilder append(String name, BigInteger value) {
		return append(name, value, skipNulls);
	}

	public ToStringBuilder appendNull(String name, BigInteger value) {
		return append(name, value, false);
	}

	private ToStringBuilder append(String name, BigInteger value, boolean skipNull) {
		if (skipNull && value == null)
			return this;
		if (appendComma)
			stringBuilder.append(", ");
		stringBuilder.append(name).append("=").append(value);
		appendComma = true;
		return this;
	}

	// *** Object *********************************************************************************

	public ToStringBuilder append(String name, Object value) {
		return append(name, value, skipNulls);
	}

	public ToStringBuilder appendNull(String name, Object value) {
		return append(name, value, false);
	}

	private ToStringBuilder append(String name, Object value, boolean skipNull) {
		if (skipNull && value == null)
			return this;
		if (appendComma)
			stringBuilder.append(", ");
		if (value == null)
			stringBuilder.append(name).append("=null");
		else
			stringBuilder.append(name).append("=\"").append(value).append("\"");
		appendComma = true;
		return this;
	}

	// *** String *********************************************************************************

	public ToStringBuilder append(String name, String value) {
		return append(name, value, skipNulls);
	}

	public ToStringBuilder appendNull(String name, String value) {
		return append(name, value, false);
	}

	private ToStringBuilder append(String name, String value, boolean skipNull) {
		if (skipNull && value == null)
			return this;
		if (appendComma)
			stringBuilder.append(", ");
		if (value == null)
			stringBuilder.append(name).append("=null");
		else
			stringBuilder.append(name).append("=\"").append(value).append("\"");
		appendComma = true;
		return this;
	}

	// *** Date ***********************************************************************************

	public ToStringBuilder append(String name, Date value) {
		return append(name, value, skipNulls);
	}

	public ToStringBuilder appendNull(String name, Date value) {
		return append(name, value, false);
	}

	private ToStringBuilder append(String name, Date value, boolean skipNull) {
		return append(name, "yyyy-MM-dd HH:mm:ss", value, skipNull);
	}

	public ToStringBuilder append(String name, String dateFormat, Date value) {
		return append(name, dateFormat, value, skipNulls);
	}

	public ToStringBuilder appendNull(String name, String dateFormat, Date value) {
		return append(name, dateFormat, value, false);
	}

	private ToStringBuilder append(String name, String dateFormat, Date value, boolean skipNull) {
		if (skipNull && value == null)
			return this;
		if (appendComma)
			stringBuilder.append(", ");
		if (value == null)
			stringBuilder.append(name).append("=null");
		else
			stringBuilder.append(name).append("=\"").append(new SimpleDateFormat(dateFormat).format(value)).append("\"");
		appendComma = true;
		return this;
	}

	// *** List ***********************************************************************************

	public ToStringBuilder append(String name, List<?> values) {
		return append(name, values, skipNulls);
	}

	public ToStringBuilder appendNull(String name, List<?> values) {
		return append(name, values, false);
	}

	private ToStringBuilder append(String name, List<?> values, boolean skipNull) {
		if (skipNull && values == null)
			return this;
		if (appendComma)
			stringBuilder.append(", ");
		if (values == null)
			stringBuilder.append(name).append("=null");
		else {
			stringBuilder.append(name).append("=List<?>{");
			boolean comma = false;
			for (@SuppressWarnings("unused") Object value : values) {
				if (comma)
					stringBuilder.append(", ");
				stringBuilder.append("obj");
				comma = true;
			}
			stringBuilder.append("}");
		}

		appendComma = true;
		return this;
	}

}
