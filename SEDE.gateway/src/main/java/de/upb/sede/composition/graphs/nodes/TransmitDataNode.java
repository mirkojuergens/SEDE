package de.upb.sede.composition.graphs.nodes;

import java.util.*;

public class TransmitDataNode extends BaseNode {

	private final String fieldname;

	private final Map<String, Object> contactInfo;

	private final Optional<String> caster;

	private final Optional<String> semanticTypename;

	public TransmitDataNode(String fieldname, Map<String, Object>  contactInfo, String caster, String semanticTypename) {
		this.fieldname = Objects.requireNonNull(fieldname);
		this.contactInfo = new HashMap<String, Object> (Objects.requireNonNull(contactInfo));
		this.caster = Optional.of(caster);
		this.semanticTypename = Optional.of(semanticTypename);
	}


	public TransmitDataNode(String fieldname, Map<String, Object> contactInfo){
		this.fieldname = Objects.requireNonNull(fieldname);
		this.contactInfo =  new HashMap<String, Object> (Objects.requireNonNull(contactInfo));
		this.caster = Optional.empty();
		this.semanticTypename = Optional.empty();
	}

	public Map<String, Object>  getContactInfo() {
		return contactInfo;
	}

	public String getSendingFieldName() {
		return fieldname;
	}


	public boolean hasCaster() {
		return caster.isPresent();
	}

	public boolean hasSemanticType() {
		return semanticTypename.isPresent();
	}

	public String getCaster() {
		return caster.get();
	}

	public String getSemanticTypename() {
		return semanticTypename.get();
	}

	public String toString() {
		String host = (String) contactInfo.get("host-address");
		if(host == null) {
			host = "NO_HOST";
		}
		return "transmit \"" + fieldname + "\"->" + contactInfo.get("id") + ":" + host;
	}
}
