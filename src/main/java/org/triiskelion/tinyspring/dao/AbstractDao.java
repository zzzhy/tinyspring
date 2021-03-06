package org.triiskelion.tinyspring.dao;

import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.triiskelion.tinyspring.AbstractSpringBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * A generic DAO class which provide essential query methods as long as
 * a query builder to help build jpql queries easier.
 *
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

	/**
	 * Override this method to use your own entity manager.
	 *
	 * @return
	 */
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

	public Optional<T> findById(Object id) {

		try {
			return Optional.fromNullable(getEntityManager().find(getEntityClass(), id));
		} catch(Exception e) {
			log.error("findById() failed. CAUSE:{}", e.getMessage());
			return Optional.absent();
		}
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

	public void removeById(Object id) {

		try {
			Optional<T> result = findById(id);
			if(result.isPresent()) {
				remove(result.get());
			}
		} catch(Exception e) {
			log.error("removeById() failed. Cause:{}", e.getMessage());
		}
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
