package org.trii.tinyspring.model;

/**
 * This class represents some displayable data which needs conversion between its raw value and
 * human readable text such as state or flag value.
 *
 * @author tian
 * @version $Id: $Id
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

	/**
	 * <p>Constructor for Displayable.</p>
	 *
	 * @param value a T object.
	 */
	public Displayable(T value) {

		this.value = value;
		this.text = createText(value);
	}

	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a T object.
	 */
	public T getValue() {

		return value;
	}

	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a T object.
	 */
	public void setValue(T value) {

		this.value = value;
	}

	/**
	 * <p>Getter for the field <code>text</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getText() {

		return text;
	}

	/**
	 * <p>Setter for the field <code>text</code>.</p>
	 *
	 * @param text a {@link java.lang.String} object.
	 */
	public void setText(String text) {

		this.text = text;
	}

	/**
	 * Implements this method to make conversion of the values.
	 *
	 * @param value
	 * 		original value
	 * @return human readable text
	 */
	protected abstract String createText(T value);
}
