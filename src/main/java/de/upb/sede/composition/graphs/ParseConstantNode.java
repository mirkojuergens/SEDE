package de.upb.sede.composition.graphs;

import java.util.Collection;
import java.util.Objects;

import de.upb.sede.composition.graphconstructioninformation.ClassesConfig;

public class ParseConstantNode extends BaseNode {
	
	private final String constantValue;
	public ParseConstantNode(String constantValue) {
		this.constantValue = Objects.requireNonNull(constantValue);
	}
	
	@Override
	boolean producesField(String fieldname, ClassesConfig configuration) {
		return constantValue.equals(fieldname);
	}

	@Override
	Collection<String> consumingFields() {
		return null;
	}


}
