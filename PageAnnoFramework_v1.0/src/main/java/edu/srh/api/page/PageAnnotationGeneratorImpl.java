package edu.srh.api.page;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.rio.RDFFormat;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.MotivationFactory;
import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.model.impl.agent.Software;
import com.github.anno4j.model.impl.multiplicity.Choice;
import com.github.anno4j.model.impl.selector.SvgSelector;
import com.github.anno4j.model.impl.targets.SpecificResource;

import edu.srh.api.TextAnnotationBody;
import edu.srh.exceptions.AnnotationExceptions;
import edu.srh.exceptions.StatusCode;
import edu.srh.pagexml.AdvertRegionType;
import edu.srh.pagexml.ChartRegionType;
import edu.srh.pagexml.ChemRegionType;
import edu.srh.pagexml.GraphicRegionType;
import edu.srh.pagexml.ImageRegionType;
import edu.srh.pagexml.LineDrawingRegionType;
import edu.srh.pagexml.MathsRegionType;
import edu.srh.pagexml.MetadataType;
import edu.srh.pagexml.MusicRegionType;
import edu.srh.pagexml.NoiseRegionType;
import edu.srh.pagexml.PageType;
import edu.srh.pagexml.PcGtsType;
import edu.srh.pagexml.RegionType;
import edu.srh.pagexml.SeparatorRegionType;
import edu.srh.pagexml.TableRegionType;
import edu.srh.pagexml.TextRegionType;
import edu.srh.pagexml.UnknownRegionType;
import edu.srh.pagexml.UserAttributeType;
import edu.srh.util.QueryUtil;

/**
 * 
 * @author SunnyLondhe
 *
 */

public class PageAnnotationGeneratorImpl implements PageAnnotationGenerator {

	private Anno4j anno4j;
	private QueryUtil qureyUtil;

	DatasetAccessor accessor;
	private List<String> resourceIDList = new ArrayList<String>();

	public PageAnnotationGeneratorImpl() throws RepositoryException, RepositoryConfigException {
		qureyUtil = new QueryUtil();
		anno4j = new Anno4j();
	}

	public PageAnnotationGeneratorImpl(Anno4j anno4j) {
		this.anno4j = anno4j;
		qureyUtil = new QueryUtil();
	}

	@Override
	public String parseAnnotations(String digitalObjID, String annotationString) throws AnnotationExceptions {

		createDataSet();

		// converting normal String to xmlObject
		PcGtsType pcgtsTypeObj = parseXML(annotationString);
		String softAgentResourceID = checkAgentInRegistry(pcgtsTypeObj.getMetadata());
		// processing the xml file and converting the xml to WADM
		processXML(pcgtsTypeObj, digitalObjID, softAgentResourceID);
		// store to apache Jena

		return postToJenaStore();

	}

	private void createDataSet() {
		accessor = DatasetAccessorFactory.createHTTP(ServiceURI);
	}

	/**
	 * This Method is used to create all the annotations based on the Regions and
	 * Page Tag.
	 * 
	 * @param pcgtsTypeObj
	 * @param softAgentResourceID
	 * @return
	 * @throws AnnotationExceptions
	 */
	@Override
	public void processXML(PcGtsType pcgtsTypeObj, String digitalObjID, String softAgentResourceID)
			throws AnnotationExceptions {
		createPageAnnotation(pcgtsTypeObj, softAgentResourceID, digitalObjID);
		createOtherAnnotations(pcgtsTypeObj, softAgentResourceID, digitalObjID);
	}

	@Override
	public List<Model> createOtherAnnotations(PcGtsType pcgtsTypeObj, String softAgentResourceID, String digitalObjID)
			throws AnnotationExceptions {
		for (RegionType regions : pcgtsTypeObj.getPage().getTextRegionOrImageRegionOrLineDrawingRegion()) {
			Annotation annoations = createAnnoataionPart(pcgtsTypeObj.getMetadata(), softAgentResourceID);
			Model otherModel = createOtherBodyTarget(annoations, regions, digitalObjID);
			// modelMap.put(annoations.getResourceAsString(), otherModel);
			accessor.add(annotationURL + annoations.getResourceAsString(), otherModel);
			resourceIDList.add(annoations.getResourceAsString());
		}
		return null;

	}

