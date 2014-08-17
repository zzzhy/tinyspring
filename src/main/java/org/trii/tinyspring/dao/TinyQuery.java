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

	private static Logger log = LoggerFactory.getLogger(TinyQuery.class);

	private Class<T> entityClass;

	private EntityManager entityManager;

	private StringBuilder queryString = new StringBuilder();

	/**
	 * the main query string.
	 */
	private StringBuilder whereClause = new StringBuilder();


	/**
	 * The ORDER BY clause
	 */
	private StringBuilder orderByClause = new StringBuilder();

	/**
	 * The GROUP BY clause
	 */
	private StringBuilder groupByClause = new StringBuilder();

	/**
	 * WHERE clause string buffer
	 */
	private StringBuilder currentClause = new StringBuilder();

	private StringBuilder joinClause = new StringBuilder();

	/**
	 * Table alias for the query
	 */
	private String tableAlias = "_model";

	/**
	 * Map for the query's named parameters.
	 */
	private HashMap<String, Object> namedParameters = new HashMap<>();

	/**
	 * Map for the query's positional parameters.
	 */
	private HashMap<Integer, Object> positionalParameters = new HashMap<>();

	private String nextWhereClauseKeyword;

	/**
	 * Number of the WHERE clause
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

	private boolean ignoreNullParameter = true;

	private int startRow = -1;

	private int maxRow = -1;

	private boolean showJpql = true;


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

	public TinyQuery<T> where() {

		if(queryString.length() == 0) {
			throw new RuntimeException("No SELECT ** FROM ** found. invoke select() first.");
		}
		nextWhereClauseKeyword = " WHERE ";
		return this;
	}

	public TinyQuery<T> and() {

		nextWhereClauseKeyword = " AND ";
		return this;
	}

	public TinyQuery<T> or() {

		nextWhereClauseKeyword = " OR ";
		return this;
	}

	public TinyQuery<T> not() {

		isNotPresent = true;
		return this;
	}

	/**
	 * @param column
	 * @param value
	 *
	 * @return
	 */
	public TinyQuery<T> greaterThan(String column, Object value) {

		return greaterThan(null, column, value);
	}

	/**
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return
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
	 * @param column
	 * @param value
	 *
	 * @return
	 */
	public TinyQuery<T> greaterThanOrEqual(String column, Object value) {

		return greaterThanOrEqual(null, column, value);
	}

	/**
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return
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
	 * @param column
	 * @param value
	 *
	 * @return
	 */
	public TinyQuery<T> lessThanOrEqual(String column, Object value) {

		return lessThanOrEqual(null, column, value);
	}

	/**
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return
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
	 * @param column
	 * @param value
	 *
	 * @return
	 */
	public TinyQuery<T> lessThan(String column, Object value) {

		return lessThan(null, column, value);
	}

	/**
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return
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
	 * @param column
	 * @param value
	 *
	 * @return
	 */
	public TinyQuery<T> equal(String column, Object value) {

		return equal(null, column, value);
	}

	/**
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return
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
	 * @param column
	 *
	 * @return
	 */
	public TinyQuery<T> isNull(String column) {

		return isNull(null, column);
	}

	/**
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 *
	 * @return
	 */
	public TinyQuery<T> isNull(String alias, String column) {


		if(StringUtils.isBlank(alias)) {
			alias = this.tableAlias;
		}
		currentClause.append(String.format("%s.%s IS NULL", alias, column));
		postWhereClause(true);
		return this;
	}

	/**
	 * @param column
	 *
	 * @return
	 */
	public TinyQuery<T> isNotNull(String column) {

		return isNotNull(null, column);
	}

	/**
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 *
	 * @return
	 */
	public TinyQuery<T> isNotNull(String alias, String column) {


		if(StringUtils.isBlank(alias)) {
			alias = this.tableAlias;
		}
		currentClause.append(String.format("%s.%s IS NOT NULL", alias, column));
		postWhereClause(true);
		return this;
	}

	/**
	 * @param column
	 * @param value
	 *
	 * @return
	 */
	public TinyQuery<T> notEqual(String column, Object value) {

		return notEqual(null, column, value);
	}

	/**
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return
	 */
	public TinyQuery<T> notEqual(String alias, String column, Object value) {

		boolean valid = validateParameters(value);
		if(valid) {

			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			String parameterHolder = column.replace(".", "_") + "_" + suffix++;
			currentClause.append(String.format("%s.%s<>:%s", alias, column, parameterHolder));
			namedParameters.put(parameterHolder, value);
		}
		postWhereClause(valid);
		return this;
	}

	/**
	 * @param column
	 * @param value
	 *
	 * @return
	 */
	public TinyQuery<T> like(String column, String value) {

		return like(null, column, value);
	}

	/**
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param value
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return
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


	public TinyQuery<T> between(String column, Object value1, Object value2) {

		return between(null, column, value1, value2);
	}

	public TinyQuery<T> between(String alias, String column, Object value1, Object value2) {

		boolean valid = validateParameters(value1, value2);
		if(valid) {
			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			String parameterHolder = column.replace(".", "_") + "_" + suffix++;
			currentClause.append(String.format("%s.%s BETWEEN :%s AND :%s",
					alias, column, parameterHolder + "1", parameterHolder + "2"));
			namedParameters.put(parameterHolder + "1", value1);
			namedParameters.put(parameterHolder + "2", value2);
		}
		postWhereClause(valid);
		return this;
	}

	/**
	 * @param column
	 * @param value
	 *
	 * @return
	 */
	public TinyQuery<T> in(String column, Object[] value) {

		return in(null, column, value);
	}

	/**
	 * @param alias
	 * 		JPQL table alias
	 * @param column
	 * 		JPQL column name
	 * @param values
	 * 		value object
	 * 		ignored if value is null.
	 *
	 * @return
	 */
	public TinyQuery<T> in(String alias, String column, Object[] values) {

		boolean valid = validateParameters(values);
		if(valid) {

			if(StringUtils.isBlank(alias)) {
				alias = this.tableAlias;
			}
			String parameterHolder = column.replace(".", "_") + "_" + suffix++;
			ArrayList<String> list = new ArrayList<>();
			for(int i = 0; i < values.length; i++) {

				list.add(":" + parameterHolder + "_" + i);
				namedParameters.put(parameterHolder + "_" + i, values[i]);
			}
			String valueString = StringUtils.join(list, ",");
			currentClause.append(String.format("%s.%s IN (%s)", alias, column,
					valueString));
		}
		postWhereClause(valid);
		return this;
	}

	public TinyQuery<T> orderBy(String colunmn, OrderType orderType) {

		return orderBy(null, colunmn, orderType);
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
	 * @return a list of the results
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
	 * @return a list of the results
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
	private Query createQuery() {

		if(nextWhereClauseKeyword != null) {
			throw new RuntimeException(nextWhereClauseKeyword + "is present but no clause found");
		}
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

	/**
	 * @param valid
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
			currentClause.insert(0, whereClauseCount == 0 ? " WHERE " :
					nextWhereClauseKeyword.contains("WHERE") ? " AND " :
							nextWhereClauseKeyword.trim().equalsIgnoreCase("WHERE")
									? " AND " : nextWhereClauseKeyword);

			whereClause.append(currentClause);
			whereClauseCount++;
		}
		nextWhereClauseKeyword = null;
		currentClause.delete(0, currentClause.length());
	}

	private boolean validateParameters(Object... values) {

		boolean result = values != null && values.length > 0;
		if(values != null) {
			for(Object obj : values) {
				result &= obj != null;
			}
		}

		return result;
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
