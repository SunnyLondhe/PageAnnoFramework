package edu.srh.rdfcore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CreatorType {

	@XmlAttribute(name = "resource", namespace = IRINS.rdf)
	private String resource;

	@XmlElement(name = "SoftwareAgent", namespace = IRINS.j2)
	private SoftwareAgentType softwareAgent;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public SoftwareAgentType getSoftwareAgent() {
		return softwareAgent;
	}

	public void setSoftwareAgent(SoftwareAgentType softwareAgent) {
		this.softwareAgent = softwareAgent;
	}

	@Override
	public String toString() {
		return "CreatorType [resource=" + resource + ", softwareAgent=" + softwareAgent + "]";
	}

}
