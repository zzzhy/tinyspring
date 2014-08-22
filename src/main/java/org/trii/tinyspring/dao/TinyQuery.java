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
 * A tiny flexible JPQL query builder
 * User: Sebastian MA
 * Date: June 22, 2014
 * Time: 18:33
 */
public class TinyQuery<T> {

	protected static Logger log = LoggerFactory.getLogger(TinyQuery.class);

	protected Class<T> entityClass;

	protected EntityManager entityManager;

	/**
	 * the main query string.
	 */
	protected StringBuilder selectClause = new StringBuilder();


	/**
	 * The JOIN clause
	 */
	protected StringBuilder joinClause = new StringBuilder();

	/**
	 * The WHERE clause
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

	protected boolean distinct = false;

	/**
	 * Map for the query's named parameters.
	 */
	protected HashMap<String, Object> namedParameters = new HashMap<>();

	/**
	 * Map for the query's positional parameters.
	 */
	protected HashMap<Integer, Object> positionalParameters = new HashMap<>();

	protected int index = 0;

	/**
	 * whether parameter with null value should be ignored.
	 */
	protected boolean ignoreNullParameter = true;

	/**
	 * start row numbered from 0
	 */
	protected int startRow = -1;

	/**
	 * max rows to retrieve
	 */
	protected int maxRow = -1;

	/**
	 *
	 */
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
	 * Set whether null-valued parameters should be ignored.<br>
	 * If set to TRUE, the predicates in WHERE clause with null-valued parameter will be ignored.
	 * otherwise an IllegalArgumentException will be thrown.<br><br>
	 * Ignoring null-valued predicate is convenient for the optional conditions like filters,
	 * but ignoring a obligatory condition may bring a false result. For example you want to
	 * find user with specific username, if the username passed in as parameter is null therefore
	 * ignored, the query will just find all the users. In this case set ignoreNullParameter to
	 * FALSE to force throw an IllegalArgumentException while a null value is encountered.
	 *
	 * @param ignore
	 * 		whether null parameters should be ignored.
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> ignoreNullParameter(boolean ignore) {

		this.ignoreNullParameter = ignore;
		return this;
	}

	////////////////////////////////////////////////////////////////////////////////////////
	// BEGIN structural JPQL query
	//
	/**
	 * Select the entity class managed by the query.
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> select() {

		selectClause.append(String.format("SELECT %s FROM %s %s",
				tableAlias, entityClass.getSimpleName(), tableAlias));

		return this;
	}

	/**
	 * Select specific columns, the result must be retrieved by <code>getUntypedResultList()
	 * </code>
	 *
	 * @return the same TinyQuery instance
	 */
	//todo select from different table is broken!
	public TinyQuery<T> select(String... cols) {

		selectClause.append("SELECT ");
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
		selectClause.append(StringUtils.join(list, ","));
		selectClause.append(String.format(" FROM %s %s", entityClass.getSimpleName(), tableAlias));

		return this;
	}

	/**
	 * Distinguish the result. This will add DISTINCT keyword to the query. Can be invoked
	 * anytime before retrieving the result.
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> distinct() {

		distinct = true;

		return this;
	}

	/**
	 * Add additional entity to FROM clause besides the entity  which the invoking DAO object
	 * represents. must invoke after select().
	 *
	 * @param entityClass
	 * 		entity class
	 * @param alias
	 * 		JPQL table alias
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> from(Class entityClass, String alias) {

		selectClause.append(String.format(",%s %s",
				entityClass.getSimpleName(), alias));

		return this;
	}

	/**
	 * Add additional entity to FROM clause besides the entity  which the invoking DAO object
	 * represents. must invoke after <code>select()</code>.
	 *
	 * @param column
	 * 		column to join
	 * @param alias
	 * 		JPQL joined table's alias
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> join(String column, String alias) {

		joinClause.append(
				String.format(" JOIN %s.%s %s ", tableAlias, column, alias));
		return this;
	}

	/**
	 * Insert the predicates into WHERE clause.
	 * Safe for multiple invocation, in this case the predicates will be combined by AND.
	 *
	 * @param predicates
	 * 		predicates for the WHERE clause, multiple predicates will be combined by AND.
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> where(TinyPredicate... predicates) {

		and(predicates);
		return this;
	}

	/**
	 * Combine the predicates by AND with the previous predicates.
	 *
	 * @param predicates
	 * 		predicates for the WHERE clause, multiple predicates will be conjunct by AND.
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> and(TinyPredicate... predicates) {

		TinyPredicate f = TinyPredicate.and(predicates);

		whereClause.append(whereClause.length() == 0 ? " WHERE " : " AND ").append(formatPredicate
				(f));
		return this;
	}

	/**
	 * Combine the predicates by OR with the previous predicates.
	 *
	 * @param predicates
	 * 		predicates for the WHERE clause, multiple predicates will be conjunct by AND.
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> or(TinyPredicate... predicates) {

		TinyPredicate f = TinyPredicate.and(predicates);

		whereClause.append(whereClause.length() == 0 ? " WHERE " : " OR ").append(formatPredicate
				(f));
		return this;
	}

	/**
	 * Add ORDER BY clause. Multiple invocation will be added successively
	 *
	 * @param column
	 * 		column to order
	 * @param orderType
	 * 		asc or desc
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> orderBy(String column, OrderType orderType) {

		return orderBy(null, column, orderType);
	}

	/**
	 * Add ORDER BY clause for the columns from joined tables. Multiple invocation will be added
	 * successively.
	 *
	 * @param alias
	 * 		alias of the joined table
	 * @param column
	 * 		column to order
	 * @param orderType
	 * 		asc or desc
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> orderBy(String alias, String column, OrderType orderType) {

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
		orderByClause.append(".").append(column).append(" ").append(orderType);
		return this;
	}

	/**
	 * Add GROUP BY clause. Multiple invocation will be added successively.
	 * </code>
	 *
	 * @param column
	 * 		column to group
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> groupBy(String column) {

		return groupBy(null, column);
	}

	/**
	 * Add ORDER BY clause for the columns from joined tables. Multiple invocation will be added
	 * successively.
	 *
	 * @param alias
	 * 		alias of the joined table
	 * @param column
	 * 		column to order
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> groupBy(String alias, String column) {

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
		groupByClause.append(".").append(column).append(" ");
		return this;
	}
	//
	// END structural JPQL query
	////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////
	// BEGIN raw JPQL query
	//

	/**
	 * Set the JPQL query. Invoking this method will discard all precedent queries.
	 *
	 * @param jpql
	 * 		the JPQL expression
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> query(String jpql) {

		selectClause.delete(0, selectClause.length());
		selectClause.append(jpql);
		return this;
	}

	/**
	 * Add a positional parameter
	 *
	 * @param position
	 * 		the position marked in the JPQL expression.
	 * @param value
	 * 		the value of the parameter
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> param(int position, Object value) {

		positionalParameters.put(position, value);
		return this;
	}

	/**
	 * add a named parameter
	 *
	 * @param name
	 * 		the name marked in the JPQL expression.
	 * @param value
	 * 		the value of the parameter
	 *
	 * @return the same TinyQuery instance
	 */
	public TinyQuery<T> param(String name, Object value) {

		namedParameters.put(name, value);
		return this;
	}
	//
	// END raw JPQL query
	////////////////////////////////////////////////////////////////////////////////////////


