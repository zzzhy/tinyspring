package org.triiskelion.tinyspring.apidoc.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 15:56
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiEntry {

	public String value();

	/**
	 * A description of what the method does
	 *
	 * @return
	 */
	public String description();
}
