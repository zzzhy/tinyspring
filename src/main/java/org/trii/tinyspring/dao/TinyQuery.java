package org.trii.tinyspring.dao;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: June 22, 2014
 * Time: 18:33
 */
public class TinyQuery<T> {

	protected static Logger log = LoggerFactory.getLogger(TinyQuery.class);

	protected Class<T> entityClass;

	protected EntityManager entityManager;

	protected StringBuilder queryString = new StringBuilder();

	protected StringBuilder joinClause = new StringBuilder();

	/**
	 * the main query string.
	 */
	protected StringBuilder whereClause = new StringBuilder();

	/**
	 * The ORDER BY clause
	 */
	protected StringBuilder orderByClause = new StringBuilder();

	/**
	 * The GROUP BY clause
	 */
	protected StringBuilder groupByClause = new StringBuilder();

	/**
	 * Table alias for the query
	 */
	public static String tableAlias = "_this";

	/**
	 * Map for the query's named parameters.
	 */
	protected HashMap<String, Object> namedParameters = new HashMap<>();

	/**
	 * Map for the query's positional parameters.
	 */
	protected HashMap<Integer, Object> positionalParameters = new HashMap<>();

	protected int index = 0;

	protected boolean ignoreNullParameter = true;

	protected int startRow = -1;

	protected int maxRow = -1;

	protected boolean showJpql = true;


	public TinyQuery(EntityManager entityManager, Class<T> entityClass) {

		this.entityManager = entityManager;
		this.entityClass = entityClass;
	}

	public TinyQuery(EntityManager entityManager, Class<T> entityClass, boolean showJpql) {

		this.entityManager = entityManager;
		this.entityClass = entityClass;
		this.showJpql = showJpql;
	}

	/**
	 * set whether null parameters should be ignored. if set to TRUE,
	 * WHERE clause with null parameter will be ignored. otherwise an
	 * Exception will be thrown.
	 *
	 * @param ignoreNull
	 * 		whether null parameters should be ignored.
	 *
	 * @return
	 */
	public TinyQuery<T> ignoreNullParameter(boolean ignoreNull) {

		this.ignoreNullParameter = ignoreNull;
		return this;
	}


	public TinyQuery<T> select() {

		queryString.append(String.format("SELECT %s FROM %s %s",
				tableAlias, entityClass.getSimpleName(), tableAlias));

		return this;
	}

	public TinyQuery<T> select(String... cols) {

		queryString.append("SELECT ");
		ArrayList<String> list = new ArrayList<>();
		StringBuilder buffer = new StringBuilder();
		for(String s : cols) {
			if(!s.contains(".")) {
				buffer.append(tableAlias).append(".");
			}
			buffer.append(s).append(" ");
			list.add(buffer.toString());
			buffer.delete(0, buffer.length());
		}
		queryString.append(StringUtils.join(list, ","));
		queryString.append(String.format(" FROM %s %s", entityClass.getSimpleName(), tableAlias));

		return this;
	}

	public TinyQuery<T> selectDistinct() {

		queryString.append(String.format("SELECT DISTINCT %s FROM %s %s",
				tableAlias, entityClass.getSimpleName(), tableAlias));

		return this;
	}

