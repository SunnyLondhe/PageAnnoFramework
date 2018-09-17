package edu.srh.api.json;

import org.openrdf.repository.RepositoryException;

import com.github.anno4j.model.Annotation;

import edu.srh.exceptions.AnnotationExceptions;
import edu.srh.jsoncore.AnnotationJson;
import edu.srh.util.PropertyHandler;

/**
 * 
 * @author SunnyLondhe
 * This interface is used to map single json object to JSON POJO classes from edu.srh.api.json.*
 * AannotationJson.java is the root class. further this classes are converted to WADM using Anno4j Classes.
 */
public interface JsonMapper {

	String ServiceURI = PropertyHandler.instance().serviceURL;
	String annotationURL = PropertyHandler.instance().baseURL;
	
	String parseJson(String content)  throws RepositoryException, IllegalAccessException, InstantiationException, AnnotationExceptions ;

	Annotation convertJsonToAnnotation(AnnotationJson jsonObj)  throws RepositoryException, IllegalAccessException, InstantiationException, AnnotationExceptions;
	
	Annotation createAnnotations(AnnotationJson jsonObj) throws AnnotationExceptions;
}
