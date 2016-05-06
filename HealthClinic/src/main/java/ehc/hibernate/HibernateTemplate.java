package ehc.hibernate;

import static ehc.hibernate.DBUtil.isRestoreException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Base class to run code with a transaction guard. In the simplest case, the transaction guard closes
 * (commits) the transaction that was opened implicitly while executing the code in the <code>run()</code>
 * method. This is the basic contract: everything within <code>run()</code> is executed with a single
 * transaction which is cleaned (commited) at the end:
 * <pre>
 * new HibernateTemplate<Void>() {
 *     {@code @Override}
 *     public void run() {
 *         // do something here
 *     }
 * }.runWithTransactionGuard();
 * </pre>
 * In the more complex cases the transaction guards are nested within each other. This means, code that
 * runs with a transaction guard creates a new instance of HibernateTemplate and runs some other code
 * with another transaction guard.
 * <p>
 * The default behavior for opening transactions is not to open transactions explicitly but let the first
 * access of HibernateUtil open a new transaction implicitly. With <code>beginTransaction()</code> a
 * new transaction is opened explicitly before executing <code>run()</code> if there is no open
 * transaction already.
 * </p><p>
 * The default behavior for closing transactions is to close (commit) the transaction in the outermost
 * transaction guard. With <code>closeTransaction()</code> the transaction is closed after executing
 * <code>run()</code> if there was no open transaction before; otherwise an Exception is thrown
 * (before executing <code>run()</code>).
 * </p><p>
 * There are also possibilities to control commit or rollback with appropriate methods. Because a commit
 * or rollback significantly impacts the transaction, this can only be done in the outermost instance of
 * <code>HibernateTemplate</code>. Unfortunately, some legacy code request rollback while executing
 * <code>run()</code>, therefore this can only be checked after execution (then rolling back and throwing
 * an Exception).
 * </p>
 * @param <T> parameterizes the return value set in <code>storeResult</code> returned by <code>execute</code>
 * @see HibernateUtil
 */
public abstract class HibernateTemplate<T> extends HibernateAccessor {

	private static final Log LOGGER = LogFactory.getLog(HibernateTemplate.class);

	@SuppressWarnings("unused")
	// debuggig
	private static final boolean DO_SESSION_COUNT = false && HibernateUtil.isSessionStatisticsEnabled();

	private static ThreadLocal<HibernateTemplate> CURRENT = new ThreadLocal<HibernateTemplate>();

	private static enum BeginTransaction {
		IMMEDIATE;
	}
	private static final BeginTransaction IMMEDIATE = BeginTransaction.IMMEDIATE;

	private static enum CloseTransaction {
		COMMIT,
		ROLLBACK;
	}
	private static final CloseTransaction COMMIT = CloseTransaction.COMMIT;
	private static final CloseTransaction ROLLBACK = CloseTransaction.ROLLBACK;

	private BeginTransaction beginTransaction = null;
	private CloseTransaction closeTransaction = null;

	private T result;

	/** subclasses implement this method with the code that should run within a transaction guard. */
	public abstract void run() throws Exception;

	/** implementation of run() may use that for passing back a return value. */
	protected void storeResult(T result) {
		this.result = result;
	}

	/** a transaction should be started if no transaction has been started yet. */
	public HibernateTemplate<T> beginTransaction() {
		this.beginTransaction = IMMEDIATE;
		return this;
	}

	/** commit the transaction after executing <code>run()</code>. */
	public HibernateTemplate<T> commitTransaction() {
		this.closeTransaction = COMMIT;
		return this;
	}

	/** commit (<code>true</code>) or rollback (<code>false</code>) the transaction after executing <code>run()</code>. */
	public HibernateTemplate<T> commitTransaction(boolean commitTransaction) {
		this.closeTransaction = commitTransaction ? COMMIT : ROLLBACK;
		return this;
	}

	/** rollback the transaction after executing <code>run()</code>. */
	public HibernateTemplate<T> rollbackTransaction() {
		this.closeTransaction = ROLLBACK;
		return this;
	}

	public static boolean hasActiveTransactionGuard() {
		return CURRENT.get() != null;
	}