	/**
	 * add additional entity to FROM clause besides the invoking dao object represents.
	 * must invoke after select().
	 *
	 * @param entityClass
	 * 		entity class
	 * @param alias
	 * 		JPQL table alias
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> from(Class entityClass, String alias) {

		queryString.append(String.format(",%s %s",
				entityClass.getSimpleName(), alias));

		return this;
	}


	public TinyQuery<T> join(String column, String alias) {

		joinClause.append(
				String.format(" JOIN %s.%s %s ", tableAlias, column, alias));
		return this;
	}


	public TinyQuery<T> where(TinyPredicate... predicates) {

		TinyPredicate f = TinyPredicate.and(predicates);

		whereClause.append(" WHERE ").append(formatPredicate(f));
		return this;
	}

	public TinyQuery<T> and(TinyPredicate... predicates) {

		TinyPredicate f = TinyPredicate.and(predicates);

		whereClause.append(whereClause.length() == 0 ? " WHERE " : " AND ").append(formatPredicate
				(f));
		return this;
	}

	public TinyQuery<T> or(TinyPredicate... predicates) {

		TinyPredicate f = TinyPredicate.and(predicates);

		whereClause.append(whereClause.length() == 0 ? " WHERE " : " OR ").append(formatPredicate
				(f));
		return this;
	}


	public TinyQuery<T> orderBy(String column, OrderType orderType) {

		return orderBy(null, column, orderType);
	}

	public TinyQuery<T> orderBy(String alias, String colunmn, OrderType orderType) {

		if(orderByClause.length() == 0) {
			orderByClause.append(" ORDER BY ");
		} else {
			orderByClause.append(",");
		}
		if(alias == null) {
			orderByClause.append(tableAlias);
		} else {
			orderByClause.append(alias);
		}
		orderByClause.append(".").append(colunmn).append(" ").append(orderType);
		return this;
	}

	public TinyQuery<T> groupBy(String colunmn) {

		return groupBy(null, colunmn);
	}

	public TinyQuery<T> groupBy(String alias, String colunmn) {

		if(groupByClause.length() == 0) {
			groupByClause.append(" GROUP BY ");
		} else {
			groupByClause.append(",");
		}
		if(alias == null) {
			groupByClause.append(tableAlias);
		} else {
			groupByClause.append(alias);
		}
		groupByClause.append(".").append(colunmn).append(" ");
		return this;
	}

	////////////////////////////////////////////////////////////////////////////////////////
	// raw JPQL query
	////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * add JPQL query. Invocation of this method will remove all precedent clause.
	 *
	 * @param jpql
	 *
	 * @return
	 */
	public TinyQuery<T> query(String jpql) {

		queryString.delete(0, queryString.length());
		queryString.append(jpql);
		return this;
	}

	/**
	 * add a positional parameter
	 *
	 * @param position
	 * @param value
	 *
	 * @return
	 */
	public TinyQuery<T> param(int position, Object value) {

		positionalParameters.put(position, value);
		return this;
	}

	/**
	 * add a named parameter
	 *
	 * @param name
	 * @param value
	 *
	 * @return
	 */
	public TinyQuery<T> param(String name, Object value) {

		namedParameters.put(name, value);
		return this;
	}

	////////////////////////////////////////////////////////////////////////////////////////
	// Result acquiring
	////////////////////////////////////////////////////////////////////////////////////////

	public long count() {

		queryString.delete(0, queryString.length());
		queryString.append(
				String.format("SELECT count(%s) FROM %s %s",
						tableAlias, entityClass.getSimpleName(), tableAlias));
		Query query = createQuery();
		return (long) query.getSingleResult();
	}

	public boolean hasNoResult() {

		return !hasResult();
	}

	public boolean hasResult() {

		queryString.delete(0, queryString.length());
		queryString.append(
				String.format("SELECT count(%s) FROM %s %s",
						tableAlias, entityClass.getSimpleName(), tableAlias));
		Query query = createQuery();
		return (Long) query.getSingleResult() != 0;
	}

	/**
	 * This method just executes the prepared JPA query then call its getSingleResult() method.
	 * The returned value is untyped.
	 *
	 * @return single untyped result
	 *
	 * @see javax.persistence.Query#getSingleResult()
	 */
	public Object getSingleResult() {

		Query query = createQuery();
		return query.getSingleResult();
	}

