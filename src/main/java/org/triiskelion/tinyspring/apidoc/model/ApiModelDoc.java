package org.triiskelion.tinyspring.apidoc.model;

import org.triiskelion.tinyspring.apidoc.annotation.ApiModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 22:02
 */
public class ApiModelDoc {

	public String id = UUID.randomUUID().toString();

	private String name;

	private String description;

	private String type;

	private List<ApiModelFieldDoc> fields = new ArrayList<>();

	public static ApiModelDoc create(Class<?> model) {

		ApiModel anno = model.getAnnotation(ApiModel.class);
		ApiModelDoc result = new ApiModelDoc();
		result.setName(anno.name());
		result.setDescription(anno.description());
		result.setType(model.getSimpleName());
		for(Field field : model.getDeclaredFields()) {
			ApiModelFieldDoc d = ApiModelFieldDoc.create(field);
			if(d != null) {
				result.getFields().add(d);
			}
		}

		return result;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getType() {

		return type;
	}

	public void setType(String type) {

		this.type = type;
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

	public List<ApiModelFieldDoc> getFields() {

		return fields;
	}

	public void setFields(List<ApiModelFieldDoc> fields) {

		this.fields = fields;
	}


}
