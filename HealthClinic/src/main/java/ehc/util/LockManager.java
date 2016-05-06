package ehc.util;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.logging.LogFactory;

public class LockManager {

	protected HashMap<Integer, HashSet<Object>> locksPerCustomer = new HashMap<Integer, HashSet<Object>>();

	/**
	 * Note: If a thread tries to lock again an object it is currently locking, it will end up in an infinite loop
	 */
	public void lock(int customerId, Object reference) {
		HashSet<Object> locks = getLocks(customerId);
		synchronized (locks) {
			while (locks.contains(reference)) {
				try {
					locks.wait();
				}
				catch (InterruptedException ex) {
					LogFactory.getLog(this.getClass()).error(ex);
				}
			}
			locks.add(reference);
			locks.notifyAll();
		}
	}

	private synchronized HashSet<Object> getLocks(int customerId) {
		Integer customerHashId = new Integer(customerId);
		HashSet<Object> locks = locksPerCustomer.get(customerHashId);
		if (locks == null) {
			locks = new HashSet<Object>();
			locksPerCustomer.put(customerHashId, locks);
		}
		return locks;
	}

	public void unlock(int customerId, Object reference) {
		HashSet<Object> locks = getLocks(customerId);
		synchronized (locks) {
			if (!locks.remove(reference))
				throw new IllegalStateException("Unlock: Specified lock does not exist. [" + customerId + "," + reference + "]");
			locks.notifyAll();
		}
	}
}
