package org.trii.tinyspring.model;

import java.util.List;

/**
 * A generic pagination model which holds page information and the list of result for the page to
 * display.
 *
 * @author tian
 * @version $Id: $Id
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

	/**
	 * <p>Constructor for Page.</p>
	 */
	public Page() {

	}

	/**
	 * <p>Constructor for Page.</p>
	 *
	 * @param pageNumber a int.
	 * @param results a {@link java.util.List} object.
	 * @param total a long.
	 */
	public Page(int pageNumber, List<T> results, long total) {

		this.pageNumber = pageNumber;
		this.results = results;
		this.total = total;
	}

	/**
	 * <p>Getter for the field <code>pageNumber</code>.</p>
	 *
	 * @return a int.
	 */
	public int getPageNumber() {

		return pageNumber;
	}

	/**
	 * <p>Setter for the field <code>pageNumber</code>.</p>
	 *
	 * @param pageNumber a int.
	 */
	public void setPageNumber(int pageNumber) {

		this.pageNumber = pageNumber;
	}

	/**
	 * <p>Getter for the field <code>total</code>.</p>
	 *
	 * @return a long.
	 */
	public long getTotal() {

		return total;
	}

	/**
	 * <p>Setter for the field <code>total</code>.</p>
	 *
	 * @param total a long.
	 */
	public void setTotal(long total) {

		this.total = total;
	}

	/**
	 * <p>Getter for the field <code>results</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<T> getResults() {

		return results;
	}

	/**
	 * <p>Setter for the field <code>results</code>.</p>
	 *
	 * @param results a {@link java.util.List} object.
	 */
	public void setResults(List<T> results) {

		this.results = results;
	}
}