	/**
	 * Execute a SELECT query and return the query's first result.
	 *
	 * @return first element of the results set
	 */
	public T getFirstResult() {

		List<T> result = getResultList(0, 1);
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * limits the number of the results based on page number.
	 *
	 * @param page
	 * 		numbered from 1
	 * @param numberPerPage
	 * 		maximum number of results to retrieve
	 *
	 * @return
	 *
	 * @see javax.persistence.Query#setFirstResult(int)
	 * @see javax.persistence.Query#setMaxResults(int)
	 */
	public TinyQuery<T> page(Integer page, Integer numberPerPage) {

		if(page != null && numberPerPage != null) {
			startRow = (page - 1) * numberPerPage;
			maxRow = numberPerPage;
		}
		return this;
	}

	/**
	 * Execute a SELECT query and return the query results as an List.
	 *
	 * @return a predicateList of the results
	 */
	public List<T> getResultList() {

		Query query = createQuery();
		if(startRow >= 0 && maxRow >= 0) {
			query.setFirstResult(startRow).setMaxResults(maxRow);
		}
		return query.getResultList();
	}

	public List getUntypedResultList() {

		Query query = createQuery();
		if(startRow >= 0 && maxRow >= 0) {
			query.setFirstResult(startRow).setMaxResults(maxRow);
		}
		return query.getResultList();
	}

	/**
	 * Execute a SELECT query and return the query results as an List.
	 *
	 * @param startRow
	 * 		position of the first result, numbered from 0, null-safe
	 * @param maxRow
	 * 		maximum number of results to retrieve, null-safe
	 *
	 * @return a predicateList of the results
	 *
	 * @see javax.persistence.Query#setFirstResult(int)
	 * @see javax.persistence.Query#setMaxResults(int)
	 */
	public List<T> getResultList(Integer startRow, Integer maxRow) {

		Query query = createQuery();
		if(startRow != null && maxRow != null) {
			query.setFirstResult(startRow).setMaxResults(maxRow);
		}
		return query.getResultList();
	}

	////////////////////////////////////////////////////////////////////////////////////////
	// Internal methods
	////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @return jpa query
	 */
	protected Query createQuery() {

		queryString.append(joinClause);
		queryString.append(whereClause);
		queryString.append(orderByClause);
		queryString.append(groupByClause);
		if(showJpql) {
			log.debug("Query built: " + queryString);
		}
		Query query = entityManager.createQuery(queryString.toString());

		for(int key : positionalParameters.keySet()) {
			query.setParameter(key, positionalParameters.get(key));
		}
		for(String key : namedParameters.keySet()) {
			query.setParameter(key, namedParameters.get(key));
		}
		return query;
	}

	public String toString() {

		StringBuilder buffer = new StringBuilder(queryString);
		buffer.append(joinClause);
		buffer.append(whereClause);
		buffer.append(orderByClause);
		buffer.append(groupByClause);
		return buffer.toString();
	}

	/**
	 * format predicate into jpql expression and inject condition values into query
	 *
	 * @param predicate
	 *
	 * @return
	 */
	protected String formatPredicate(TinyPredicate predicate) {

		if(!predicate.isValid) {
			if(ignoreNullParameter) {
				return "";
			} else {
				throw new IllegalArgumentException(predicate.toString() + " is invalid");
			}
		}
		ArrayList<String> list;
		switch(predicate.predicateType) {
			case SIMPLE:
				return predicate.createExpression(this);
			case AND:
				list = new ArrayList<>();
				for(TinyPredicate p : predicate.predicateList) {
					String exp = formatPredicate(p);
					if(StringUtils.isNotBlank(exp)) {
						list.add(exp);
					}
				}
				return "(" + StringUtils.join(list, " AND ") + ")";
			case OR:
				list = new ArrayList<>();
				for(TinyPredicate p : predicate.predicateList) {
					list.add(formatPredicate(p));
				}
				return "(" + StringUtils.join(list, " OR ") + ")";
			case NOT:
				return "(NOT " + formatPredicate(predicate.predicateList.get(0)) + ")";
			default:
				throw new IllegalArgumentException("Unknown predicate type");
		}
	}

	/**
	 * null safe
	 *
	 * @param term
	 *
	 * @return
	 */
	public static String likeContains(String term) {

		return term == null ? null : "%" + term + "%";
	}


}
