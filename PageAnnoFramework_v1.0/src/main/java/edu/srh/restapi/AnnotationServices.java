package edu.srh.restapi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import edu.srh.api.QueryByTarget;
import edu.srh.api.QueryByTargetImpl;
import edu.srh.api.page.PageAnnotationGenerator;
import edu.srh.api.page.PageAnnotationGeneratorImpl;
import edu.srh.exceptions.AnnotationExceptions;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/")
public class AnnotationServices {

	/**
	 * This Method is used to test the Rest service.
	 * 
	 * @return String
	 */
	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkConnection() {
		return "Connection Found Status : OK Then";
	}

	/**
	 * This Method is used to convert and store the xml/json into Annotation Store.
	 * 
	 * @param digitalObjID
	 * @param dataToStore
	 * @return No of Annotation created.
	 * @throws AnnotationExceptions
	 */
	@POST
	@Path("store")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String checkJsonLD(@QueryParam("digitalObjID") String digitalObjID, String dataToStore)
			throws AnnotationExceptions {

		try {
			PageAnnotationGenerator generator = new PageAnnotationGeneratorImpl();
			return generator.parseAnnotations(digitalObjID, dataToStore);
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (RepositoryConfigException e) {
			e.printStackTrace();
		}

		return "No Annotation Created";
	}

	/**
	 * This method is used to run any query (select / construct ) and obtain the
	 * results in required format. Which can be any of the following, "TURTLE"
	 * TURTLE "TTL" TURTLE "Turtle" TURTLE "N-TRIPLES" NTRIPLES "N-TRIPLE" NTRIPLES
	 * "NT" NTRIPLES "JSON-LD" JSONLD "RDF/XML-ABBREV" RDFXML "RDF/XML" RDFXML_PLAIN
	 * "N3" N3 "RDF/JSON" RDFJSON "ld+json" anno-json annotation profile
	 * 
	 * @param queryString
	 * @param format
	 * @return
	 * , @QueryParam("format") String format
	 */
	@POST
	@Path("sparql")	
	public String executeQuery(@FormParam("qry") String queryString) {
		if (String.valueOf(queryString).isEmpty()) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(
					"Query Not provided")
					.build());
		} else {
			QueryByTarget queryExec = new QueryByTargetImpl();
			String results = queryExec.getQueryResults(queryString, "ld+json");
			return results;

		}
	}
	
	
	@POST
	@Path("upload")	
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormParam("file") InputStream uploadedInputStream,
			@FormParam("file") FormDataContentDisposition fileDetail) {
		// check if all form parameters are provided
		if (uploadedInputStream == null || fileDetail == null)
			return Response.status(400).entity("Invalid form data").build();
		// create our destination folder, if it not exists
		try {
			createFolderIfNotExists(UPLOAD_FOLDER);
		} catch (SecurityException se) {
			return Response.status(500)
					.entity("Can not create destination folder on server")
					.build();
		}
		String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();
		try {
			saveToFile(uploadedInputStream, uploadedFileLocation);
		} catch (IOException e) {
			return Response.status(500).entity("Can not save file").build();
		}
		return Response.status(200)
				.entity("File saved to " + uploadedFileLocation).build();
	}

	@GET
	@Path("getregistry")
	public Response getall() {

		QueryByTargetImpl queryExec = new QueryByTargetImpl();
		String results = queryExec.getQueryResultsByID("http://srh.de/anno/annotationRegistry", "RDF"); // Json format
		// for registry
		// is not
		// possible
		return Response.status(200).entity(results).build();
		// }
	}

	/**
	 * This Method is used to get Single annotaion by the given ID.
	 * 
	 * @param annoid
	 * @return
	 */
	@GET
	@Path("getAnnotationById")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String executeQueryByID(@QueryParam("format") String format, @QueryParam("ID") String annoid) {
		QueryByTargetImpl queryExec = new QueryByTargetImpl();
		String results = queryExec.getQueryResultsByID(annoid, format);
		return results;
	}
	
	private void saveToFile(InputStream inStream, String target)
			throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];
		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}
	
	private void createFolderIfNotExists(String dirName)
			throws SecurityException {
		File theDir = new File(dirName);
		if (!theDir.exists()) {
			theDir.mkdir();
		}
	}

}
