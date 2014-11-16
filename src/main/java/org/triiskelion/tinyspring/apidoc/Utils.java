package org.triiskelion.tinyspring.apidoc;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 17:02
 */
public class Utils {

	public static String[] convert(RequestMethod[] methods) {

		String[] result = new String[methods.length];
		for(int i = 0; i < methods.length; i++) {
			result[i] = methods[i].toString();
		}
		return result;
	}
}
