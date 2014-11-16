package org.triiskelion.tinyspring.apidoc;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.triiskelion.tinyspring.apidoc.annotation.ApiModel;
import org.triiskelion.tinyspring.apidoc.annotation.ApiRepository;
import org.triiskelion.tinyspring.apidoc.model.ApiDocumentation;
import org.triiskelion.tinyspring.apidoc.model.ApiModelDoc;
import org.triiskelion.tinyspring.apidoc.model.ApiModelFieldDoc;
import org.triiskelion.tinyspring.apidoc.model.ApiRepositoryDoc;

import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 16:15
 */
public class ApiScanner {

	private static Logger log = LoggerFactory.getLogger(ApiScanner.class);

	public static HashMap<String, String> apiModelMap = new HashMap<>();

	public static ApiDocumentation scan(String version, String path, List<String> packages) {

		Set<URL> urls = new HashSet<URL>();
		FilterBuilder filter = new FilterBuilder();

		log.debug("Scanning {} package(s)...", packages.size());
		for(String pkg : packages) {
			urls.addAll(ClasspathHelper.forPackage(pkg));
			filter.includePackage(pkg);
		}

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.filterInputsBy(filter).setUrls(urls));

		ApiDocumentation apiDoc = new ApiDocumentation(version, path);
		apiDoc.setRepositories(scanApiRepository(reflections.getTypesAnnotatedWith
				(ApiRepository.class)));
		apiDoc.setModels(scanApiModel(reflections.getTypesAnnotatedWith
				(ApiModel.class)));
		return apiDoc;
	}

	private static Map<String, ApiModelDoc> scanApiModel(Set<Class<?>> models) {

		HashMap<String, ApiModelDoc> result = new HashMap<>();
		for(Class<?> model : models) {
			log.debug("Scanning class: " + model.getName());
			ApiModelDoc modelDoc = ApiModelDoc.create(model);
			if(modelDoc != null) {
				result.put(modelDoc.getId(), modelDoc);
				apiModelMap.put(modelDoc.getType(), modelDoc.getId());
			}
		}
		for(ApiModelDoc doc : result.values()) {
			for(ApiModelFieldDoc fieldDoc : doc.getFields()) {
				fieldDoc.setTypeRef(apiModelMap.get(fieldDoc.getType()));
			}
		}
		return result;
	}

	private static List<ApiRepositoryDoc> scanApiRepository(Set<Class<?>> controllers) {

		List<ApiRepositoryDoc> result = new ArrayList<>();
		for(Class<?> controller : controllers) {
			log.debug("Scanning class: " + controller.getName());
			ApiRepositoryDoc repo = ApiRepositoryDoc.create(controller);
			if(repo != null) {
				result.add(repo);
			}
		}
		return result;
	}
}
