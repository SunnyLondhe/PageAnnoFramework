package edu.srh.restapi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


import com.github.jsonldjava.core.JsonLdError;

public class QueryTester {

	public static void main(String[] args) throws IOException, JsonLdError {
//		final String ServiceURI = "http://localhost:3030/kit/";
//		QueryUtil qureyUtil = new QueryUtil();
		
//		QueryExecution query = QueryExecutionFactory.sparqlService(ServiceURI,
//				qureyUtil.getByAnnoIDQuery("urn:anno4j:6172bf32-db26-4bc2-a31f-04c10074964f"));
//		ResultSet results = query.execSelect();
		// below method can be used for printing RDF Jena data
//		 ResultSetFormatter.out(System.out, results);

		/*while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get("o");
			if (x.toString().equals(metadata.getCreator())) {
				return soln.get("s").toString();
			}
		}*/
		
		RestService rest = new RestService();
//		String rite = rest.getAnnotationByTarget("http://example.org/source1", "RDF/XML");
//		String rite = rest.getAnnotationByTarget("http://example.org/source1", "ld+json");
//		String rite = rest.getAnnotationByID("urn:anno4j:628fdbc4-1646-4163-bcb8-b4be0490c5b3","ld+json");
		String rite = rest.getAnnotationByID("http://kit.edu/anno/urn:anno4j:a33b3709-29fa-4056-a281-6b00b9669c80","ld+json");
		
//		QueryByTarget queryExec = new QueryByTargetImpl();
//		String rite = queryExec.getQueryResults(" CONSTRUCT {?s ?p ?o} WHERE { GRAPH <http://kit.edu/anno/urn:anno4j:c09e2a2a-35b6-4b6e-9a3e-a15e6a9c4f9c> {?s ?p ?o} } ","RDF/XML");
		
//			System.out.println(rite);
		writeTofile(rite);
	
	}

	private static void writeTofile(String rite) {
		System.out.println("writing to file");
		File f = new File("C:\\Users\\sunny\\Desktop\\AnnotationOutPut\\completeAnno.json");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
			writer.write(rite);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				/* ignore */}
		}
	}
}