	@Override
	public Model createOtherBodyTarget(Annotation annoations, RegionType regions, String digitalObjID)
			throws AnnotationExceptions {

		Choice choice;
		try {
			choice = anno4j.createObject(Choice.class);

			for (UserAttributeType userAttribuets : regions.getUserDefined().getUserAttribute()) {
				TextAnnotationBody txtBody2 = anno4j.createObject(TextAnnotationBody.class);
				txtBody2.setValue(userAttribuets.getValue());
				txtBody2.setName(userAttribuets.getName());
				txtBody2.setSubject(getRegionType(regions));
				txtBody2.setIdentifier(regions.getId());
				txtBody2.setFormat(userAttribuets.getType());
				if (null != userAttribuets.getDescription())
					txtBody2.setUnit(userAttribuets.getDescription().split(":")[1]);

				if (null != regions.getCustom())
					txtBody2.setContributor(regions.getCustom());

				choice.addItem(txtBody2);

			}

			SpecificResource specific = getTarget(regions.getCoords().getPoints(), digitalObjID);

			annoations.addBody(choice);
			annoations.addTarget(specific);

			Model model = getJenaModel(annoations.getTriples(RDFFormat.NTRIPLES));
			writeTofile(annoations, "New_" + regions.getId());
			return model;
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}
	}

	@Override
	public String getRegionType(RegionType regionType) {
		if (regionType.getClass().equals(TextRegionType.class))
			return "TextRegion";
		if (regionType.getClass().equals(ImageRegionType.class))
			return "ImageRegion";
		if (regionType.getClass().equals(LineDrawingRegionType.class))
			return "LineDrawingRegion";
		if (regionType.getClass().equals(GraphicRegionType.class))
			return "GraphicRegion";
		if (regionType.getClass().equals(TableRegionType.class))
			return "TableRegion";
		if (regionType.getClass().equals(ChartRegionType.class))
			return "ChartRegion";
		if (regionType.getClass().equals(SeparatorRegionType.class))
			return "SeparatorRegion";
		if (regionType.getClass().equals(MathsRegionType.class))
			return "MathsRegion";
		if (regionType.getClass().equals(ChemRegionType.class))
			return "ChemRegion";
		if (regionType.getClass().equals(MusicRegionType.class))
			return "MusicRegion";
		if (regionType.getClass().equals(AdvertRegionType.class))
			return "AdvertRegion";
		if (regionType.getClass().equals(NoiseRegionType.class))
			return "NoiseRegion";
		if (regionType.getClass().equals(UnknownRegionType.class))
			return "UnknownRegion";

		return null;
	}

	@Override
	public Model createPageAnnotation(PcGtsType pcgtsTypeObj, String softAgentResourceID, String digitalObjID)
			throws AnnotationExceptions {
		Annotation annoations;
		Model model;
		annoations = createAnnoataionPart(pcgtsTypeObj.getMetadata(), softAgentResourceID);
		model = createBodyTarget(annoations, pcgtsTypeObj.getPage(), digitalObjID);
		accessor.add(annotationURL + annoations.getResourceAsString(), model);
		resourceIDList.add(annoations.getResourceAsString());
		return model;

	}

	@Override
	public Model createBodyTarget(Annotation annoations, PageType page, String digitalObjID)
			throws AnnotationExceptions {

		TextAnnotationBody fileNameBody;
		try {
			fileNameBody = anno4j.createObject(TextAnnotationBody.class);
			fileNameBody.setUnit(page.getCustom().split(":")[1]);
			fileNameBody.setName("imageFileName");
			fileNameBody.setValue(page.getImageFilename());

			TextAnnotationBody imageHeightBody = anno4j.createObject(TextAnnotationBody.class);
			imageHeightBody.setUnit(page.getCustom().split(":")[1]);
			imageHeightBody.setName("imageHeight");
			imageHeightBody.setValue("" + page.getImageHeight());

			TextAnnotationBody imageWidthBody = anno4j.createObject(TextAnnotationBody.class);
			imageWidthBody.setUnit(page.getCustom().split(":")[1]);
			imageWidthBody.setName("imageWidth");
			imageWidthBody.setValue("" + page.getImageWidth());

			Choice choice = anno4j.createObject(Choice.class);
			for (UserAttributeType userAttribuets : page.getUserDefined().getUserAttribute()) {
				TextAnnotationBody txtBody2 = anno4j.createObject(TextAnnotationBody.class);
				txtBody2.setValue(userAttribuets.getValue());
				txtBody2.setName(userAttribuets.getName());
				txtBody2.setFormat(userAttribuets.getType());
				if (null != userAttribuets.getDescription()) {
					txtBody2.setUnit(userAttribuets.getDescription().split(":")[1]);
				}
				choice.addItem(txtBody2);
			}

			SpecificResource specific = getTarget(
					(null != page.getBorder() ? page.getBorder().getCoords().getPoints() : ""), digitalObjID);
			annoations.addBody(fileNameBody);
			annoations.addBody(imageHeightBody);
			annoations.addBody(imageWidthBody);
			annoations.addBody(choice);
			annoations.addTarget(specific);
			Model model = getJenaModel(annoations.getTriples(RDFFormat.NTRIPLES));

			// writeTofile(annoations, "pageAnnotation");
			return model;
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}

	}

