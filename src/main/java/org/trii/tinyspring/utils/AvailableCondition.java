package org.trii.tinyspring.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 23, 2014
 * Time: 21:12
 */
public interface AvailableCondition<T> {

	public boolean isAvailable(T obj);

	public T getDefault();
}
