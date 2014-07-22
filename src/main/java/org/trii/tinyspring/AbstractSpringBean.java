package org.trii.tinyspring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created with IntelliJ IDEA.
 * User: tian
 * Date: 16/2/14
 * Time: 18:06
 *
 * @author tian
 * @version $Id: $Id
 */
public abstract class AbstractSpringBean {

	/** Constant <code>log</code> */
	protected static Logger log;

	/**
	 * <p>postConstruct.</p>
	 */
	@PostConstruct
	protected void postConstruct() {

		log = LoggerFactory.getLogger(this.getClass().getSimpleName());
	}

}
