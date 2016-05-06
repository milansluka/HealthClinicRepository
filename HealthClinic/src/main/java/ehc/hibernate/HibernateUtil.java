package ehc.hibernate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.stat.Statistics;

import ehc.util.Config;

public class HibernateUtil {

	private static final Log LOGGER = LogFactory.getLog(HibernateUtil.class);

	private static final String HIBERNATE_DEFAULT_CONFIG_FILE = "hibernate.cfg.xml";
	private static final String DELIMITER = ";";

	private static SessionFactory sessionFactory;
	private static ISessionInitializer sessionInitializer;

	private static final ThreadLocal<Boolean> READONLY = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};

	public static void setReadOnly(boolean readOnly) {
		READONLY.set(readOnly);
	}

	public static boolean isReadOnly() {
		return READONLY.get();
	}

	// ********************************************************************************************
	// ** current session
	// ********************************************************************************************

	public static Session getCurrentSession() {
		Session session = getSessionFactory().getCurrentSession();
		if (sessionInitializer != null) {
			sessionInitializer.init(session);
		}
		return session;
	}

	public static Session getCurrentSessionWithTransaction() {
		Session currentSession = getCurrentSession();

		// TODO 2012-08-16 Claus: configurable
		// HIBERNATE_ALWAYS_BEGIN_TRANSACTION_IMPLICITLY is only for transition;
		// remove eventually
		if (!(currentSession.getTransaction().getStatus() == TransactionStatus.ACTIVE)
				&& (HibernateTemplate.hasActiveTransactionGuard()
						|| Config.getBooleanProperty(Config.HIBERNATE_ALWAYS_BEGIN_TRANSACTION_IMPLICITLY, false))) {
			currentSession.beginTransaction();
		}

		return currentSession;
	}

	public static long getNumberOfOpenSessions() {
		if (!isSessionStatisticsEnabled()) {
			return 0;
		}
		Statistics statistics = getSessionFactory().getStatistics();
		return statistics == null ? 0 : statistics.getSessionOpenCount() - statistics.getSessionCloseCount();
	}

	public static boolean isSessionStatisticsEnabled() {
		Statistics statistics = getSessionFactory().getStatistics();
		return statistics != null && statistics.isStatisticsEnabled();
	}

	// ********************************************************************************************
	// ** transaction management
	// ********************************************************************************************

	public static boolean hasActiveTransaction() {
		return sessionFactory != null
				&& (sessionFactory.getCurrentSession().getTransaction().getStatus() == TransactionStatus.ACTIVE);
	}

	public static void beginTransaction() {
		getCurrentSession().beginTransaction();
	}

	public static void flush() {
		getCurrentSession().flush();
	}

	public static void commitTransaction() {
		getCurrentSession().getTransaction().commit();
	}

	public static void rollbackTransaction() {
		getCurrentSession().getTransaction().rollback();
	}

	public static void rollbackTransactionAndCatchAllExceptions() {
		try {
			getCurrentSession().getTransaction().rollback();
		} catch (Exception e1) {
			LOGGER.warn("caught Exception when rolling back a transaction", e1);

			// when there was an Exception on the line, then it is necessary to
			// close
			// the session to prevent subsequent errors.
			try {
				getCurrentSession().close();
			} catch (Exception e2) {
				LOGGER.warn("caught Exception when closing a connection", e2);
			}
		}
	}

	public static void rollbackTransactionAndCloseSessionAndCatchAllExceptions() {
		try {
			getCurrentSession().getTransaction().rollback();
		} catch (Exception e1) {
			LOGGER.warn("caught Exception when rolling back a transaction", e1);
		} finally {
			try {
				getCurrentSession().close();
			} catch (Exception e2) {
				LOGGER.warn("caught Exception when closing a connection", e2);
			}
		}
	}

	public static void closeCurrentSession() {
		if (sessionFactory != null)
			sessionFactory.getCurrentSession().close();
	}

	// ********************************************************************************************
	// ** data manipulation
	// ********************************************************************************************

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<? extends T> clazz, Serializable id) {
		return (T) getCurrentSessionWithTransaction().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(String entityName, Serializable id) {
		return (T) getCurrentSessionWithTransaction().get(entityName, id);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<? extends T> clazz, Serializable id, LockMode lockMode) {
		return (T) getCurrentSessionWithTransaction().get(clazz, id, lockMode);
	}

	public static Serializable save(Object object) {
		return getCurrentSessionWithTransaction().save(object);
	}

	public static void saveOrUpdate(String entityName, Object object) {
		getCurrentSessionWithTransaction().saveOrUpdate(entityName, object);
	}

	public static void saveOrUpdate(Object object) {
		getCurrentSessionWithTransaction().saveOrUpdate(object);
	}

	public static Serializable save(String entityName, Object object) {
		return getCurrentSessionWithTransaction().save(entityName, object);
	}

	public static void update(Object object) {
		getCurrentSessionWithTransaction().update(object);
	}

	public static Object merge(Object object) {
		return getCurrentSessionWithTransaction().merge(object);
	}

	public static void refresh(Object object) {
		getCurrentSessionWithTransaction().refresh(object);
	}

	public static void delete(Object object) {
		getCurrentSessionWithTransaction().delete(object);
	}

	public static void evict(Object object) {
		// ATTENTION: evict does not necessarily need an open transaction
		// see also: GenericDaoHibernate.save(final String entityName, final T
		// entity, final boolean historic)
		getCurrentSession().evict(object);
	}

	// ********************************************************************************************
	// ** queries
	// ********************************************************************************************

	public static Query createQuery(String queryString) {
		return getCurrentSessionWithTransaction().createQuery(queryString);
	}

	public static Query getNamedQuery(String queryName) {
		return getCurrentSessionWithTransaction().getNamedQuery(queryName);
	}

	public static SQLQuery createSQLQuery(String queryString) {
		return getCurrentSessionWithTransaction().createSQLQuery(queryString);
	}

	public static Criteria createCriteria(Class<?> persistentClass) {
		return getCurrentSessionWithTransaction().createCriteria(persistentClass);
	}

	public static Criteria createCriteria(Class<?> persistentClass, String alias) {
		return getCurrentSessionWithTransaction().createCriteria(persistentClass, alias);
	}

	public static Filter enableFilter(String filterName) {
		return getCurrentSessionWithTransaction().enableFilter(filterName);
	}

	// ********************************************************************************************
	// ** initialization
	// ********************************************************************************************

	/**
	 * Creates a SessionFactory on the fly if it has not been created yet.
	 * 
	 * @see initialize()
	 * @return a SessionFactory instance.
	 */
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			initialize();
		}
		return sessionFactory;
	}

	/**
	 * Creates a SessionFactory if it has not been created yet. All Hibernate
	 * configuration files specified by system properties of the form
	 * <code>hibernate-conf.{identifier}</code> are processed. Finally, the
	 * default configuration file <code>hibernate.cfg.xml</code> is processed if
	 * found.
	 */
	public static synchronized void initialize() {
		if (sessionFactory == null) {
			List<String> configurationFiles = findConfigurations();
			configurationFiles.add(HIBERNATE_DEFAULT_CONFIG_FILE);
			initialize(configurationFiles);
		}
	}

	public static synchronized void initialize(String configurationFile) {
		if (sessionFactory == null && configurationFile != null) {
			initialize(Arrays.asList(configurationFile));
		}
	}

	public static synchronized void initialize(List<String> configurationFiles) {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();
				for (String configFile : configurationFiles) {
					loadConfiguration(configuration, configFile);
				}

				// session interceptor
				Interceptor interceptor = loadSessionInterceptor();
				if (interceptor != null) {
					configuration.setInterceptor(interceptor);
				}
				sessionFactory = configuration.buildSessionFactory();
				// Hibernate MBean
				// registerHibernateMBean(sessionFactory);

				// session statistics
				// boolean sessionStatisticsEnabled =
				// Config.getBooleanProperty(Config.HIBERNATE_STATISTICS,
				// false);
				// LOGGER.info("using hibernate session statistics: {}",
				// sessionStatisticsEnabled);
				// if (sessionStatisticsEnabled) {
				// Statistics statistics = sessionFactory.getStatistics();
				// statistics.setStatisticsEnabled(sessionStatisticsEnabled);
				// }

				// session initializer
				sessionInitializer = loadSessionInitializer();
			} catch (HibernateException e) {
				throw new ExceptionInInitializerError(e);
			}
		}
	}

	private static synchronized void loadConfiguration(Configuration configuration, String resourceName) {
		URL resourceUrl = HibernateUtil.class.getClassLoader().getResource(resourceName);
		if (resourceUrl != null) {
			LOGGER.info("Processing configuration {} for URL {}" + resourceName + (resourceUrl.toExternalForm()));
			configuration.configure(resourceName);
			return;
		}

		File f = new File(resourceName);
		if (f.exists() && f.isFile()) {
			LOGGER.info("Processing configuration {} for file {}" + resourceName + f.getAbsolutePath());
			configuration.configure(f);
			return;
		}

		LOGGER.warn("No configuration {} found." + resourceName);
	}

	private static List<String> findConfigurations() {
		List<String> configFiles = new ArrayList<String>();
		for (Entry<Object, Object> prop : Config.getProperties().entrySet()) {
			if (((String) prop.getKey()).startsWith(Config.HIBERNATE_CONFIG_PREFIX)) {
				for (String config : ((String) prop.getValue()).split(DELIMITER)) {
					config = org.apache.commons.lang.StringUtils.trimToNull(config);
					if (config != null) {
						configFiles.add(config);
					}
				}
			}
		}
		return configFiles;
	}

	private static Interceptor loadSessionInterceptor() {
		String className = StringUtils.trim(Config.getProperty(Config.HIBERNATE_INTERCEPTOR));
		if (className == null) {
			return null;
		}

		Object interceptor = null;
		try {
			interceptor = Class.forName(className).newInstance();
		} catch (Exception e) {
			ExceptionInInitializerError pe = new ExceptionInInitializerError(
					"error creating hibernate interceptor " + className);
			LOGGER.warn(pe.getMessage(), pe);
			throw pe;
		}
		if (Interceptor.class.isAssignableFrom(interceptor.getClass())) {
			LOGGER.info("using hibernate interceptor {}" + className);
			return (Interceptor) interceptor;
		}

		ExceptionInInitializerError pe = new ExceptionInInitializerError(
				"error creating hibernate interceptor " + className);
		LOGGER.warn(pe.getMessage(), pe);
		throw pe;
	}

	private static ISessionInitializer loadSessionInitializer() {
		String className = StringUtils.trim(Config.getProperty(Config.HIBERNATE_SESSION_INITIALIZER));
		if (className == null) {
			return null;
		}

		Object interceptor = null;
		try {
			interceptor = Class.forName(className).newInstance();
		} catch (Exception e) {
			ExceptionInInitializerError pe = new ExceptionInInitializerError(
					"error creating hibernate session initializer " + className);
			LOGGER.warn(pe.getMessage(), pe);
			throw pe;
		}

		if (ISessionInitializer.class.isAssignableFrom(interceptor.getClass())) {
			LOGGER.info("using hibernate session initializer {}" + className);
			return (ISessionInitializer) interceptor;
		}

		ExceptionInInitializerError pe = new ExceptionInInitializerError(
				"error creating hibernate session initializer " + className);
		LOGGER.warn(pe.getMessage(), pe);
		throw pe;
	}

	// TODO: if we find a maven repository for hibernate-jconsole then uncomment
	// it
	// private static void registerHibernateMBean(SessionFactory sessionFactory)
	// {
	// boolean register = Config.getBooleanProperty(Config.HIBERNATE_MBEAN,
	// false);
	// if (sessionFactory != null && register) {
	// try {
	// MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
	// final StatisticsService mBean = new StatisticsService();
	// mBean.setStatisticsEnabled(true);
	// HibernateJmxBinding jmxBinding = new HibernateJmxBinding(mbeanServer,
	// sessionFactory);
	// jmxBinding.registerJmxBinding();
	// }
	// catch (Throwable t) {
	// LOGGER.error("error creating hibernate mbean", t);
	// }
	// }
	// }
	public static Clob readFileToClob(String fileName) throws FileNotFoundException, IOException {
		return readFileToClob(new File(fileName));
	}

	public static Clob readFileToClob(File file) throws FileNotFoundException, IOException {

		String clobData = readFileToString(file);
		return getCurrentSession().getLobHelper().createClob(clobData);
	}

	public static String readFileToString(File file) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String nextLine = "";
		StringBuffer sb = new StringBuffer();
		while ((nextLine = br.readLine()) != null) {
			sb.append(nextLine);
		}
		// Convert the content into to a string
		String clobData = sb.toString();
		br.close();
		// Return the data.
		return clobData;
	}

	public static Blob createBlob(byte[] bytes) {
		return getCurrentSession().getLobHelper().createBlob(bytes);
	}
}
