package ehc.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * {@code GenericDaoHibernate} implements a generic DAO that encapsulates session and transaction
 * management, so that child DAO classes need not concern themselves with low level plumbing code.
 * <br/>
 * It is meant to be subclassed.
 */
public abstract class GenericDaoHibernate<T> {

	public Long save(final T entity) throws PersistenceException {

		return new HibernateTemplate<Long>() {
			@Override
			public void run() throws Exception {
				Long id = (Long) HibernateUtil.save(entity);
				storeResult(id);
			}
		}.runWithTransactionGuard();
	}

	public Long save(final String entityName, final T entity) throws PersistenceException {
		return save(entityName, entity, false);
	}

	public Long save(final String entityName, final T entity, final boolean historic) throws PersistenceException {
		// TODO please check if this is correct. Basically that code was replaced:
		//
		//	// insures that the history object actually gets written to the database
		//	if (historic) session.evict(entity);
		//	Integer id = (Integer) session.save(entityName, entity);
		//	if (owningTransaction) session.getTransaction().commit();
		//	// guarantees that all further persisted references in the transaction point to current objects
		//	// and not to historic objects
		//	if (historic) session.evict(entity);
		//	return id;
		//
		// The original code probably only works if owningTransaction is true, so the transaction
		// is committed before the (historic) entity is evicted. Therefore the new code with the
		// HibernateTemplate explicitly requests a commit (and will fail when inside a nested instance
		// of HibernateTemplate).

		// TODO that code does not work either because it is run nested within another HibernateTemplate
		// therefore the commit is refused
		//
		//	Integer id = new HibernateTemplate<Integer>() {
		//		@Override
		//		public void run() throws Exception {
		//			// insures that the history object actually gets written to the database
		//			if (historic) HibernateUtil.evict(entity);
		//			Integer id = (Integer) HibernateUtil.save(entityName, entity);
		//			storeResult(id);
		//		}
		//	}.beginTransaction().commitTransaction().runWithTransactionGuard();
		//	// actually this second HibernateTemplate is not much of use since the transaction is closed and evict
		//	// does not need an open one...
		//	new HibernateTemplate<Void>() {
		//		@Override
		//		public void run() throws Exception {
		//			// guarantees that all further persisted references in the transaction point to current objects
		//			// and not to historic objects
		//			if (historic) HibernateUtil.evict(entity);
		//		}
		//	}.runWithTransactionGuard();
		//	return id;

		// TODO it seems to work now.
		// At least it does not throw an Exception when nested. But it does not commit explicitly.
		// First, the original implementation did not commit either when it was not owning the transaction.
		// Second, this implementation does a commit when it is the outermost instance (i.e. not nested).
		// Third, there is small difference between those still. Let's hope the best.
		return new HibernateTemplate<Long>() {
			@Override
			public void run() throws Exception {
				// insures that the history object actually gets written to the database
				if (historic) HibernateUtil.evict(entity);
				Long id = (Long) HibernateUtil.save(entityName, entity);
				// guarantees that all further persisted references in the transaction point to current objects
				// and not to historic objects
				if (historic) HibernateUtil.evict(entity);
				storeResult(id);
			}
		}.runWithTransactionGuard();
	}

	public T update(final T entity) throws PersistenceException {
		return new HibernateTemplate<T>() {
			@Override
			public void run() throws Exception {
				try {
					HibernateUtil.update(entity);
				}
				catch (NonUniqueObjectException e) {
					HibernateUtil.merge(entity);
				}
				storeResult(entity);
			}
		}.runWithTransactionGuard();
	}
	
	public T saveOrUpdate(final T entity) throws PersistenceException {
		return new HibernateTemplate<T>() {
			@Override
			public void run() throws Exception {
				try {
					HibernateUtil.saveOrUpdate(entity);
				}
				catch (NonUniqueObjectException e) {
					HibernateUtil.merge(entity);
				}
				storeResult(entity);
			}
		}.runWithTransactionGuard();
	}

	public T delete(final T entity) throws PersistenceException {
		return new HibernateTemplate<T>() {
			@Override
			public void run() throws Exception {
				HibernateUtil.delete(entity);
				storeResult(entity);
			}
		}.runWithTransactionGuard();
	}

	public Integer execute(final String hql) throws PersistenceException {
		return new HibernateTemplate<Integer>() {
			@Override
			public void run() throws Exception {
				Query query = HibernateUtil.createQuery(hql);
				Integer modified = query.executeUpdate();
				storeResult(modified);
			}
		}.runWithTransactionGuard();
	}

