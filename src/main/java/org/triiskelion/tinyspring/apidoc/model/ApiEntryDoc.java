package org.triiskelion.tinyspring.apidoc.model;

import com.google.common.reflect.Invokable;
import com.google.common.reflect.Parameter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.triiskelion.tinyspring.apidoc.Utils;
import org.triiskelion.tinyspring.apidoc.annotation.ApiEntry;
import org.triiskelion.tinyutils.BeanUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 16:00
 */
public class ApiEntryDoc {

	String id = UUID.randomUUID().toString();

	String[] path;

	String name = "";

	String description = "";

	String[] requestMethods;

	private String[] produces;

	private String[] consumes;

	private String[] headers;

	private List<ApiParameterDoc> queryParameters = new ArrayList<>();

	private List<ApiParameterDoc> pathParameters = new ArrayList<>();

	private List<ApiParameterDoc> headerParameters = new ArrayList<>();

	private ApiParameterDoc bodyParameter;

	// private ApiBodyObjectDoc bodyobject;

	private boolean isResponseArray = false;


	private String responseType;

	private String responseTypeRef;


	// private List<ApiErrorDoc> apierrors;

	public static ApiEntryDoc create(Method method) {

		RequestMapping mvcAnnotation = method.getAnnotation(RequestMapping.class);
		ApiEntry myAnnotation = method.getAnnotation(ApiEntry.class);
		ResponseBody responseBodyAnno = method.getAnnotation(ResponseBody.class);

		if(BeanUtils.allNull(mvcAnnotation, myAnnotation)) {
			return null;
		}

		ApiEntryDoc doc = new ApiEntryDoc();

		//todo  handle placeholder
		if(myAnnotation != null) {
			doc.name = myAnnotation.value();
			doc.description = myAnnotation.description();
		}
		doc.path = mvcAnnotation.value();
		doc.requestMethods = Utils.convert(mvcAnnotation.method());
		doc.produces = mvcAnnotation.produces();
		doc.consumes = mvcAnnotation.consumes();
		doc.headers = mvcAnnotation.headers();

		for(Parameter parameter : Invokable.from(method).getParameters()) {
			ApiParameterDoc parameterDoc = ApiParameterDoc.create(parameter);
			if(parameterDoc != null) {
				switch(parameterDoc.getParamType()) {
					case PATH:
						doc.getPathParameters().add(parameterDoc);
						break;
					case QUERY:
						doc.getQueryParameters().add(parameterDoc);
						break;
					case HEADER:
						doc.getHeaderParameters().add(parameterDoc);
						break;
					case BODY:
						doc.setBodyParameter(parameterDoc);
						break;
				}
			}
		}

		// handle response
		if(responseBodyAnno != null) {
			Class returnType = null;
			if(method.getReturnType().isArray()) {
				doc.isResponseArray = true;
				returnType = method.getReturnType().getComponentType();

			} else if(Collection.class.isAssignableFrom(method.getReturnType())) {
				Type returnGenericType = method.getGenericReturnType();
				doc.isResponseArray = true;

				if(returnGenericType instanceof ParameterizedType) {
					returnType = (Class) ((ParameterizedType) returnGenericType)
							.getActualTypeArguments()[0];
					doc.responseType = returnType.getSimpleName();
				}
			} else {
				returnType = method.getReturnType();
				doc.responseType = returnType.getSimpleName();
			}
		}
		return doc;
	}

	public String getResponseTypeRef() {

		return responseTypeRef;
	}

	public void setResponseTypeRef(String responseTypeRef) {

		this.responseTypeRef = responseTypeRef;
	}

	public boolean getIsResponseArray() {

		return isResponseArray;
	}

	public void setIsResponseArray(boolean isResponseArray) {

		this.isResponseArray = isResponseArray;
	}

	public String getResponseType() {

		return responseType;
	}

	public void setResponseType(String responseType) {

		this.responseType = responseType;
	}

	public List<ApiParameterDoc> getHeaderParameters() {

		return headerParameters;
	}

	public void setHeaderParameters(List<ApiParameterDoc> headerParameters) {

		this.headerParameters = headerParameters;
	}

	public String[] getRequestMethods() {

		return requestMethods;
	}

	public String[] getProduces() {

		return produces;
	}

	public String[] getConsumes() {

		return consumes;
	}

	public String[] getPath() {

		return path;
	}

	public void setPath(String[] path) {

		this.path = path;
	}

	public void setRequestMethods(String[] requestMethods) {

		this.requestMethods = requestMethods;
	}

	public void setProduces(String[] produces) {

		this.produces = produces;
	}

	public void setConsumes(String[] consumes) {

		this.consumes = consumes;
	}

	public String[] getHeaders() {

		return headers;
	}

	public void setHeaders(String[] headers) {

		this.headers = headers;
	}

	public ApiParameterDoc getBodyParameter() {

		return bodyParameter;
	}

	public void setBodyParameter(ApiParameterDoc bodyParameter) {

		this.bodyParameter = bodyParameter;
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


	public List<ApiParameterDoc> getPathParameters() {

		return pathParameters;
	}

	public void setPathParameters(List<ApiParameterDoc> pathParameters) {

		this.pathParameters = pathParameters;
	}

	public List<ApiParameterDoc> getQueryParameters() {

		return queryParameters;
	}

	public void setQueryParameters(List<ApiParameterDoc> queryParameters) {

		this.queryParameters = queryParameters;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}
}
