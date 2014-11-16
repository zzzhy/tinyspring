package org.triiskelion.tinyspring.apidoc.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 23:11
 */
@Documented
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModel {

	public String name() default "";

	public String description() default "";

}