	public T findById(final Class<T> clazz, final long id) throws PersistenceException {
		return new HibernateTemplate<T>() {
			@Override
			public void run() throws Exception {
				T entity = HibernateUtil.get(clazz, id);
				storeResult(entity);
			}
		}.runWithTransactionGuard();
	}
	
	public T findById(final Class<T> clazz, final int id) throws PersistenceException {
		return new HibernateTemplate<T>() {
			@Override
			public void run() throws Exception {
				T entity = HibernateUtil.get(clazz, id);
				storeResult(entity);
			}
		}.runWithTransactionGuard();
	}

	public T findById(final Class<T> clazz, final String id) throws PersistenceException {
		return new HibernateTemplate<T>() {
			@Override
			public void run() throws Exception {
				T entity = HibernateUtil.get(clazz, id);
				storeResult(entity);
			}
		}.runWithTransactionGuard();
	}

	public List<T> query(final Class<?> clazz, final List<Criterion> criterionList, final List<Order> orderList) throws PersistenceException {
		return query(clazz, null, null, criterionList, orderList, true);
	}

	public List<T> query(final Class<?> clazz, final List<Criterion> criterionList, final List<Order> orderList, final Integer maxResult) throws PersistenceException {
		return query(clazz, null, null, criterionList, orderList, maxResult, true);
	}

	public List<T> query(final Class<?> clazz, final String alias, final List<String[]> aliases, final List<Criterion> criterionList, final List<Order> orderList, final boolean distinct) throws PersistenceException {
		return query(clazz, alias, aliases, criterionList, orderList, null, distinct);
	}

	public List<T> query(final Class<?> clazz, final String alias, final List<String[]> aliases, final List<Criterion> criterionList, final List<Order> orderList, final Integer maxResult, final boolean distinct) throws PersistenceException {
		return new HibernateTemplate<List<T>>() {
			@Override
			public void run() throws Exception {
				Criteria criteria = alias == null ? HibernateUtil.createCriteria(clazz) : HibernateUtil.createCriteria(clazz, alias);
				if (aliases != null) {
					for (String[] a : aliases) {
						criteria.createAlias(a[0], a[1]);
					}
				}
				if (criterionList != null) {
					for (Criterion criterion : criterionList) {
						criteria.add(criterion);
					}
				}
				if (orderList != null) {
					for (Order order : orderList) {
						criteria.addOrder(order);
					}
				}
				if (maxResult != null)
					criteria.setMaxResults(maxResult);
				if (distinct)
					criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				@SuppressWarnings("unchecked")
				List<T> result = criteria.list();
				storeResult(result.isEmpty() ? new ArrayList<T>() : result);
			}
		}.runWithTransactionGuard();
	}

	public List<T> query(final String hql) throws PersistenceException {
		return new HibernateTemplate<List<T>>() {
			@Override
			public void run() throws Exception {
				Query query = HibernateUtil.createQuery(hql);
				@SuppressWarnings("unchecked")
				List<T> result = query.list();
				storeResult(result);
			}
		}.runWithTransactionGuard();
	}

	public List<T> query(final String hql, final String filterId, final Map<String, Object> filterParams) throws PersistenceException {
		return new HibernateTemplate<List<T>>() {
			@Override
			public void run() throws Exception {
				Filter filter = HibernateUtil.enableFilter(filterId);
				if (filterParams != null) {
					for (Entry<String, Object> param : filterParams.entrySet()) {
						filter.setParameter(param.getKey(), param.getValue());
					}
				}
				Query query = HibernateUtil.createQuery(hql);
				@SuppressWarnings("unchecked")
				List<T> result = query.list();
				storeResult(result);
			}
		}.runWithTransactionGuard();
	}

	public List namedQuery(final String queryName, final Map<String, Object> params) throws PersistenceException {
		return new HibernateTemplate<List>() {
			@Override
			public void run() throws Exception {
				Query query = HibernateUtil.getNamedQuery(queryName);
				if (params != null) {
					for (Entry<String, Object> param : params.entrySet()) {
						query.setParameter(param.getKey(), param.getValue());
					}
				}
				@SuppressWarnings("unchecked")
				List result = query.list();
				storeResult(result);
			}
		}.runWithTransactionGuard();
	}

	public List scalarSqlQuery(final String query, final String scalar) throws PersistenceException {
		return new HibernateTemplate<List>() {
			@Override
			public void run() throws Exception {
				@SuppressWarnings("unchecked")
				List result = HibernateUtil.createSQLQuery(query).addScalar(scalar).list();
				storeResult(result);
			}
		}.runWithTransactionGuard();
	}

}
