package org.trii.tinyspring.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: July 04, 2014
 * Time: 9:17
 */
public class Converter {

	public static int[] toIntArray(String string, String separatorChars) {

		return toIntArray(StringUtils.split(string, separatorChars));
	}

	public static int[] toIntArray(String[] array) {

		int[] result = new int[array.length];
		for(int i = 0; i < array.length; i++) {
			result[i] = Integer.parseInt(array[i]);
		}
		return result;
	}

	public static Integer[] toIntegerArray(String string, String separatorChars) {

		return toIntegerArray(StringUtils.split(string, separatorChars));
	}

	public static Integer[] toIntegerArray(String[] array) {

		Integer[] result = new Integer[array.length];
		for(int i = 0; i < array.length; i++) {
			result[i] = Integer.valueOf(array[i]);
		}
		return result;
	}
}
