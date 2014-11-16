package org.triiskelion.tinyspring.apidoc.model;

import org.triiskelion.tinyspring.apidoc.annotation.ApiModelField;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 22:03
 */
public class ApiModelFieldDoc {

	public String id = UUID.randomUUID().toString();

	private String name;

	private String type;

	private String typeRef;

	private boolean isArray = false;

	private String description;

	private String format;

	private String[] allowedValues;


	public static ApiModelFieldDoc create(Field field) {

		ApiModelField anno = field.getAnnotation(ApiModelField.class);
		ApiModelFieldDoc result = new ApiModelFieldDoc();
		result.setName(field.getName());

		if(field.getType().isArray()) {
			result.isArray = true;
			result.setType(field.getType().getComponentType().getSimpleName());
		} else if(Collection.class.isAssignableFrom(field.getType())) {

			result.isArray = true;
			Type returnGenericType = field.getGenericType();

			if(returnGenericType instanceof ParameterizedType) {
				Class innerType = (Class) ((ParameterizedType) returnGenericType)
						.getActualTypeArguments()[0];
				result.type = innerType.getSimpleName();
			}
		} else if(Map.class.isAssignableFrom(field.getType())) {
			result.type = "Map";
		} else {
			result.type = field.getType().getSimpleName();
		}

		if(anno != null) {
			result.setDescription(anno.description());
			result.setFormat(anno.format());
			result.setAllowedValues(anno.allowedValues());
		}

		return result;
	}

	public String getTypeRef() {

		return typeRef;
	}

	public void setTypeRef(String typeRef) {

		this.typeRef = typeRef;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getType() {

		return type;
	}

	public void setType(String type) {

		this.type = type;
	}

	public boolean getIsArray() {

		return isArray;
	}

	public void setIsArray(boolean isArray) {

		this.isArray = isArray;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public String getFormat() {

		return format;
	}

	public void setFormat(String format) {

		this.format = format;
	}

	public String[] getAllowedValues() {

		return allowedValues;
	}

	public void setAllowedValues(String[] allowedValues) {

		this.allowedValues = allowedValues;
	}


}
