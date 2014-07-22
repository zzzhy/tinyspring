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
 * This class is meant to build a simple JPQL query then query for the result using method chain.
 *
 * @author tian
 * @version $Id: $Id
 */
public class TinyQuery<T> {

	private static Logger log = LoggerFactory.getLogger(TinyQuery.class);

	private Class<T> entityClass;

	private EntityManager entityManager;

	/**
	 * the main JPQL query string.
	 */
	private StringBuilder queryString = new StringBuilder();


	/**
	 * the WHERE clause
	 */
	StringBuilder whereClause = new StringBuilder();


	/**
	 * the ORDER BY clause
	 */
	StringBuilder orderByClause = new StringBuilder();

	/**
	 * WHERE clause string buffer
	 */
	StringBuilder currentClause = new StringBuilder();

	/**
	 * the JOIN clause
	 */
	StringBuilder joinClause = new StringBuilder();


	/**
	 * The GROUP BY clause
	 */
	StringBuilder groupByClause = new StringBuilder();

	/**
	 * table alias for the query
	 */
	String tableAlias = "_model";

	/**
	 * map for the query's named parameters.
	 */
	HashMap<String, Object> namedParameters = new HashMap<>();

	/**
	 * map for the query's positional parameters.
	 */
	HashMap<Integer, Object> positionalParameters = new HashMap<>();

	private String nextWhereClauseKeyword;

	/**
	 * number of the WHERE clause
	 */
	private int whereClauseCount = 0;

	/**
	 * parameter name suffix
	 */
	private int suffix = 0;

	/**
	 * Indicates if NOT keyword is present
	 */
	private boolean isNotPresent = false;

	/**
	 * indicates whether null parameter should be ignored
	 */
	private boolean ignoreNullParameter = true;

	private int startRow = -1;

	private int maxRow = -1;


	/**
	 * <p>Constructor for TinyQuery.</p>
	 *
	 * @param entityManager
	 * 		a {@link javax.persistence.EntityManager} object.
	 * @param entityClass
	 * 		a {@link java.lang.Class} object.
	 */
	public TinyQuery(EntityManager entityManager, Class<T> entityClass) {

		this.entityManager = entityManager;
		this.entityClass = entityClass;
	}

	/**
	 * set whether null parameters should be ignored. if set to TRUE,
	 * WHERE clause with null parameter will be ignored. otherwise an
	 * Exception will be thrown.
	 *
	 * @param ignoreNull
	 * 		whether null parameters should be ignored.
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> ignoreNullParameter(boolean ignoreNull) {

		this.ignoreNullParameter = ignoreNull;
		return this;
	}


	/**
	 * Equivalent to "SELECT m FROM EntityClass m"
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> select() {

		queryString.append(String.format("SELECT %s FROM %s %s",
				tableAlias, entityClass.getSimpleName(), tableAlias));

		return this;
	}

	/**
	 * select certain columns
	 * Equivalent to "SELECT m.col1,m.col2 FROM EntityClass m"
	 *
	 * @param cols
	 * 		the columns to be selected
	 *
	 * @return the same instance
	 */
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

	/**
	 * adds DISTINCT keyword to the query. must be invoked after select()
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> distinct() {

		queryString.insert(6, " DISTINCT ");
		return this;
	}

	/**
	 * add additional entity to FROM clause besides the invoking dao object represents.
	 * must be invoked after select().
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

	/**
	 * adds JOIN clause
	 *
	 * @param column
	 * 		the column to be join
	 * @param alias
	 * 		the alias for the joined table
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> join(String column, String alias) {

		joinClause.append(
				String.format(" JOIN %s.%s %s ", tableAlias, column, alias));
		return this;
	}

	/**
	 * <p>where.</p>
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> where() {

		if(queryString.length() == 0) {
			throw new RuntimeException("No SELECT ** FROM ** found. invoke select() first.");
		}
		nextWhereClauseKeyword = " WHERE ";
		return this;
	}

	/**
	 * <p>and.</p>
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> and() {

		nextWhereClauseKeyword = " AND ";
		return this;
	}

	/**
	 * <p>or.</p>
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> or() {

		nextWhereClauseKeyword = " OR ";
		return this;
	}

	/**
	 * <p>not.</p>
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> not() {

		isNotPresent = true;
		return this;
	}

	/**
	 * <p>greaterThan.</p>
	 *
	 * @param column
	 * 		column name
	 * @param value
	 * 		value
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> greaterThan(String column, Object value) {

		return greaterThan(null, column, value);
	}

	/**
	 * <p>greaterThan.</p>
	 *
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> greaterThan(String alias, String column, Object value) {

		boolean valid = validateParameters(value);

		if(valid) {

			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			String parameterHolder = column + "_" + suffix++;

			currentClause.append(String.format("%s.%s>:%s", alias, column, parameterHolder));
			namedParameters.put(parameterHolder, value);

		}
		postWhereClause(valid);

		return this;
	}

	/**
	 * <p>greaterThanOrEqual.</p>
	 *
	 * @param column
	 * 		column name
	 * @param value
	 * 		value object 		ignored if value is null.
	 *
	 * @return the same instance
	 *
	 * @see TinyQuery#equal(String, String, Object)
	 */
	public TinyQuery<T> greaterThanOrEqual(String column, Object value) {

		return greaterThanOrEqual(null, column, value);
	}

