package org.trii.tinyspring.model;

/**
 * This class represents some displayable data which needs conversion between its raw value and
 * human readable text such as state or flag value.
 */
public abstract class Displayable<T> {

	/**
	 * Original value
	 */
	protected T value;

	/**
	 * Human readable text
	 */
	protected String text;

	public Displayable(T value) {

		this.value = value;
		this.text = createText(value);
	}

	public T getValue() {

		return value;
	}

	public void setValue(T value) {

		this.value = value;
	}

	public String getText() {

		return text;
	}

	public void setText(String text) {

		this.text = text;
	}

	/**
	 * Implements this method to make conversion of the values.
	 *
	 * @param value
	 * 		original value
	 *
	 * @return human readable text
	 */
	protected abstract String createText(T value);
}
