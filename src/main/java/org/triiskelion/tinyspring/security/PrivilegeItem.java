package org.triiskelion.tinyspring.security;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: June 22, 2014
 * Time: 9:39
 * 权限功能单项
 */
public class PrivilegeItem {

	String id;

	String name;

	String description;

	int value;

	public PrivilegeItem() {

	}

	public PrivilegeItem(String id, String name, String description, int value) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.value = value;
	}

	public PrivilegeItem(PrivilegeItem clone) {

		this.id = clone.id;
		this.name = clone.name;
		this.description = clone.description;
		this.value = clone.value;
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

	public int getValue() {

		return value;
	}

	public void setValue(int value) {

		this.value = value;
	}
}
