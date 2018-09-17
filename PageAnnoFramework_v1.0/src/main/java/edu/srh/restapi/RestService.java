package edu.srh.restapi;

import edu.srh.api.QueryByTarget;
import edu.srh.api.QueryByTargetImpl;

public class RestService {

	/**
	 * This method is used for converting and storing pageXML to annotations.
	 * @param digitalObjId
	 * @param xmlString
	 */
	public void storeAnnotation(String digitalObjId, String xmlString) {
		
		/*PageXMLProcessing xmlProcessing = new PageXMLProcessing(digitalObjId,xmlString);
		xmlProcessing.parseAndStoreXML();*/
		
	}
	/**
	 * The following RDF formats are supported by Jena.
     * Jena reader	RIOT Lang
     * 
	 * 	  "TURTLE"	TURTLE
	 * 	     "TTL"	TURTLE
	 * 	  "Turtle"	TURTLE
	 * "N-TRIPLES"	NTRIPLES
	 *  "N-TRIPLE"	NTRIPLES
	 * 	      "NT"	NTRIPLES
	 *   "RDF/XML"	RDFXML
	 * 	      "N3"	N3
	 *   "JSON-LD"	JSONLD
	 *  "RDF/JSON"	RDFJSON
	 *  "RDF/JSON"	RDFJSON
	 *  
	 * @param targetString
	 * @param format
	 */
	public String getAnnotationByTarget(String targetString,String format){
		QueryByTarget queryByTarget = new QueryByTargetImpl();
		String queryOutPut = queryByTarget.getByTarget(targetString,format);
		return queryOutPut;
		
	}
	
	/**
	 * This Method is used for executing all the RAW query need for Executions.
	 * @param queryString
	 * @param format
	 */
	public void executeQuery(String queryString, String format){
		QueryByTarget queryExec = new QueryByTargetImpl();
		queryExec.getQueryResults(queryString,format);
		
	}
	public String getAnnotationByID(String idStr, String format) {
		QueryByTarget queryExec = new QueryByTargetImpl();
		return queryExec.getQueryResultsByID(idStr,format);
	}
	
	

}
