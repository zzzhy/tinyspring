package org.trii.tinyspring.security;


import org.trii.tinyspring.AbstractSpringBean;

import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 11, 2014
 * Time: 14:00
 */
public abstract class TinyAuthenticator extends AbstractSpringBean {

	public static final String SESSION_NAME_USER = "TINY_USER_OBJECT";

	protected String logoutSuccessUrl;

	protected String logoutFailureUrl;

	protected String loginSuccessUrl;

	protected String loginFailureUrl;

	public abstract String authenticate(HttpSession session, String username, String password);


	public void setLogoutSuccessUrl(String logoutSuccessUrl) {

		this.logoutSuccessUrl = logoutSuccessUrl;
	}

	public String getLogoutFailureUrl() {

		return logoutFailureUrl;
	}

	public void setLogoutFailureUrl(String logoutFailureUrl) {

		this.logoutFailureUrl = logoutFailureUrl;
	}

	public String getLoginSuccessUrl() {

		return loginSuccessUrl;
	}

	public void setLoginSuccessUrl(String loginSuccessUrl) {

		this.loginSuccessUrl = loginSuccessUrl;
	}

	public String getLoginFailureUrl() {

		return loginFailureUrl;
	}

	public void setLoginFailureUrl(String loginFailureUrl) {

		this.loginFailureUrl = loginFailureUrl;
	}

}
