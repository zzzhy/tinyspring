package org.trii.tinyspring.dao;

import org.trii.tinyspring.AbstractSpringBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: February 23, 2014
 * Time: 11:52
 *
 * @author tian
 * @version $Id: $Id
 */
public abstract class AbstractDao<T> extends AbstractSpringBean {

	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * <p>Getter for the field <code>entityManager</code>.</p>
	 *
	 * @return a {@link javax.persistence.EntityManager} object.
	 */
	protected EntityManager getEntityManager() {

		return entityManager;
	}

	/**
	 * MUST be implemented by concrete dao class to inject class object.
	 * Because java's generic type could not be obtained at runtime.
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	abstract protected Class<T> getEntityClass();

	/**
	 * <p>findById.</p>
	 *
	 * @param id a {@link java.lang.Object} object.
	 * @return a T object.
	 */
	public T findById(Object id) {

		return getEntityManager().find(getEntityClass(), id);
	}

	/**
	 * <p>persist.</p>
	 *
	 * @param entity a T object.
	 */
	public void persist(T entity) {

		getEntityManager().persist(entity);
	}

	/**
	 * <p>merge.</p>
	 *
	 * @param entity a T object.
	 * @return a T object.
	 */
	public T merge(T entity) {

		return getEntityManager().merge(entity);
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param entity a T object.
	 */
	public void remove(T entity) {

		getEntityManager().remove(entity);
	}

	/**
	 * <p>totalCount.</p>
	 *
	 * @return a long.
	 */
	public long totalCount() {

		return this.beginQuery().select().count();
	}

	/**
	 * <p>findAll.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<T> findAll() {

		return this.beginQuery().select().getResultList();
	}

	/**
	 * <p>beginQuery.</p>
	 *
	 * @return a {@link org.trii.tinyspring.dao.TinyQuery} object.
	 */
	public TinyQuery<T> beginQuery() {

		return new TinyQuery<>(getEntityManager(), getEntityClass());
	}


}
