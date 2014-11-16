package org.triiskelion.tinyspring.apidoc.model;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.triiskelion.tinyspring.apidoc.annotation.ApiParam;
import org.triiskelion.tinyutils.BeanUtils;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.UUID;

public class ApiParameterDoc {

	public String id = UUID.randomUUID().toString();

	/**
	 * read from spring mvc
	 */
	private String name;

	/**
	 * read from tinyspring
	 */
	private String description;

	/**
	 * read from reflection
	 */
	private String type;

	/**
	 * read from tinyspring
	 */
	private ApiParameterType paramType;

	/**
	 * read from spring mvc
	 */
	private boolean required = true;

	private boolean allowMultipleValues = false;

	/**
	 * read from spring mvc
	 */
	private String defaultValue;

	/**
	 * read from tinyspring
	 */
	private String[] allowedValues;

	/**
	 * read from tinyspring
	 */
	private String format;

	public static ApiParameterDoc create(Parameter parameter) {

		ApiParam myAnno = parameter.getAnnotation(ApiParam.class);
		RequestHeader headerAnno = parameter.getAnnotation(RequestHeader.class);
		RequestParam paramAnno = parameter.getAnnotation(RequestParam.class);
		PathVariable pathAnno = parameter.getAnnotation(PathVariable.class);
		RequestBody bodyAnno = parameter.getAnnotation(RequestBody.class);


		if(BeanUtils.allNull(headerAnno, paramAnno, pathAnno, bodyAnno)) {
			return null;
		}

		ApiParameterDoc doc = new ApiParameterDoc();


		if(headerAnno != null) {
			doc.setParamType(ApiParameterType.HEADER);
			doc.setName(!headerAnno.value().equals("") ? headerAnno.value() : parameter.getName());
			doc.setRequired(headerAnno.required());
			doc.setDefaultValue(headerAnno.defaultValue());

		} else if(pathAnno != null) {

			doc.setParamType(ApiParameterType.PATH);
			doc.setName(!pathAnno.value().equals("") ? pathAnno.value() : parameter.getName());

		} else if(paramAnno != null) {

			doc.setParamType(ApiParameterType.QUERY);

			String name = !paramAnno.value().equals("") ? paramAnno.value() : null;
			if(name == null && myAnno != null) {
				name = myAnno.name();
			}
			doc.setName(name);
			doc.setRequired(paramAnno.required());
			doc.setDefaultValue(paramAnno.defaultValue());
		} else if(bodyAnno != null) {
			doc.setParamType(ApiParameterType.BODY);
			doc.setRequired(bodyAnno.required());
			//todo link body object
		}

		if(myAnno != null) {
			doc.setDescription(myAnno.description());
			doc.setAllowedValues(myAnno.allowedValues());
			doc.setFormat(myAnno.format());
		}
		if(parameter.getType().isArray()) {
			doc.setType(parameter.getType().getComponentType().getSimpleName());
			doc.allowMultipleValues = true;
		} else if(Collection.class.isAssignableFrom(parameter.getType())) {

			doc.allowMultipleValues = true;
			Class c
					= (Class) ((ParameterizedType) parameter.getParameterizedType())
					.getActualTypeArguments()[0];
			doc.setType(c.getSimpleName());
		}
		return doc;
	}

	public boolean getAllowMultipleValues() {

		return allowMultipleValues;
	}

	public void setAllowMultipleValues(boolean allowMultipleValues) {

		this.allowMultipleValues = allowMultipleValues;
	}

	public String getType() {

		return type;
	}

	public void setType(String type) {

		this.type = type;
	}

	public String getid() {

		return id;
	}

	public void setid(String id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public ApiParameterType getParamType() {

		return paramType;
	}

	public void setParamType(ApiParameterType paramType) {

		this.paramType = paramType;
	}

	public boolean isRequired() {

		return required;
	}

	public void setRequired(boolean required) {

		this.required = required;
	}

	public String getDefaultValue() {

		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {

		this.defaultValue = defaultValue;
	}

	public String[] getAllowedValues() {

		return allowedValues;
	}

	public void setAllowedValues(String[] allowedValues) {

		this.allowedValues = allowedValues;
	}

	public String getFormat() {

		return format;
	}

	public void setFormat(String format) {

		this.format = format;
	}


}