	/**
	 * <p>greaterThanOrEqual.</p>
	 *
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> greaterThanOrEqual(String alias, String column, Object value) {

		boolean valid = validateParameters(value);
		if(valid) {

			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			String parameterHolder = column + "_" + suffix++;
			currentClause.append(String.format("%s.%s>=:%s", alias, column, parameterHolder));
			namedParameters.put(parameterHolder, value);

		}
		postWhereClause(valid);
		return this;
	}

	/**
	 * <p>lessThanOrEqual.</p>
	 *
	 * @param column
	 * 		column name
	 * @param value
	 * 		value object 		ignored if value is null.
	 *
	 * @return the same instance
	 *
	 * @see TinyQuery#equal(String, String, Object)
	 */
	public TinyQuery<T> lessThanOrEqual(String column, Object value) {

		return lessThanOrEqual(null, column, value);
	}

	/**
	 * <p>lessThanOrEqual.</p>
	 *
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> lessThanOrEqual(String alias, String column, Object value) {

		boolean valid = validateParameters(value);
		if(valid) {
			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			String parameterHolder = column + "_" + suffix++;
			currentClause.append(String.format("%s.%s<=:%s", alias, column, parameterHolder));
			namedParameters.put(parameterHolder, value);

		}
		postWhereClause(valid);
		return this;
	}

	/**
	 * <p>lessThan.</p>
	 *
	 * @param column
	 * 		column name
	 * @param value
	 * 		value object 		ignored if value is null.
	 *
	 * @return the same instance
	 *
	 * @see TinyQuery#equal(String, String, Object)
	 */
	public TinyQuery<T> lessThan(String column, Object value) {

		return lessThan(null, column, value);
	}

	/**
	 * <p>lessThan.</p>
	 *
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> lessThan(String alias, String column, Object value) {

		boolean valid = validateParameters(value);
		if(valid) {

			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			String parameterHolder = column + "_" + suffix++;
			currentClause.append(String.format("%s.%s<:%s", alias, column, parameterHolder));
			namedParameters.put(parameterHolder, value);

		}
		postWhereClause(valid);
		return this;
	}

	/**
	 * <p>equal.</p>
	 *
	 * @param column
	 * 		column name
	 * @param value
	 * 		value object 		ignored if value is null.
	 *
	 * @return the same instance
	 *
	 * @see TinyQuery#equal(String, String, Object)
	 */
	public TinyQuery<T> equal(String column, Object value) {

		return equal(null, column, value);
	}

	/**
	 * <p>equal.</p>
	 *
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> equal(String alias, String column, Object value) {

		boolean valid = validateParameters(value);
		if(!ignoreNullParameter && !valid) {
			throw new RuntimeException(String.format("Value for column %s.%s is null", alias,
					column));
		}
		if(valid) {
			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			String parameterHolder = column.replace(".", "_") + "_" + suffix++;
			currentClause.append(String.format("%s.%s=:%s", alias, column, parameterHolder));
			namedParameters.put(parameterHolder, value);

		}
		postWhereClause(valid);
		return this;
	}

	/**
	 * <p>notEqual.</p>
	 *
	 * @param column
	 * 		column name
	 * @param value
	 * 		value object 		ignored if value is null.
	 *
	 * @return the same instance
	 *
	 * @see TinyQuery#equal(String, String, Object)
	 */
	public TinyQuery<T> notEqual(String column, Object value) {

		return notEqual(null, column, value);
	}

