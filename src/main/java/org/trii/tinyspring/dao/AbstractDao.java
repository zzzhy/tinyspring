package org.trii.tinyspring.dao;

import org.springframework.beans.factory.annotation.Value;
import org.trii.tinyspring.AbstractSpringBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: February 23, 2014
 * Time: 11:52
 */
public abstract class AbstractDao<T> extends AbstractSpringBean {

	@PersistenceContext
	protected EntityManager entityManager;

	@Value("${tinyspring.dao.showJPQL:false}")
	Boolean showJpql;

	protected EntityManager getEntityManager() {

		return entityManager;
	}

	/**
	 * MUST be implemented by concrete dao class to inject class object.
	 * Because java's generic type could not be obtained at runtime.
	 *
	 * @return
	 */
	abstract protected Class<T> getEntityClass();

	public T findById(Object id) {

		return getEntityManager().find(getEntityClass(), id);
	}

	public void persist(T entity) {

		getEntityManager().persist(entity);
	}

	public T merge(T entity) {

		return getEntityManager().merge(entity);
	}

	public void remove(T entity) {

		getEntityManager().remove(entity);
	}

	public long totalCount() {

		return this.beginQuery().select().count();
	}

	public List<T> findAll() {

		return this.beginQuery().select().getResultList();
	}

	@SuppressWarnings("unchecked")
	public List getColumnValues(String targetColumn, TinyPredicate... predicates) {

		String[] columnList = null;
		TinyQuery query = beginQuery().select(targetColumn);
		if(predicates != null && predicates.length > 0) {
			query.where(predicates);
		}
		query.groupBy(targetColumn);
		return query.getUntypedResultList();
	}

	public TinyQuery<T> beginQuery() {

		return new TinyQuery<>(getEntityManager(), getEntityClass(), showJpql);
	}


}
