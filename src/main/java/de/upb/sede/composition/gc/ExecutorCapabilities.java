package de.upb.sede.composition.gc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ExecutorCapabilities {
	public static final String javalibs = "JAVA-LIB";
	public static final String pythonlibs = "PYTHON-LIB";
	

	private final List<String> gatewayCapabilities;
	private final List<String> supportedServiceClasses;
	
	public ExecutorCapabilities(String...capabilities) {
		List<String> capabilityList = new ArrayList<>();
		this.supportedServiceClasses = new ArrayList<>();
		for(String capability : capabilities) {
			capabilityList.add(capability);
		}
		gatewayCapabilities = Collections.unmodifiableList(capabilityList);
	}
	
	public void addAllServiceClasses(String... newServiceClasses) {
		Objects.requireNonNull(newServiceClasses);
		for(String serviceClass : newServiceClasses) {
			supportedServiceClasses.add(serviceClass);
		}
	}
	
	public boolean supportsServiceClass(String capability) {
		return supportedServiceClasses.contains(capability);
	}
	
	public boolean isCapableOf(String capability) {
		return gatewayCapabilities.contains(capability);
	}
	
}