	/**
	 * <p>notEqual.</p>
	 *
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> notEqual(String alias, String column, Object value) {

		boolean valid = validateParameters(value);
		if(valid) {

			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			String parameterHolder = column + "_" + suffix++;
			currentClause.append(String.format("%s.%s<>:%s", alias, column, parameterHolder));
			namedParameters.put(parameterHolder, value);

		}
		postWhereClause(valid);
		return this;
	}

	/**
	 * <p>like.</p>
	 *
	 * @param column
	 * 		column name
	 * @param value
	 * 		value object 		ignored if value is null.
	 *
	 * @return the same instance
	 *
	 * @see TinyQuery#equal(String, String, Object)
	 */
	public TinyQuery<T> like(String column, String value) {

		return like(null, column, value);
	}

	/**
	 * <p>like.</p>
	 *
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return the same instance
	 */
	public TinyQuery<T> like(String alias, String column, String value) {

		boolean valid = validateParameters(value);
		if(valid) {

			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			currentClause.append(String.format("%s.%s LIKE '%s'", alias, column, value));

		}
		postWhereClause(valid);
		return this;
	}


	/**
	 * <p>between.</p>
	 *
	 * @param column
	 * 		a {@link java.lang.String} object.
	 * @param value1
	 * 		a {@link java.lang.Object} object.
	 * @param value2
	 * 		a {@link java.lang.Object} object.
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> between(String column, Object value1, Object value2) {

		return between(null, column, value1, value2);
	}

	/**
	 * <p>between.</p>
	 *
	 * @param alias
	 * 		a {@link java.lang.String} object.
	 * @param column
	 * 		a {@link java.lang.String} object.
	 * @param value1
	 * 		a {@link java.lang.Object} object.
	 * @param value2
	 * 		a {@link java.lang.Object} object.
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> between(String alias, String column, Object value1, Object value2) {

		boolean valid = validateParameters(value1, value2);
		if(valid) {
			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			String parameterHolder = column + "_" + suffix++;
			currentClause.append(String.format("%s.%s BETWEEN :%s AND :%s",
					alias, column, parameterHolder + "1", parameterHolder + "2"));
			namedParameters.put(parameterHolder + "1", value1);
			namedParameters.put(parameterHolder + "2", value2);

		}
		postWhereClause(valid);
		return this;
	}

	/**
	 * <p>in.</p>
	 *
	 * @param column
	 * 		a {@link java.lang.String} object.
	 * @param value
	 * 		an array of {@link java.lang.Object} objects.
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 *
	 * @see TinyQuery#equal(String, String, Object)
	 */
	public TinyQuery<T> in(String column, Object[] value) {

		return in(null, column, value);
	}

	/**
	 * <p>in.</p>
	 *
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param values
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> in(String alias, String column, Object[] values) {

		boolean valid = validateParameters(values);
		if(valid) {

			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			String parameterHolder = column + "_" + suffix++;
			ArrayList<String> list = new ArrayList<>();
			for(int i = 0; i < values.length; i++) {

				list.add(":" + parameterHolder + i);
				namedParameters.put(parameterHolder + i + "", values[i]);
			}
			String valueString = StringUtils.join(list, ",");
			currentClause.append(String.format("%s.%s IN (%s)", alias, column,
					valueString));

		}
		postWhereClause(valid);
		return this;
	}

	/**
	 * <p>orderBy.</p>
	 *
	 * @param colunmn
	 * 		a {@link java.lang.String} object.
	 * @param orderType
	 * 		a {@link org.trii.tinyspring.dao.OrderType} object.
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> orderBy(String colunmn, OrderType orderType) {

		return orderBy(null, colunmn, orderType);
	}

	/**
	 * <p>orderBy.</p>
	 *
	 * @param alias
	 * 		a {@link java.lang.String} object.
	 * @param colunmn
	 * 		a {@link java.lang.String} object.
	 * @param orderType
	 * 		a {@link org.trii.tinyspring.dao.OrderType} object.
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> orderBy(String alias, String colunmn, OrderType orderType) {

		if(queryString.length() == 0) {
			throw new RuntimeException("No SELECT ** FROM ** found. invoke select() first.");
		}
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
	// native JPQL query
	////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * set a JPQL query. Invocation of this method will invalidate all precedent clause.
	 *
	 * @param jpql
	 * 		a {@link java.lang.String} object.
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> query(String jpql) {

		queryString.delete(0, queryString.length());
		queryString.append(jpql);
		return this;
	}

	/**
	 * adds a positional parameter for the JPQL set by query()
	 *
	 * @param position
	 * 		a int.
	 * @param value
	 * 		a {@link java.lang.Object} object.
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> param(int position, Object value) {

		positionalParameters.put(position, value);
		return this;
	}

	/**
	 * adds a named parameter for the JPQL set by query()
	 *
	 * @param name
	 * 		a {@link java.lang.String} object.
	 * @param value
	 * 		a {@link java.lang.Object} object.
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> param(String name, Object value) {

		namedParameters.put(name, value);
		return this;
	}

	////////////////////////////////////////////////////////////////////////////////////////
	// Result acquiring
	////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * counts the results. It is equivalent to "SELECT count() FROM ..."
	 *
	 * @return number of the results
	 */
	public long count() {

		queryString.delete(0, queryString.length());
		queryString.append(
				String.format("SELECT count(%s) FROM %s %s",
						tableAlias, entityClass.getSimpleName(), tableAlias));
		Query query = createQuery();
		return (long) query.getSingleResult();
	}

