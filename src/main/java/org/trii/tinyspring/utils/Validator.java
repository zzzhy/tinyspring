package org.trii.tinyspring.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * User: tian
 * Date: 14/2/14
 * Time: 15:19
 */
public class Validator {


	/**
	 * validates if paramter is location
	 *
	 * @param location
	 * 		comma-delimited two double values
	 *
	 * @return true if location is valid location
	 */
	public static boolean isGeolocation(String location) {

		return !StringUtils.isBlank(location) && location.matches("\\d*\\.\\d,\\d*\\.\\d");
	}

	/**
	 * Validates if a string is
	 * @param string
	 * @param separator
	 * @return
	 */
	public static boolean containsOnlyDouble(String string, String separator) {

		try {
			String[] splitted = string.split(separator);

			for(String str : splitted) {
				if(!str.matches("[0-9]*\\.[0-9]")) {
					return false;
				}
			}
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	/**
	 * valid email address. Null pointer safe.
	 *
	 * @param email
	 * 		may be null
	 */
	public static boolean isEmail(String email) {

		return StringUtils.isNotBlank(email) && email.matches(".*@.*\\..*");
	}

	/**
	 * valid chinese mobile number an optional country code of 0086 or +86 followed by an 11-digit
	 * number. Null pointer safe.
	 *
	 * @param number
	 * 		may be null
	 */
	public static boolean isMobileNumber(String number) {

		return StringUtils.isNotBlank(number) && number.matches("(((00)|\\+)86)?[0-9]{11}");
	}

	/**
	 * Strict 11-digit mobile number. country code is not allowed.
	 */
	public static boolean isStrictMobileNumber(String number) {

		return StringUtils.isNotBlank(number) && number.matches("1[0-9]{10}");
	}

	public static String getEncoding(String str) {

		String encode;
		encode = "UTF-8";
		try {
			if(str.equals(new String(str.getBytes(encode), encode))) {
				return encode;
			}
		} catch(Exception exception2) {
		}
		encode = "GB2312";
		try {
			if(str.equals(new String(str.getBytes(encode), encode))) {
				return encode;
			}
		} catch(Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if(str.equals(new String(str.getBytes(encode), encode))) {
				return encode;
			}
		} catch(Exception exception1) {
		}

		encode = "GBK";
		try {
			if(str.equals(new String(str.getBytes(encode), encode))) {
				return encode;
			}
		} catch(Exception exception3) {
		}
		return "";
	}

}
