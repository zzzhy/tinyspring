package org.triiskelion.tinyspring.apidoc.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 23:12
 */
@Documented
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelField {

	/**
	 * A drescription of what the field is
	 */
	public String description();

	/**
	 * The format pattern for this field
	 *
	 * @return
	 */
	public String format() default "";

	/**
	 * The allowed values for this field
	 *
	 * @return
	 */
	public String[] allowedValues() default { };
}
