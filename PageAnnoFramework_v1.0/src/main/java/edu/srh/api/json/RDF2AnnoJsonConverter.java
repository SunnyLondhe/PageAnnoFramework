package edu.srh.api.json;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.srh.rdfcore.ChoiceType;
import edu.srh.rdfcore.CreationProvenanceType;
import edu.srh.rdfcore.CreatorType;
import edu.srh.rdfcore.EmbeddedContentType;
import edu.srh.rdfcore.ExternalWebResourceType;
import edu.srh.rdfcore.HasBodyType;
import edu.srh.rdfcore.HasSelector;
import edu.srh.rdfcore.HasTarget;
import edu.srh.rdfcore.ItemsType;
import edu.srh.rdfcore.RDFtype;
import edu.srh.rdfcore.ResourceBodyType;

public interface RDF2AnnoJsonConverter {

	String parse(String xmlStr);

	String convertAnnoJson(RDFtype rdf);

	JSONObject getTarget(HasTarget hasTarget);
	
	JSONObject getSelector(HasSelector hasSelector);
	
	JSONArray getBody(List<HasBodyType> bodyList);
	
	JSONArray createResource(ResourceBodyType resource);
	
	JSONArray createChoice(ChoiceType choice);
	
	JSONArray createCreationProvenance(CreationProvenanceType creationProvenance);
	
	JSONArray createExternalWebResource(ExternalWebResourceType externalWebResource);
	
	JSONObject checkItemsType(ItemsType externalItem);
	
	JSONObject externalWebResourceType(ExternalWebResourceType externalWebResource);
	
	JSONObject getResourceType(ResourceBodyType resource);
	
	JSONObject embeddedContent(EmbeddedContentType embeddedContent);
	
	JSONObject creationProvenance(CreationProvenanceType creationProvenanceType);
	
	JSONObject getCreator(CreatorType creator);
	
	RDFtype getParser(String xmlStr);
}
