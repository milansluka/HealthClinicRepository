package ehc.hibernate;

import org.hibernate.Session;

/**
 * @author w.sachse
 */
public interface ISessionInitializer {
	public void init(Session session);
}