	private void writeTofile(Annotation annoations, String fileName) {
		File f = new File("C:\\Users\\sunny\\Desktop\\AnnotationOutPut\\" + fileName + "_RDF.xml");
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
			writer.write(annoations.getTriples(RDFFormat.RDFXML));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				/* ignore */}
		}

	}

	@Override
	public SpecificResource getTarget(String points, String digitalObjID) throws AnnotationExceptions {
		SpecificResource specific;
		try {
			specific = anno4j.createObject(SpecificResource.class);
			SvgSelector svg = anno4j.createObject(SvgSelector.class);
			if (!points.isEmpty()) {
				svg.setValue(points);
				specific.setSelector(svg);
			}
			ResourceObject source = anno4j.createObject(ResourceObject.class);
			source.setResourceAsString(digitalObjID);
			specific.setSource(source);

			return specific;
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (MalformedQueryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (UpdateExecutionException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}

	}

	@Override
	public Annotation createAnnoataionPart(MetadataType metadata, String softAgentResourceID)
			throws AnnotationExceptions {
		Annotation annotation;
		try {
			String created = (metadata.getCreated().toString().indexOf("Z") == -1)
					? metadata.getCreated().toString() + "Z"
					: metadata.getCreated().toString();
			annotation = anno4j.createObject(Annotation.class);
			Software agent = anno4j.createObject(Software.class);
			agent.setResourceAsString(softAgentResourceID);
			annotation.setCreator(agent);
			annotation.setCreated(created);
			annotation.setModified(created);
			annotation.addMotivation(MotivationFactory.getDescribing(anno4j));

			return annotation;
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (MalformedQueryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		} catch (UpdateExecutionException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}

	}

	/**
	 * This Method is used to store new creator information and retrieve the stored
	 * user from JENA.
	 * 
	 * @param softwareAgent
	 * @return
	 * @throws AnnotationExceptions
	 */
	@Override
	public String checkAgentInRegistry(MetadataType metadata) throws AnnotationExceptions {

		QueryExecution query = QueryExecutionFactory.sparqlService(ServiceURI,
				qureyUtil.checkAgentQuery(metadata.getCreator()));
		ResultSet results = query.execSelect();
		// below method can be used for printing RDF Jena data
		// ResultSetFormatter.out(System.out, results);

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			RDFNode x = soln.get("o");
			if (x.toString().equals(metadata.getCreator())) {
				return soln.get("s").toString();
			}
		}

		Software softwareAgent;
		try {
			softwareAgent = anno4j.createObject(Software.class);
			softwareAgent.setName(metadata.getCreator());
		} catch (RepositoryException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		} catch (IllegalAccessException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.SERVICE_UNAVAILABLE.getStatusCode());
		} catch (InstantiationException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
		}

		// agentModel =
		// getJenaModel(softwareAgent.getTriples(RDFFormat.NTRIPLES));
		// agentFlag = true;
		// DatasetAccessor accessor =
		// DatasetAccessorFactory.createHTTP(ServiceURI);
		// Model model =
		// getJenaModel(softwareAgent.getTriples(RDFFormat.NTRIPLES));
		// accessor.add(annotationURL + "agents", model);
		return softwareAgent.getResourceAsString();
	}

	@Override
	public Model getJenaModel(String triples) {
		Model model = ModelFactory.createDefaultModel();
		try (final InputStream in = new ByteArrayInputStream(triples.getBytes("UTF-8"))) {
			model.read(in, null, "TTL");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return model;
	}

	/**
	 * This method is used to convert the xmlString into PcGtsType Object using
	 * JAXb.
	 * 
	 * @param annotationString
	 * @return Object PcGtsType
	 * @throws AnnotationExceptions
	 */
	private PcGtsType parseXML(String annotationString) throws AnnotationExceptions {
		Source source = new StreamSource(new StringReader(annotationString));
		PcGtsType pcgtsTypeObj = null;
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(PcGtsType.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			JAXBElement<PcGtsType> root = jaxbUnmarshaller.unmarshal(source, PcGtsType.class);
			pcgtsTypeObj = root.getValue();
			return pcgtsTypeObj;
		} catch (JAXBException e) {
			throw new AnnotationExceptions(e.getMessage(), StatusCode.BAD_REQUEST.getStatusCode());
		}
	}

	@Override
	public String postToJenaStore() {
		int counter = 0;
		String pageXmlID = "" + UUID.randomUUID();
		UpdateRequest request = UpdateFactory.create();
		request.add("CREATE GRAPH <" + ServiceURI + "/registry>");

		for (String resourceID : resourceIDList) {
			request.add(qureyUtil.getAnnotationRegistryQuery(pageXmlID, annotationURL + resourceID));
			counter++;
		}
		UpdateProcessor createRemote = UpdateExecutionFactory.createRemote(request, ServiceURI);
		createRemote.execute();

		return "Total No Of Annotation Create : " + counter;

	}

}
