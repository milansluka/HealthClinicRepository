package ehc.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import antlr.StringUtils;
import ehc.hibernate.PersistenceException;
import ehc.util.StringUtil;

abstract class HibernateAccessor {

	// ********************************************************************************************
	// ** finders
	// ********************************************************************************************

	public <T> T accFindById(Class<? extends T> clazz, Serializable id) {
		try {
			return id == null ? null : (T) getCurrentSessionWithTransaction().get(clazz, id);
		}
		catch (HibernateException e) {
			return null;
		}
	}

	public <T> T accFindById(Class<? extends T> clazz, Serializable id, LockMode lockMode) {
		try {
			return id == null ? null : (T) getCurrentSessionWithTransaction().get(clazz, id, lockMode);
		}
		catch (HibernateException e) {
			return null;
		}
	}

	// ********************************************************************************************
	// ** update, merge
	// ********************************************************************************************

	public Serializable accSave(Object object) {
		return getCurrentSessionWithTransaction().save(object);
	}

	public Serializable accSave(String entityName, Object object) {
		return getCurrentSessionWithTransaction().save(entityName, object);
	}

	public void accUpdate(Object object) {
		getCurrentSessionWithTransaction().update(object);
	}

	public Object accMerge(Object object) {
		return getCurrentSessionWithTransaction().merge(object);
	}

	public void accRefresh(Object object) {
		getCurrentSessionWithTransaction().refresh(object);
	}

	public void accDelete(Object object) {
		getCurrentSessionWithTransaction().delete(object);
	}

	public void accEvict(Object object) {
		// ATTENTION: evict does not necessarily need an open transaction
		// see also: GenericDaoHibernate.save(final String entityName, final T
		// entity, final boolean historic)
		getCurrentSession().evict(object);
	}

	public Integer accExecuteUpdate(final String hql) {
		Query query = accCreateQuery(hql);
		return query.executeUpdate();
	}

	public int accUpdateBySqlQuery(String updateString) {
		if (StringUtil.isEmpty(updateString)) {
			throw new IllegalArgumentException("updateString");
		}
		SQLQuery q = getCurrentSessionWithTransaction().createSQLQuery(updateString);

		return q.executeUpdate();
	}

	// ********************************************************************************************
	// ** queries
	// ********************************************************************************************

	public Query accCreateQuery(String queryString) {
		return getCurrentSessionWithTransaction().createQuery(queryString);
	}

	public Query accGetNamedQuery(String queryName) {
		return getCurrentSessionWithTransaction().getNamedQuery(queryName);
	}

	public Filter accEnableFilter(String filterName) {
		return getCurrentSessionWithTransaction().enableFilter(filterName);
	}

	public <R> List<R> accFind(Class<? extends R> clazz, String queryString) {
		return accFind(clazz, queryString, (Object[]) null);
	}

	public <R> List<R> accFind(Class<? extends R> clazz, String queryString, Object value) {
		return accFind(clazz, queryString, new Object[]{value});
	}

	public <R> List<R> accFind(Class<? extends R> clazz, final String queryString, final Object... values) {
		Query queryObject = getCurrentSessionWithTransaction().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.list();
	}

	public List accFindByNamedParam(String queryString, String paramName, Object value) {
		return accFindByNamedParam(queryString, new String[]{paramName}, new Object[]{value});
	}

