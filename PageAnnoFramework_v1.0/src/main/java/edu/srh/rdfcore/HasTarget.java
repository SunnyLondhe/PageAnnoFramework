package edu.srh.rdfcore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class HasTarget {
	
	@XmlElement(name="CreationProvenance", namespace = IRINS.j4)
	private CreationProvenanceType creationProvenance;

	public CreationProvenanceType getCreationProvenance() {
		return creationProvenance;
	}

	public void setCreationProvenance(CreationProvenanceType creationProvenance) {
		this.creationProvenance = creationProvenance;
	}
	
}
