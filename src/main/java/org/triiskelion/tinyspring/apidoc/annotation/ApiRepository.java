package org.triiskelion.tinyspring.apidoc.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 15:53
 */
@Documented
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRepository {

	/**
	 * A description of what the API does
	 *
	 * @return
	 */
	public String description();

	/**
	 * The name of the API
	 *
	 * @return
	 */
	public String name();

}
