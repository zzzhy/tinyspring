package org.trii.tinyspring.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 23, 2014
 * Time: 21:00
 */
public class BeanUtils {

	public static <T> Map<String, Set<Object>> distinctPropertyValues(List<T> list,
	                                                          String... names) throws
			NoSuchFieldException, IllegalAccessException, NoSuchMethodException,
			InvocationTargetException {

		Map<String, Set<Object>> map = new HashMap<>();
		for(T t : list) {
			for(String name : names) {
				if(map.get(name) == null) {
					map.put(name, new HashSet<>());
				}
				map.get(name).add(
						t.getClass().getDeclaredMethod("get" + name.substring(0,
								1).toUpperCase() + name.substring(1)).invoke(t));
			}
		}
		return map;
	}

	public static <T> T firstAvailable(AvailableCondition<T> ac, T... objects) {

		if(objects == null || objects.length == 0) {
			return null;
		}

		for(T obj : objects) {
			if(ac.isAvailable(obj)) {
				return obj;
			}
		}
		return null;
	}

	public static String firstAvailable(String... objects) {

		if(objects == null || objects.length == 0) {
			return null;
		}

		AvailableCondition<String> ac = new AvailableCondition<String>() {

			@Override
			public boolean isAvailable(String obj) {

				return obj != null && !obj.trim().isEmpty();
			}

			@Override
			public String getDefault() {

				return "";
			}
		};
		for(String obj : objects) {
			if(ac.isAvailable(obj)) {
				return obj;
			}
		}
		return ac.getDefault();
	}

	public static <T> T cherryPick(CherryPicker picker, T... objects) {

		if(objects == null || objects.length == 0) {
			return null;
		}

		for(T obj : objects) {

		}
		return null;
	}

	public static String cherryPick(String... objects) {

		if(objects == null || objects.length == 0) {
			return null;
		}

		for(String obj : objects) {

		}
		return null;
	}
}
