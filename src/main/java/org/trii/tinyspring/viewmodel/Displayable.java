package org.trii.tinyspring.viewmodel;

/**
 * Displayable is designed to wrap enumerated value and its human readable meaning together.<br>
 * This is very convenient for the view layer as in view layer both text and value are required
 * .<br>
 * For projects with no i18n requirement the Displayable can take role of the interpreter by
 * implementing <code>createText()</code>
 * User: Sebastian
 * Date: 2014/7/10
 * Time: 16:53
 */
public abstract class Displayable<T> {

	protected T value;

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

	protected abstract String createText(T value);
}
