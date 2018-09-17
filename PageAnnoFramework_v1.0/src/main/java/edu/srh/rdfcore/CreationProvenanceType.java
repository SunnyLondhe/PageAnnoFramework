package edu.srh.rdfcore;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CreationProvenanceType {

	@XmlElement(name = "hasSource", namespace = IRINS.j0)
	private HasSourceType hasSource;
	
	@XmlElement(name= "creator", namespace = IRINS.j1)
	private Creator creator;

	@XmlElement(name = "hasSelector", namespace = IRINS.j0)
	private HasSelector hasSelector;
	
	@XmlElement(name="items", namespace = IRINS.j3)
	private List<ItemsType> items;
	
	@XmlElement(name="title",namespace=IRINS.dc)
	private String title;
	
	@XmlElement(name="subject", namespace=IRINS.dc)
	private String subject;
	
	@XmlElement(name="value", namespace=IRINS.rdf)
	private String value;
	
	@XmlElement(name="identifier",namespace=IRINS.dc)
	private String identifier;
	
	@XmlElement(name="imageWidth", namespace=IRINS.unit)
	private String imageWidth;
	
	@XmlElement(name="imageHeight", namespace=IRINS.unit)
	private String imageHeight;
	
	@XmlElement(name = "unit", namespace=IRINS.j6)
	private String unit;
	
	@XmlElement(name="format", namespace=IRINS.dc)
	private String format;

	@XmlElement(name="contributor", namespace=IRINS.dc)
	private String contributor;
	
	@XmlElement(name="hasPurpose", namespace=IRINS.j0)
	private HasPurpose hasPurpose;
	
	
	
	public HasPurpose getHasPurpose() {
		return hasPurpose;
	}

	public void setHasPurpose(HasPurpose hasPurpose) {
		this.hasPurpose = hasPurpose;
	}

	public HasSourceType getHasSource() {
		return hasSource;
	}

	public void setHasSource(HasSourceType hasSource) {
		this.hasSource = hasSource;
	}

	public HasSelector getHasSelector() {
		return hasSelector;
	}

	public void setHasSelector(HasSelector hasSelector) {
		this.hasSelector = hasSelector;
	}

	public List<ItemsType> getItems() {
		return items;
	}

	public void setItems(List<ItemsType> items) {
		this.items = items;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(String imageWidth) {
		this.imageWidth = imageWidth;
	}

	public String getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(String imageHeight) {
		this.imageHeight = imageHeight;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getContributor() {
		return contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	public Creator getCreator() {
		return creator;
	}

	public void setCreator(Creator creator) {
		this.creator = creator;
	}

}
