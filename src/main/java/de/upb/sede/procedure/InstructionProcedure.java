package de.upb.sede.procedure;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.upb.sede.composition.graphs.nodes.ParseConstantNode.ConstantType;
import de.upb.sede.core.SEDEObject;
import de.upb.sede.core.ServiceInstanceHandle;
import de.upb.sede.exec.ExecutionEnvironment;
import de.upb.sede.exec.Task;

public class InstructionProcedure implements Procedure {
	static Logger logger = LogManager.getLogger(InstructionProcedure.class);

	@Override
	public void process(Task task) {
		ExecutionEnvironment environment = task.getExecution().getExecutionEnvironment();
		InstructionNodeAttributes nodeAttributes = new InstructionNodeAttributes(task);
		try {
			// Get class to be called.
			String contextType;
			if (!nodeAttributes.isContextAFieldname()) {
				contextType = nodeAttributes.getContext();
			} else {
				// Get the service from the environment and afterwards its type.
				contextType = environment.get(nodeAttributes.getContext()).getType();
			}
			Class<?> contextClass = Class.forName(contextType);

			// Get SEDEObjects of the parameters that the method is called with.
			Map<String, SEDEObject> parameterObjects = getParameterObjectsInOrder(nodeAttributes.getParameters(),
					environment);
			// Get the class of the parameters.
			List<String> parameterTypes = getParameterTypes(parameterObjects);
			Class<?>[] parameterClasses = getParameterClasses(parameterTypes, nodeAttributes.getMethod(), contextClass);
			// Get the values of the parameters
			Object[] parameterValues = getParameterValues(parameterObjects);

			/*
			 * When the call is a constructor call the the constructor is being reflected
			 * and called.
			 */
			SEDEObject returnSEDEObject;
			if (nodeAttributes.isConstructor()) {
				Constructor<?> constructor = contextClass.getConstructor(parameterClasses);
				Object newInstance = constructor.newInstance(parameterValues);
				ServiceInstanceHandle serviceInstanceHandle = task.getExecution()
						.createServiceInstanceHandle(newInstance);
				returnSEDEObject = new SEDEObject(ServiceInstanceHandle.class.getName(), serviceInstanceHandle);
			} else {
				Method methodToBeCalled = contextClass.getMethod(nodeAttributes.getMethod(), parameterClasses);
				String returnType = methodToBeCalled.getReturnType().getName();
				Object contextServiceInstance;
				if (nodeAttributes.isContextAFieldname()) {
					SEDEObject serviceInstace = environment.get(nodeAttributes.getContext());
					if (!serviceInstace.isServiceInstance()) {
						throw new RuntimeException("BUG: trying to operate on service of type: " + contextType
								+ " instead the SEDE Object is " + serviceInstace.getType());
					}
					contextServiceInstance = ((ServiceInstanceHandle) serviceInstace.getObject()).getServiceInstance()
							.get();
				} else {
					contextServiceInstance = null;
				}
				Object returnValue = methodToBeCalled.invoke(contextServiceInstance, parameterValues);
				returnSEDEObject = new SEDEObject(returnType, returnValue);
			}
			if (nodeAttributes.getLeftsidefieldname() != null) {
				environment.put(nodeAttributes.getLeftsidefieldname(), returnSEDEObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> getParameterTypes(Map<String, SEDEObject> parameterObjects) {
		List<String> paramTypes = new ArrayList<>();
		parameterObjects.values().stream().forEach(sedeObj -> paramTypes.add(sedeObj.getType()));
		return paramTypes;
	}

	private Map<String, SEDEObject> getParameterObjectsInOrder(List<String> parameters,
			ExecutionEnvironment environment) {
		Map<String, SEDEObject> result = new LinkedHashMap<>(parameters.size());
		parameters.forEach(varName -> result.put(varName, environment.get(varName)));
		return result;
	}

	private Class<?>[] getParameterClasses(List<String> paramTypes, String methodName, Class<?> contextClass)
			throws ClassNotFoundException, NoSuchMethodException {
		List<Class<?>> inOrderClasses = new ArrayList<>(paramTypes.size());
		/*
		 * If there are ConstantTypes in the signature of the method to be called, it's
		 * iterated over all possible methods (matching name and parameter count).
		 */
		if (parameterIncludeConstantType(paramTypes)) {
			Method methodThatMatchesSignatureWithConstantTypes = getMethodThatMatchesSignatureWithConstantTypes(
					paramTypes, methodName, contextClass);
			for (Class<?> clazz : methodThatMatchesSignatureWithConstantTypes.getParameterTypes()) {
				inOrderClasses.add(clazz);
			}
		} else {
			for (String classType : paramTypes) {
				Class<?> clazz = Class.forName(classType);
				inOrderClasses.add(clazz);
			}
		}
		Class<?>[] inOrderClassesArray = new Class<?>[inOrderClasses.size()];
		inOrderClassesArray = inOrderClasses.toArray(inOrderClassesArray);
		return inOrderClassesArray;
	}

	private Method getMethodThatMatchesSignatureWithConstantTypes(List<String> paramTypes, String calledMethodName,
			Class<?> contextClass) throws NoSuchMethodException {
		Method[] contextClassMethods = contextClass.getMethods();
		List<Method> methodsThatMatchMethodNameAndParamCount = new ArrayList<>();
		for (Method method : contextClassMethods) {
			if (nameAndParamCountMatches(paramTypes, calledMethodName, method)) {
				methodsThatMatchMethodNameAndParamCount.add(method);
			}
		}
		for (Method method : methodsThatMatchMethodNameAndParamCount) {
			if (matchesSignature(paramTypes, method)) {
				return method;
			}
		}
		throw new NoSuchMethodException();
	}

	private boolean nameAndParamCountMatches(List<String> paramTypes, String calledMethodName, Method method) {
		return method.getName().equals(calledMethodName) && method.getParameterCount() == paramTypes.size();
	}

	private boolean matchesSignature(List<String> calledParamTypes, Method methodToCheck) {
		Class<?>[] methodParameters = methodToCheck.getParameterTypes();
		Map<Integer, String> indicesOfRealTypesInCall = getIndicesOfRealTypes(calledParamTypes);
		// If the real types do not match then this method is no candidate for the
		// called parameters.
		for (Entry<Integer, String> indexTypePairInCall : indicesOfRealTypesInCall.entrySet()) {
			if (methodParameters[indexTypePairInCall.getKey()].getName() != indexTypePairInCall.getValue()) {
				return false;
			}
		}
		Map<Integer, String> indicesOfConstantTypesInCall = getInverseIndicesTypes(indicesOfRealTypesInCall,
				calledParamTypes);
		return false;
	}

	private Map<Integer, String> getInverseIndicesTypes(Map<Integer, String> indicesOfRealTypesInCall,
			List<String> calledParamTypes) {
		Map<Integer, String> mapOfParameters = new HashMap<>();
		for (int i = 0, size = calledParamTypes.size(); i < size; i++) {
			mapOfParameters.put(i, calledParamTypes.get(i));
		}
		
		return null;
	}

	private Map<Integer, String> getIndicesOfRealTypes(List<String> calledParamTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<String> getParamTypes(Method methodToCheck) {
		List<String> methodParamTypes = new ArrayList<>();
		for (Class<?> clazz : methodToCheck.getParameterTypes()) {
			methodParamTypes.add(clazz.getName());
		}
		return methodParamTypes;
	}

	private boolean parameterIncludeConstantType(List<String> paramTypes) {
		ConstantType[] constantTypes = ConstantType.values();
		Set<String> constantTypeNames = new HashSet<>();
		for (ConstantType type : constantTypes) {
			constantTypeNames.add(type.toString());
		}
		for (String paramType : paramTypes) {
			if (constantTypeNames.contains(paramType))
				return true;
		}
		return false;
	}

	private Object[] getParameterValues(Map<String, SEDEObject> parameterObjects) {
		List<Object> inOrderObjects = new ArrayList<>(parameterObjects.size());
		for (SEDEObject sedeObject : parameterObjects.values()) {
			inOrderObjects.add(sedeObject.getObject());
		}
		Object[] parameterArray = new Object[inOrderObjects.size()];
		parameterArray = inOrderObjects.toArray(parameterArray);
		return parameterArray;
	}

	// TODO Must be moved into another class.
	private Class<?> addClassInClassLoader(String context) throws Exception {
		URL serviceFilesURL = new URL("jar", "", context + "!/");
		JarURLConnection jarURLConnection = (JarURLConnection) serviceFilesURL.openConnection();
		Attributes attr = jarURLConnection.getMainAttributes();
		if (attr != null) {
			logger.info("Servicefile: " + context + "\n Main class:" + attr.getValue(Attributes.Name.MAIN_CLASS));
		} else {
			logger.error("No entry point in: \"" + context + "\"");
		}
		URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { serviceFilesURL },
				this.getClass().getClassLoader());
		Class<?> reflectedClass = Class.forName(context, true, urlClassLoader);
		addURLToClassLoader(serviceFilesURL);
		return reflectedClass;
	}

	private static void addURLToClassLoader(URL url) throws Exception {
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		if (systemClassLoader instanceof URLClassLoader) {
			URLClassLoader urlClassLoader = (URLClassLoader) systemClassLoader;
			// addURL is a protected method, but we can use reflection to call it
			Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			addURLMethod.setAccessible(true);
			addURLMethod.invoke(urlClassLoader, url);
		} else {
			logger.error("System ClassLoader is no URLClassLoader.");
		}
	}

	class InstructionNodeAttributes {
		private final boolean isConstructor;
		private final boolean isContextAFieldname;
		private final String method;
		private final String leftsidefieldname;
		private final String host;
		private final String context;
		private final String fmInstruction;
		private final List<String> parameters;

		@SuppressWarnings("unchecked")
		public InstructionNodeAttributes(Task task) {
			Map<String, Object> parameters = task.getAttributes();
			this.isConstructor = (boolean) parameters.get("is-service-construction");
			this.isContextAFieldname = (boolean) parameters.get("is-context-a-fieldname");
			this.method = (String) parameters.get("method");
			this.leftsidefieldname = (String) parameters.get("leftsidefieldname");
			this.host = (String) parameters.get("host");
			this.context = (String) parameters.get("context");
			this.fmInstruction = (String) parameters.get("fmInstruction");
			this.parameters = (List<String>) parameters.get("params");
		}

		public String getFmInstruction() {
			return fmInstruction;
		}

		public boolean isConstructor() {
			return isConstructor;
		}

		public boolean isContextAFieldname() {
			return isContextAFieldname;
		}

		public String getMethod() {
			return method;
		}

		public String getLeftsidefieldname() {
			return leftsidefieldname;
		}

		public String getHost() {
			return host;
		}

		public String getContext() {
			return context;
		}

		public List<String> getParameters() {
			return parameters;
		}

	}
}
