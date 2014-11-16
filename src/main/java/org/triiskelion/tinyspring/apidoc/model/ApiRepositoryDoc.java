package org.triiskelion.tinyspring.apidoc.model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.triiskelion.tinyspring.apidoc.annotation.ApiRepository;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 16:31
 */
public class ApiRepositoryDoc {

	String name;

	String description;

	String[] path;

	private List<ApiEntryDoc> entries = new ArrayList<>();

	private int entryCount;

	public static ApiRepositoryDoc create(Class<?> controller) {

		ApiRepositoryDoc repositoryDoc = new ApiRepositoryDoc();

		RequestMapping mvcAnnotation = controller.getAnnotation(RequestMapping.class);
		ApiRepository myAnnotation = controller.getAnnotation(ApiRepository.class);
		repositoryDoc.setName(myAnnotation.name());
		repositoryDoc.setDescription(myAnnotation.description());
		repositoryDoc.setPath(mvcAnnotation.value());

		Method[] methods = controller.getMethods();
		for(Method method : methods) {
			ApiEntryDoc entryDoc = ApiEntryDoc.create(method);
			if(entryDoc != null) {
				repositoryDoc.getEntries().add(entryDoc);
			}
		}
		repositoryDoc.entryCount = repositoryDoc.entries.size();
		return repositoryDoc;

	}

	public int getEntryCount() {

		return entryCount;
	}

	public void setEntryCount(int entryCount) {

		this.entryCount = entryCount;
	}

	public List<ApiEntryDoc> getEntries() {

		return entries;
	}

	public void setEntries(List<ApiEntryDoc> entries) {

		this.entries = entries;
	}

	public String[] getPath() {

		return path;
	}

	public void setPath(String[] path) {

		this.path = path;
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
}
