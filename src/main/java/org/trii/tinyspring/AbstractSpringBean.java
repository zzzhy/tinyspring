package org.trii.tinyspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created with IntelliJ IDEA.
 * User: tian
 * Date: 16/2/14
 * Time: 18:06
 */
public abstract class AbstractSpringBean {

	protected static Logger log;

	@PostConstruct
	protected void postConstruct() {

		log = LoggerFactory.getLogger(this.getClass().getSimpleName());
	}

}
