package org.triiskelion.tinyspring.dao.test;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: December 04, 2014
 * Time: 15:11
 */
@Entity
@Table(name = "dummy")
public class DummyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column
	private String name;


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
