package edu.srh.api.page;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.impl.targets.SpecificResource;

import edu.srh.exceptions.AnnotationExceptions;
import edu.srh.pagexml.MetadataType;
import edu.srh.pagexml.PageType;
import edu.srh.pagexml.PcGtsType;
import edu.srh.pagexml.RegionType;
import edu.srh.util.PropertyHandler;

public interface PageAnnotationGenerator {

	String ServiceURI = PropertyHandler.instance().serviceURL;
	String annotationURL = PropertyHandler.instance().baseURL;

	public String parseAnnotations(String digitalObjID, String annotationString) throws AnnotationExceptions;

	String postToJenaStore();

	void processXML(PcGtsType pcgtsTypeObj, String digitalObjID, String softAgentResourceID)
			throws AnnotationExceptions;

	List<Model> createOtherAnnotations(PcGtsType pcgtsTypeObj, String softAgentResourceID, String digitalObjID)
			throws AnnotationExceptions;

	Model createOtherBodyTarget(Annotation annoations, RegionType regions, String digitalObjID)
			throws AnnotationExceptions;

	String getRegionType(RegionType regionType);

	Model createPageAnnotation(PcGtsType pcgtsTypeObj, String softAgentResourceID, String digitalObjID)
			throws AnnotationExceptions;

	Model createBodyTarget(Annotation annoations, PageType page, String digitalObjID) throws AnnotationExceptions;

	SpecificResource getTarget(String points, String digitalObjID) throws AnnotationExceptions;

	Annotation createAnnoataionPart(MetadataType metadata, String softAgentResourceID) throws AnnotationExceptions;

	String checkAgentInRegistry(MetadataType metadata) throws AnnotationExceptions;

	Model getJenaModel(String triples);
}
