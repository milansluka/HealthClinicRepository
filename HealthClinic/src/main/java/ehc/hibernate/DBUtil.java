package ehc.hibernate;

public class DBUtil {

	public static boolean isRestoreException(Throwable e) {
		if (e == null)
			return false;

		if (e.getMessage() != null && (e.getMessage().endsWith("' cannot be opened. It is in the middle of a restore.") ||
		                               e.getMessage().endsWith("' is being recovered. Waiting until recovery is finished.")))
			return true;

		return isRestoreException(e.getCause());
	}

}