	public T runWithTransactionGuard() throws PersistenceException {
		return runWithTransactionGuard(HibernateUtil.isReadOnly());
	}

	public T runWithTransactionGuard(boolean readOnly) throws PersistenceException {
		long sessionNumbersPreRun = 0;
		if (DO_SESSION_COUNT) {
			sessionNumbersPreRun = HibernateUtil.getNumberOfOpenSessions();
		}

		HibernateTemplate parent = CURRENT.get();
		CURRENT.set(this);
		boolean oldReadOnly = HibernateUtil.isReadOnly();
		HibernateUtil.setReadOnly(readOnly);
		try {
			boolean transactionAlreadyStartedOutside = HibernateUtil.hasActiveTransaction();
			if (!transactionAlreadyStartedOutside && beginTransaction == IMMEDIATE) {
				HibernateUtil.beginTransaction();
			}

			boolean shouldFinishTransaction = false;
			try {
				try {
					doRun();
				}
				finally {
					// run() may call shouldCommit(boolean), thus modifying closeTransaction
					shouldFinishTransaction = !transactionAlreadyStartedOutside && (closeTransaction != null || parent == null);
				}
			}
			catch (Exception e) {
				if (shouldFinishTransaction && HibernateUtil.hasActiveTransaction()) {
					HibernateUtil.rollbackTransactionAndCatchAllExceptions();
				}
				throw new PersistenceException("caught an Exception in run()", e);
			}

			if (closeTransaction != null && (transactionAlreadyStartedOutside || parent != null)) {
				if (shouldFinishTransaction)
					HibernateUtil.rollbackTransactionAndCatchAllExceptions();
				throw new PersistenceException("not allowed to close a transaction [" + closeTransaction + "] that was opened outside or when inside a nested call");
			}

			// Auch mit dem gesetzten Property HIBERNATE_ALWAYS_BEGIN_TRANSACTION_IMPLICITLY
			// gibt es nicht aktive Transaktionen, wo shouldFinishTransaction == true
			// In der Folge bleibt die Session und damit die DB-Verbindungen offen.
			//
			// if (shouldFinishTransaction && HibernateUtil.hasActiveTransaction()) {
			if (shouldFinishTransaction) {
				if (HibernateUtil.hasActiveTransaction()) {
					// commit is the default when neither commit nor rollback was
					// requested
					if (closeTransaction == ROLLBACK) {
						HibernateUtil.rollbackTransaction();
					}
					else {
						HibernateUtil.commitTransaction();
					}
				}
				else {
					try {
						HibernateUtil.closeCurrentSession();
					}
					catch (Exception e) {
						LOGGER.warn("error closing hibernate session", e);
					}
				}

			}
			return result;

		}
		finally {
			CURRENT.set(parent);
			HibernateUtil.setReadOnly(oldReadOnly);
			if (DO_SESSION_COUNT) {
				long sessionNumbersPostRun = HibernateUtil.getNumberOfOpenSessions();
				LOGGER.info("sessionCount pre: {}, post:{}" + sessionNumbersPreRun + sessionNumbersPostRun);
				if ((sessionNumbersPostRun - sessionNumbersPreRun) > 0) {
					LOGGER.warn("invalid session count, pre:{}, post{}" + sessionNumbersPreRun + sessionNumbersPostRun);
				}
			}
		}
	}

	private void doRun() throws Exception {
		// this method does not do anything but calling run() except when executing on the backup
		// database server: when the database is currently restored, then all queries against it fail.
		// Therefore wait 5 seconds and try again. Since it is the backup database server, only reads
		// are allowed (database is read-only), nothing can be corrupted, so re-run should be safe.
		try {
			run(); 
		}
		catch (Exception e) {
			if (isRestoreException(e)) {
				LOGGER.debug("caught restore exception; wait 5 seconds and re-run", e);
				try {
					Thread.sleep(5000);
				}
				catch (InterruptedException e1) {
					LOGGER.warn("caught InterruptedException", e1);
				}
				doRun();
				return;
			}
			throw e;
		}
	}

}
