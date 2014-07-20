package org.trii.tinyspring.model;

import java.util.List;

/**
 * A generic pagination model which holds page information and the list of result for the page to
 * display.
 */
public class Page<T> {

	/**
	 * page number start from 1
	 */
	private int pageNumber;

	/**
	 * total number of the result
	 */
	private long total;

	/**
	 * the result for the page
	 */
	private List<T> results;

	public Page() {

	}

	public Page(int pageNumber, List<T> results, long total) {

		this.pageNumber = pageNumber;
		this.results = results;
		this.total = total;
	}

	public int getPageNumber() {

		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {

		this.pageNumber = pageNumber;
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
