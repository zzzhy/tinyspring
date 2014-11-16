package org.triiskelion.tinyspring.apidoc.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 17:31
 */
@Documented
@Target(value = ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParam {

	public String name();

	/**
	 * A description of what the parameter is needed for
	 *
	 * @return
	 */
	public String description() default "";


	/**
	 * An array representing the allowed values this parameter can have. Default value is *
	 *
	 * @return
	 */
	public String[] allowedValues() default { };

	/**
	 * The format from the parameter (ex. yyyy-MM-dd HH:mm:ss, ...)
	 *
	 * @return
	 */
	public String format() default "";

}
