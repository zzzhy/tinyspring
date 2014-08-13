package org.trii.tinyspring.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 11, 2014
 * Time: 15:40
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityCheck {

	/**
	 * default value is true. set to false if you wish to override class-level annotation. only for
	 * method level.
	 *
	 * @return
	 */
	boolean value() default true;

	String[] requireRole() default { };

	String requirePrivilege() default "";

	/**
	 * matched path will be checked unless it is excluded. only for class level.
	 *
	 * @return
	 */
	String[] matches() default { };

	/**
	 * matched path will be excluded thus will NOT be checked. only for class level.
	 *
	 * @return
	 */
	String[] excludes() default { };


}