	////////////////////////////////////////////////////////////////////////////////////////
	// BEGIN Result retrieving
	//

	/**
	 * Count result number.
	 *
	 * @return number of the result
	 */
	public long count() {

		selectClause.delete(0, selectClause.length());
		selectClause.append(
				String.format("SELECT count(%s) FROM %s %s",
						tableAlias, entityClass.getSimpleName(), tableAlias));
		Query query = createQuery();
		return (long) query.getSingleResult();
	}

	/**
	 * Find if query has no result
	 *
	 * @return TRUE if query has no result, FALSE otherwise.
	 */
	public boolean hasNoResult() {

		return !hasResult();
	}

	/**
	 * Find if query has result
	 *
	 * @return TRUE if query has result, FALSE otherwise.
	 */
	public boolean hasResult() {

		selectClause.delete(0, selectClause.length());
		selectClause.append(
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
	 * @return first element of the result list or null if no result.
	 */
	public T getFirstResult() {

		List<T> result = this.limit(0, 1).getResultList();
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * Limit the number of the results based on page number.Null parameters will be ignored
	 * quietly.
	 *
	 * @param page
	 * 		numbered from 1, nullable
	 * @param numberPerPage
	 * 		maximum number to retrieve, nullable
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
	 * Limit the number of the results based on row number. Null parameters will be ignored
	 * quietly.
	 *
	 * @param startRow
	 * 		position of the first result, numbered from 0, nullable
	 * @param maxRow
	 * 		maximum number to retrieve, nullable
	 *
	 * @return a predicateList of the results
	 *
	 * @see javax.persistence.Query#setFirstResult(int)
	 * @see javax.persistence.Query#setMaxResults(int)
	 */
	public TinyQuery<T> limit(Integer startRow, Integer maxRow) {

		if(startRow != null && maxRow != null) {
			this.startRow = startRow;
			this.maxRow = maxRow;
		}
		return this;
	}

	/**
	 * Execute a SELECT query and return the query results as an List.
	 *
	 * @return the typed result list
	 */
	public List<T> getResultList() {

		Query query = createQuery();
		if(startRow >= 0 && maxRow >= 0) {
			query.setFirstResult(startRow).setMaxResults(maxRow);
		}
		return query.getResultList();
	}

	/**
	 * Execute a SELECT query and return the query results as an untyped List.
	 * This method is used to retrieve result other than entity objects
	 * like aggregated value or specified columns.
	 *
	 * @return the untyped result list
	 */
	public List getUntypedResultList() {

		Query query = createQuery();
		if(startRow >= 0 && maxRow >= 0) {
			query.setFirstResult(startRow).setMaxResults(maxRow);
		}
		return query.getResultList();
	}
	//
	// END Result retrieving
	////////////////////////////////////////////////////////////////////////////////////////


	////////////////////////////////////////////////////////////////////////////////////////
	// BEGIN Internal methods
	//

	/**
	 * Create JPA query
	 *
	 * @return JPA Query object
	 */
	protected Query createQuery() {

		StringBuilder queryString = new StringBuilder();
		if(distinct) {
			//insert DISTINCT after SELECT
			selectClause.insert(6, " DISTINCT ");
		}
		queryString.append(selectClause);
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
	 * @return the JPQL expression
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder(selectClause);
		if(distinct) {
			//insert DISTINCT after SELECT
			buffer.insert(6, " DISTINCT ");
		}
		buffer.append(joinClause);
		buffer.append(whereClause);
		buffer.append(orderByClause);
		buffer.append(groupByClause);
		return buffer.toString();
	}

	/**
	 * Format recursively a predicate and all its descendants into jpql expression and inject
	 * values
	 * of the predicate into query
	 *
	 * @param predicate
	 * 		the TinyPredicate object to parse
	 *
	 * @return parsed JPQL expression
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
	//
	// END Internal methods
	////////////////////////////////////////////////////////////////////////////////////////

}
