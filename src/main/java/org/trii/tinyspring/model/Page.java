package org.trii.tinyspring.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: July 03, 2014
 * Time: 15:52
 */
public class Page<T> {

	private long total;

	private List<T> results;

	public Page() {

	}

	public Page(List<T> results, long total) {

		this.results = results;
		this.total = total;
	}

	public long getTotal() {

		return total;
	}

	public void setTotal(long total) {

		this.total = total;
	}

	public List<T> getResults() {

		return results;
	}

	public void setResults(List<T> results) {

		this.results = results;
	}
}
