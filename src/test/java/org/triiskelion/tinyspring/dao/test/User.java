package org.triiskelion.tinyspring.dao.test;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: December 04, 2014
 * Time: 15:11
 */
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column
	private String name;

	@Column
	private Integer sort;

	public Integer getSort() {

		return sort;
	}

	public void setSort(Integer sort) {

		this.sort = sort;
	}

	public int getId() {

		return id;
	}

	public void setId(int id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

}
