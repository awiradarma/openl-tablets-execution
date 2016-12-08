package andre.test;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openl.rules.runtime.RulesEngineFactory;
import org.openl.types.IOpenClass;
import org.openl.types.IOpenField;
import org.openl.types.impl.DomainOpenClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenLRuleManager {

	// TODO - validate and refactor to make the code thread safe
	// TODO - figure out how to handle multiple spreadsheets and naming collision
	// TODO - clean up exception/error handling across all classes
	// TODO - clean up logging level across all classes
		
	private final Logger logger = LoggerFactory.getLogger(OpenLRuleManager.class);
	
	private String excelfile=null;
	
	boolean isInitialized() {
		if (excelfile == null) {
			return false;
		} else {
			return true;
		}
	}
	
	// datatypeMap stores the generated Datatypes - which also includes alias datatypes 
	private static ConcurrentHashMap<String,ValueObject> datatypeMap = new ConcurrentHashMap<String, ValueObject>();
	
	// map of alias datatype and its corresponding base type
	private static ConcurrentHashMap<String, String> aliasTypeMap = new ConcurrentHashMap<String, String>();
	
	// list of supported base types
	final static List<String> baseTypes = Arrays.asList("java.lang.String", "java.lang.String[]", "java.lang.Integer", "java.lang.Integer[]"
			,"java.lang.Double","java.lang.Double[]","java.lang.Boolean","java.lang.Boolean[]"
			,"java.util.Date","java.util.Date[]");

	// provides a new valueobject based on the specified datatype (if it's been defined and loaded properly)
	public ValueObject newValueObject(String datatype) {
		if (isInitialized()) {
			if (datatypeMap.containsKey(this.excelfile+"::"+datatype)) {
				return datatypeMap.get(this.excelfile+"::"+datatype).generateEmptyCopy(); 
			} 
		}
		return null;
	}

	public ValueObject[] newValueObjectArrayOf(String datatype, int size) {
		if (isInitialized()) {
			if (datatypeMap.containsKey(this.excelfile+"::"+datatype)) {
				ValueObject[] vos = new ValueObject[size]; 
				for (int i = 0; i < vos.length; i++) {
					vos[i] = datatypeMap.get(this.excelfile+"::"+datatype).generateEmptyCopy();
				}
				return vos;
			}
		} 
		return null;
	}
	
	// returns the class definition of the specified datatype, needed to convert JSON string to the datatype 
	public Class obtainClassDefinition(String datatype) {
			if (isInitialized()) {
				if (datatypeMap.containsKey(this.excelfile+"::"+datatype)) {
					return datatypeMap.get(this.excelfile+"::"+datatype).getWrappedObject().getClass(); 
				}
			} 
			return null;
	}

	public OpenLRule findRule(String ruleName) {
		if (isInitialized()) {
		RulesEngineFactory<?> rulesFactory =  new RulesEngineFactory<Object>(this.excelfile);
		Object ruleFactory = rulesFactory.newInstance();
		Method ruleMethod = null;
		Method[] methods = rulesFactory.getInterfaceClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equalsIgnoreCase(ruleName)) {
				ruleMethod = methods[i]; // assume / create standard that there should not be more than one rule with the same name and different argument
				break;
			}
		}
		
		try{
		       OpenLRule rule = new OpenLRule();
		       rule.setRuleFactory(ruleFactory).setRuleName(ruleMethod);
		       return rule;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		} else {
			return null;
		}
}

	
	public OpenLRuleManager(String excelfile) {
		if (!isInitialized()) {
		RulesEngineFactory<?> rulesFactory =  new RulesEngineFactory<Object>(excelfile);

		// Generate ValueObject class definitions, build (load into classloader) and store in datatypeMap
		Map<String, IOpenClass> map = rulesFactory.getCompiledOpenClass().getTypes();
		
		int built=0;
		
		LinkedHashMap<String, ArrayList<String>> complexDataTypes = new LinkedHashMap <String, ArrayList<String>>();
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			Boolean isPrimitiveOnly = true;
			Boolean isDomainType = false;
			String type = iterator.next();
			logger.debug("map entry is " + type);
			IOpenClass c = map.get(type);
			ArrayList<String> usedComplexTypes = new ArrayList<String>();
			
			String classTypeName = c.getClass().getName();
			logger.debug(type + " is " + c.getClass().getName());
			
			if (classTypeName.equalsIgnoreCase("org.openl.types.impl.DomainOpenClass")) { 
				// A domain type example is the Gender datatype in Tutorial #2, 
				// where it's basically an alias to a String value with the domain value of Male or Female
				DomainOpenClass d = (DomainOpenClass) c;
				String domainClassType = d.getInstanceClass().getName();
				if (baseTypes.contains(domainClassType)) { // Check to see if it's based off a base/primitive type
					aliasTypeMap.put(c.getName(), domainClassType);
					logger.debug("Storing " +c.getName() + " as " + domainClassType);
					isDomainType = true;
				} else { // Not sure what to do in the case of complex domain value, need sample use case to investigate if such thing exists
					logger.debug("A domain class that is not a base type, don't know what to do with " + type);
				}
				logger.debug(d.getInstanceClass().getName());
				logger.debug(d.getDomain().toString());
			} else if (classTypeName.equalsIgnoreCase("org.openl.rules.lang.xls.types.DatatypeOpenClass")) {
				// Check to see if the Datatype uses non primitive/base types
				Map<String, IOpenField> fields = c.getDeclaredFields();
				for (Iterator<String> iterator2 = fields.keySet().iterator(); iterator2.hasNext();) {
					String fieldName = (String) iterator2.next();
					String fieldType = fields.get(fieldName).getType().toString();
					logger.debug("fieldName: " + fieldName + " fieldType: " + fieldType);
					if (!baseTypes.contains(fields.get(fieldName).getType().toString()) && !aliasTypeMap.containsKey(fields.get(fieldName).getType().toString()) ) {
						isPrimitiveOnly = false;
						usedComplexTypes.add(fieldType);
					}
				}				
			} else {
				logger.error("Don't know what to do with " + type + " which is a " + classTypeName);
			}

			
			if (isPrimitiveOnly && !isDomainType) {
				logger.debug(c.getName() + " uses only base types, building now ");
				datatypeMap.put(excelfile+"::"+c.getName(), build(c));
				built++;
			} else if (!isDomainType) {
				logger.debug(c.getName() + " uses complex types, to be processed later ");
				complexDataTypes.put(c.getName(), usedComplexTypes);
			} else {
				if (isDomainType) {
					logger.debug("A domain value type : " + c.getName() );
					datatypeMap.put(excelfile+"::"+c.getName(), new ValueObject()); // just put an entry in the datatypeMap to mark is as 'resolved'
					built++;
				} else {
					logger.error("Do not expect to see this, not sure what to do with " + c.getName());
				}
			}
		}
	
		int loop=0;
		int MAX_LOOP = 10;
		while (loop < MAX_LOOP && built < map.size()) {
			for (Iterator<String> iter = complexDataTypes.keySet().iterator(); iter.hasNext();) {
				String typeName = iter.next();
				if (!datatypeMap.containsKey(excelfile+"::"+typeName)) { // if it's not built yet, then let's check to see if its dependencies are all built.
					boolean allDependenciesResolved = true;
					ArrayList<String> dependencies = complexDataTypes.get(typeName);
					for (Iterator<String> iter2 = dependencies.iterator(); iter2.hasNext();) {
						String dependency = iter2.next();
						if (!datatypeMap.containsKey(excelfile+"::"+dependency)) { // if a dependency is still not built yet, we'll have to go back to this one later 
							allDependenciesResolved = false;
							break;
						}
					}
					if (allDependenciesResolved) {
						logger.debug("building " + typeName);						
						datatypeMap.put(excelfile+"::"+typeName, build(map.get("org.openl.this::"+typeName)));
						built++;
					} else {
						logger.debug("postponing the build of " + typeName);
					}
				}
			}
			loop++;
		}
		
		for (Iterator<String> iterator = datatypeMap.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			logger.debug(type + ": " + datatypeMap.get(type));
		}
		this.excelfile = excelfile;
		logger.debug("Initialized " + this.excelfile);
		
		}
	}

	private ValueObject build(IOpenClass c) {
		ValueObject dataType = new ValueObject();
		dataType.setClassName("org.openl.generated.beans." + c.getName());

		Map<String, IOpenField> fields = c.getDeclaredFields();
		for (Iterator<String> iterator2 = fields.keySet().iterator(); iterator2.hasNext();) {				
			String fieldName = (String) iterator2.next();
			String fieldTypeName = fields.get(fieldName).getType().toString();
			if (!baseTypes.contains(fieldTypeName)) {
				if (aliasTypeMap.containsKey(fieldTypeName)) {
					fieldTypeName = aliasTypeMap.get(fieldTypeName);
				} else {
					fieldTypeName = "org.openl.generated.beans." + fieldTypeName;					
				}
			}
			dataType.addField(fieldName, fieldTypeName);
		}
		dataType.build();
		return dataType;
	}
	
}
