package edu.srh.restapi;



import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import edu.srh.api.QueryByTarget;
import edu.srh.api.QueryByTargetImpl;

public class ResultFinder {
	public static void main(String[] args) throws RepositoryException, RepositoryConfigException {
		//String annoid = "http://srh.de/anno/urn:anno4j:71b9c442-7015-42fd-b734-cb93c96a6537";
		String format ="ld+json";
		String qry="SELECT ?subject ?predicate ?object WHERE { {graph <http://srh.de/anno/urn:anno4j:71b9c442-7015-42fd-b734-cb93c96a6537> {  ?subject ?predicate ?object} } }";
		
		QueryByTarget queryExec = new QueryByTargetImpl();
		String results = queryExec.getQueryResults(qry, format);
		
		/*QueryByTargetImpl queryExec = new QueryByTargetImpl();
		String results = queryExec.getQueryResultsByID(annoid, format);*/
		System.out.println(results);
	}
}
