package org.triiskelion.tinyspring.apidoc.model;


import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 16:13
 */
public class ApiDocumentation {

	String version;

	String path;

	private List<ApiRepositoryDoc> repositories;

	private Map<String, ApiModelDoc> models;

	public Map<String, ApiModelDoc> getModels() {

		return models;
	}

	public void setModels(Map<String, ApiModelDoc> models) {

		this.models = models;
	}

	public ApiDocumentation(String version, String path) {

		this.version = version;
		this.path = path;
	}

	public String getVersion() {

		return version;
	}

	public void setVersion(String version) {

		this.version = version;
	}

	public String getPath() {

		return path;
	}

	public void setPath(String path) {

		this.path = path;
	}

	public List<ApiRepositoryDoc> getRepositories() {

		return repositories;
	}

	public void setRepositories(List<ApiRepositoryDoc> repositories) {

		this.repositories = repositories;
	}

	//private Set<ApiObjectDoc> objects;
}