	/**
	 * return true if count of the result equals to zero otherwise false.
	 *
	 * @return whether the query has results
	 */
	public boolean hasNoResult() {

		return !hasResult();
	}

	/**
	 * return true if count of the result not equals to zero otherwise false.
	 *
	 * @return whether the query has results
	 */
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
	 * @return first element of the result set
	 */
	public T getFirstResult() {

		List<T> result = getResultList(0, 1);
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * limits the number of the results based on page number.
	 * the parameters are null pointer safe. invalid parameters will be ignored.
	 *
	 * @param page
	 * 		page number numbered from 1.
	 * @param numberPerPage
	 * 		maximum number of results to retrieve.
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 *
	 * @see javax.persistence.Query#setFirstResult(int)
	 * @see javax.persistence.Query#setMaxResults(int)
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
	 * @return a list of the results
	 */
	public List<T> getResultList() {

		Query query = createQuery();
		if(startRow >= 0 && maxRow >= 0) {
			query.setFirstResult(startRow).setMaxResults(maxRow);
		}
		return query.getResultList();
	}

	/**
	 * Execute a SELECT query and return the query results as an List.
	 *
	 * @return a untyped list of the results
	 */
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
	 * @return a list of the results
	 *
	 * @see javax.persistence.Query#setFirstResult(int)
	 * @see javax.persistence.Query#setMaxResults(int)
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
	 * @return a jpa Query object
	 *
	 * @see javax.persistence.Query
	 */
	private Query createQuery() {

		if(nextWhereClauseKeyword != null) {
			throw new RuntimeException(nextWhereClauseKeyword + "is present but no clause found");
		}
		queryString.append(joinClause);
		queryString.append(whereClause);
		queryString.append(orderByClause);
		queryString.append(groupByClause);

		log.debug("Query built: " + queryString);
		Query query = entityManager.createQuery(queryString.toString());

		for(int key : positionalParameters.keySet()) {
			query.setParameter(key, positionalParameters.get(key));
		}
		for(String key : namedParameters.keySet()) {
			query.setParameter(key, namedParameters.get(key));
		}
		return query;
	}

	/**
	 * encloses a where clause
	 *
	 * @param valid
	 * 		whether the given WHERE clause's parameter is valid.
	 */
	private void postWhereClause(boolean valid) {

		if(valid) {
			if(currentClause.length() > 0) {
				if(isNotPresent) {
					currentClause.insert(0, "NOT(");
					currentClause.append(")");
					isNotPresent = false;
				}

			}
			currentClause.insert(0, whereClauseCount == 0 ? " WHERE " : nextWhereClauseKeyword);
			whereClause.append(currentClause);
			whereClauseCount++;
		}
		nextWhereClauseKeyword = null;
		currentClause.delete(0, currentClause.length());
	}

	/**
	 * validates parameters
	 *
	 * @param values
	 * 		parameters
	 *
	 * @return true if no values is null,  otherwise false
	 */
	private boolean validateParameters(Object... values) {

		boolean result = values != null && values.length > 0;
		if(values != null) {
			for(Object obj : values) {
				result &= obj != null;
			}
		}

		return result;
	}

}
