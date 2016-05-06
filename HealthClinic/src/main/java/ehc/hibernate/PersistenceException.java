package ehc.hibernate;

/**
 * {@code PersistenceException} should be used to encapsulate
 * exceptions of specific persistence frameworks occurring during
 * persistence operations by catching the specific exception and
 * re-throwing this exception in the catch block.
 */
public class PersistenceException extends RuntimeException {

	public PersistenceException() {
		super();
	}

	public PersistenceException(String message) {
		super(message);
	}

	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceException(Throwable cause) {
		super(cause);
	}

}
