package org.trii.tinyspring.viewmodel;

import java.util.List;

/**
 * Page is designed to list of result as long as the result's pagination information<br>
 * User: Sebastian MA
 * Date: July 03, 2014
 * Time: 15:52
 */
public class Page<T> {

	private long pageNumber;

	private long numberPerPage;

	private long total;

	private List<T> results;

	public Page(List<T> results, long pageNumber, long numberPerPage, long total) {

		this.pageNumber = pageNumber;
		this.numberPerPage = numberPerPage;
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
