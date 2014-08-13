package org.trii.tinyspring.security;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: June 21, 2014
 * Time: 12:25
 * 权限功能表
 */
public class PrivilegeSet {

	String id;

	String name;

	String description;

	HashMap<String, PrivilegeItem> items = new HashMap<>();

	HashMap<String, PrivilegeSet> subsets = new HashMap<>();

	public PrivilegeSet() {

	}

	public PrivilegeSet(String id, String name, String description) {

		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public HashMap<String, PrivilegeItem> getItems() {

		return items;
	}

	public void setItems(HashMap<String, PrivilegeItem> items) {

		this.items = items;
	}

	public HashMap<String, PrivilegeSet> getSubsets() {

		return subsets;
	}

	public void setSubsets(HashMap<String, PrivilegeSet> subsets) {

		this.subsets = subsets;
	}
}
