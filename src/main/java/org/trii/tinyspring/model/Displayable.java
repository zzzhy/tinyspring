package org.trii.tinyspring.model;

/**
 * Created with IntelliJ IDEA.
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