	public List accFindByNamedParam(final String queryString, final String[] paramNames, final Object[] values) {
		if (paramNames != null && values != null && paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}

		Query queryObject = getCurrentSessionWithTransaction().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
			}
		}
		return queryObject.list();
	}

	public SQLQuery accCreateSQLQuery(String queryString) {
		return getCurrentSessionWithTransaction().createSQLQuery(queryString);
	}

	public List accFindBySqlQuery(String sqlQuery) {
		return accFindBySqlQuery(sqlQuery, new String[]{}, new Object[]{});
	}

	public List accFindBySqlQuery(String sqlQuery, final String[] paramNames, final Object[] values) {
		return accFindBySqlQuery(sqlQuery, null, paramNames, values);
	}

	public List accFindBySqlQuery(String sqlQuery, Class<?> entityClass, final String[] paramNames,
		final Object[] values) {
		if (StringUtil.isEmpty(sqlQuery)) {
			throw new IllegalArgumentException("findBySqlQuery");
		}

		if (paramNames != null && values != null && paramNames.length != values.length) {
			throw new IllegalArgumentException("Length of paramNames array must match length of values array");
		}

		SQLQuery queryObject = getCurrentSessionWithTransaction().createSQLQuery(sqlQuery);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
			}
		}
		if (entityClass != null) {
			queryObject.addEntity(entityClass);
		}

		return queryObject.list();
	}
	protected void applyNamedParameterToQuery(Query queryObject, String paramName, Object value)
		throws HibernateException {

		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection) value);
		}
		else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		}
		else {
			queryObject.setParameter(paramName, value);
		}
	}

	// ********************************************************************************************
	// ** criteria
	// ********************************************************************************************

	public Criteria accCreateCriteria(Class<?> persistentClass) {
		return getCurrentSessionWithTransaction().createCriteria(persistentClass);
	}

	public Criteria accCreateCriteria(Class<?> persistentClass, String alias) {
		return getCurrentSessionWithTransaction().createCriteria(persistentClass, alias);
	}

	public <R> List<R> accFindByCriteria(Class<? extends R> clazz, DetachedCriteria criteria) {
		return accFindByCriteria(clazz, criteria, -1, -1);
	}

	public <R> List<R> accFindByCriteria(Class<? extends R> clazz, DetachedCriteria criteria, final int firstResult,
		final int maxResults) {
		Criteria executableCriteria = criteria.getExecutableCriteria(getCurrentSessionWithTransaction());
		if (firstResult >= 0) {
			executableCriteria.setFirstResult(firstResult);
		}
		if (maxResults > 0) {
			executableCriteria.setMaxResults(maxResults);
		}
		return executableCriteria.list();
	}

	public <R> List<R> accQuery(final Class<? extends R> clazz, final List<Criterion> criterionList,
		final List<Order> orderList) throws PersistenceException {
		return accQuery(clazz, (String) null, (List<String[]>) null, criterionList, orderList, (Integer) null, true);
	}

	public <R> List<R> accQuery(final Class<? extends R> clazz, final List<Criterion> criterionList,
		final List<Order> orderList, final Integer maxResult) throws PersistenceException {
		return accQuery(clazz, null, null, criterionList, orderList, maxResult, true);
	}

	public <R> List<R> accQuery(final Class<? extends R> clazz, final String alias, final List<String[]> aliases,
		final List<Criterion> criterionList, final List<Order> orderList, final boolean distinct)
		throws PersistenceException {
		return accQuery(clazz, alias, aliases, criterionList, orderList, null, distinct);
	}

	public <R> List<R> accQuery(final Class<? extends R> clazz, final String alias, final List<String[]> aliases,
		final List<Criterion> criterionList, final List<Order> orderList, final Integer maxResult,
		final boolean distinct) throws PersistenceException {
		Criteria criteria = alias == null ? HibernateUtil.createCriteria(clazz) : HibernateUtil.createCriteria(
			clazz,
			alias);
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
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<R> result = criteria.list();
		return result.isEmpty() ? new ArrayList<R>() : result;
	}

	// ********************************************************************************************
	// ** session related stuff
	// ********************************************************************************************

	protected boolean isReadOnlySession() {
		return HibernateUtil.isReadOnly();
	}

	protected Session getCurrentSessionWithTransaction() {
		return HibernateUtil.getCurrentSessionWithTransaction();
	}

	protected Session getCurrentSession() {
		return HibernateUtil.getCurrentSession();
	}

}
