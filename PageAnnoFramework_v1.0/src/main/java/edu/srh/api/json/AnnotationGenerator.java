package edu.srh.api.json;

public interface AnnotationGenerator {

	String validateNStoreJSONLD(String input);

	String validateNStoreRDFXML(String input);

	String validateNStoreTURTLE(String input);

	String validateNStoreNTriple(String input);

}
