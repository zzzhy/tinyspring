package org.triiskelion.tinyspring.viewmodel;

import java.util.List;

/**
 * Page is designed to list of result as long as the result's pagination information<br>
 * User: Sebastian MA
 * Date: July 03, 2014
 * Time: 15:52
 */
public class Page<T> {

	private long page;

	private long max;

	private long total;

	private long totalPage;

	private long dataSize;

	private List<T> data;

	public Page(List<T> data, long page, long max, long total) {

		this.page = page;
		this.max = max;
		this.total = total;
		this.totalPage = total / max + (total % max > 0 ? 1 : 0);
		this.data = data;
		this.dataSize = data != null ? data.size() : 0;
	}

	public long getTotalPage() {

		return totalPage;
	}

	public void setTotalPage(long totalPage) {

		this.totalPage = totalPage;
	}

	public long getDataSize() {

		return dataSize;
	}

	public void setDataSize(long dataSize) {

		this.dataSize = dataSize;
	}

	public long getPage() {

		return page;
	}

	public void setPage(long page) {

		this.page = page;
	}

	public long getMax() {

		return max;
	}

	public void setMax(long max) {

		this.max = max;
	}

	public long getTotal() {

		return total;
	}

	public void setTotal(long total) {

		this.total = total;
	}

	public List<T> getData() {

		return data;
	}

	public void setData(List<T> data) {

		this.data = data;
	}
}
