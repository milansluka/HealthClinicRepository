package ehc.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {

	public static final String CONFIG_SYS_NAME = "config.sys";
	public static final String CONFIG_SYS_DEFAULT = "config.txt";

	// TODO 2012-08-16 C.Schrammel: configurable HIBERNATE_ALWAYS_BEGIN_TRANSACTION_IMPLICITLY is only for transition; remove eventually
	public static final String HIBERNATE_ALWAYS_BEGIN_TRANSACTION_IMPLICITLY = "hibernate-util.always.begin.transaction.implicitly";

	// general web proxy configuration
	public static final String HTTP_PROXY_HOST = "webproxy.http.proxyHost";
	public static final String HTTP_PROXY_PORT = "webproxy.http.proxyPort";

	// Linker (IQA Business Relationship AT)
	public static final String LINKER_SQL_RETRY_PREFIX = "linker.sqlretry";

	private static final String HIBERNATE_PREFIX = "hibernate";

	// Hibernate (IQA Service Persistence Default AT)
	public static final String HIBERNATE_CONFIG_PREFIX = HIBERNATE_PREFIX + ".conf";

	// FQCN Hibernate Session-Interceptor
	public static final String HIBERNATE_INTERCEPTOR = HIBERNATE_PREFIX + ".interceptor";

	// FQCN Hibernate Sessioninitializer
	public static final String HIBERNATE_SESSION_INITIALIZER = HIBERNATE_PREFIX + ".session.initializer";

	// enable Hibernate Statistics
	public static final String HIBERNATE_STATISTICS = HIBERNATE_PREFIX + ".statistics";

	private static final String MBEAN_PREFIX = "mbean";

	// register Hibernate MBean
	public static final String HIBERNATE_MBEAN = MBEAN_PREFIX + "." + HIBERNATE_PREFIX;

	private static final Map<String, Boolean> BOOLEANS = new HashMap<String, Boolean>();
	static {
		BOOLEANS.put("0", false);
		BOOLEANS.put("false", false);
		BOOLEANS.put("off", false);
		BOOLEANS.put("no", false);
		BOOLEANS.put("1", true);
		BOOLEANS.put("true", true);
		BOOLEANS.put("on", true);
		BOOLEANS.put("yes", true);
	}

	// -- all properties --------------------------------------------------------------------------

	public static Properties getProperties() {
		return System.getProperties();
	}

	// -- String (default) property -------------------------------------------------------------------------

	public static boolean hasProperty(String key) {
		return System.getProperties().containsKey(key);
	}

	public static String getProperty(String key) {
		return System.getProperty(key);
	}

	public static String getProperty(String key, String def) {
		return System.getProperty(key, def);
	}

	public static void setProperty(String key, String value) {
		System.setProperty(key, value);
	}

	public static void clearProperty(String key) {
		System.clearProperty(key);
	}

	// -- boolean property ------------------------------------------------------------------------

	public static boolean getBooleanProperty(String key) {
		String property = System.getProperty(key);
		if (property == null)
			throw new NumberFormatException("null");

		Boolean value = BOOLEANS.get(property);
		if (value != null)
			return value;

		throw new IllegalArgumentException("config key [" + key + "] does not contain a boolean value [" + property
			+ "]");
	}

	public static boolean getBooleanProperty(String key, boolean def) {
		String property = System.getProperty(key);
		if (property == null)
			return def;

		Boolean value = BOOLEANS.get(property);
		if (value != null)
			return value;

		throw new IllegalArgumentException("config key [" + key + "] does not contain a boolean value [" + property
			+ "]");
	}

	public static void setBooleanProperty(String key, boolean value) {
		System.setProperty(key, value ? "1" : "0");
	}

	// -- int property ----------------------------------------------------------------------------

	public static int getIntProperty(String key) {
		return Integer.parseInt(System.getProperty(key));
	}

	public static int getIntProperty(String key, int def) {
		String property = System.getProperty(key);
		if (property == null)
			return def;

		int value = Integer.parseInt(property);
		if (Integer.toString(value).equals(property))
			return value;

		throw new IllegalArgumentException("config key [" + key + "] does not contain an integer value [" + property
			+ "]");
	}

	public static void setIntProperty(String key, int value) {
		System.setProperty(key, Integer.toString(value));
	}

	// -- long property ---------------------------------------------------------------------------

	public static long getLongProperty(String key) {
		return Long.parseLong(System.getProperty(key));
	}

	public static long getLongProperty(String key, long def) {
		String property = System.getProperty(key);
		if (property == null)
			return def;

		long value = Long.parseLong(property);
		if (Long.toString(value).equals(property))
			return value;

		throw new IllegalArgumentException("config key [" + key + "] does not contain a long value [" + property + "]");
	}

	public static void setLongProperty(String key, long value) {
		System.setProperty(key, Long.toString(value));
	}
	
	private static Properties readProperties(String fileName, Object initiator) throws IOException{
		Properties props = new Properties();
		//		InputStream inputStream = initiator.getClass().getResourceAsStream(fileName);
		FileInputStream inputStream =  new FileInputStream(fileName);
//		if (inputStream == null)
//			throw new IOException("config file: " + fileName + "not found");
		props.load(inputStream);
		return props;
	}

	public static void boot(Object initiator ) throws IOException{
		boot( initiator, getProperty(CONFIG_SYS_NAME, CONFIG_SYS_DEFAULT));
	}
	
	public static void boot(Object initiator , String configFilePath) throws IOException{
		Properties fileProps = readProperties(configFilePath, initiator);
		getProperties().putAll(fileProps);
	}
}
