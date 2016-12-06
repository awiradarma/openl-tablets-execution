package andre.test;

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

public class OpenLRuleManager {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO - change to instance based
		// TODO - validate and refactor to make the code thread safe
		// TODO - figure out how to handle multiple spreadsheets and naming collision
		// TODO - clean up exception/error handling across all classes
		
		initialize("/Users/andre/openl-tablets/DT4.xlsx");
//		initialize("/Users/awiradarma/PlaySheet.xlsx");
		
		
	}

	private static ConcurrentHashMap<String,ValueObject> datatypeMap = new ConcurrentHashMap<String, ValueObject>();
	private static List<String> baseTypes = Arrays.asList("java.lang.String", "java.lang.String[]", "java.lang.Integer", "java.lang.Integer[]"
			,"java.lang.Double","java.lang.Double[]","java.lang.Boolean","java.lang.Boolean[]"
			,"java.util.Date","java.util.Date[]");

	public static ValueObject newValueObject(String excelfile, String datatype) {
		if (datatypeMap.containsKey(excelfile+"::"+datatype)) {
			return datatypeMap.get(excelfile+"::"+datatype); //.generateEmptyCopy();
		} else {
			return null;
		}
	}

	public static OpenLRule findRule(String filename, String ruleName) {
		RulesEngineFactory<?> rulesFactory =  new RulesEngineFactory<Object>(filename);
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
}

	
	public static void initialize(String excelfile) {
		RulesEngineFactory<?> rulesFactory =  new RulesEngineFactory<Object>(excelfile);

		// Generate ValueObject class definitions, build (load into classloader) and store in datatypeMap
		Map<String, IOpenClass> map = rulesFactory.getCompiledOpenClass().getTypes();
		
		int built=0;
		
		LinkedHashMap<String, ArrayList<String>> complexDataTypes = new LinkedHashMap <String, ArrayList<String>>();
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			Boolean isPrimitiveOnly = true;
			String type = iterator.next();
			// System.out.println("DEBUG -- map entry is " + type);
			IOpenClass c = map.get(type);
			ArrayList<String> usedComplexTypes = new ArrayList<String>();
			
			// Check to see if the Datatype uses non base field types
			Map<String, IOpenField> fields = c.getDeclaredFields();
			for (Iterator<String> iterator2 = fields.keySet().iterator(); iterator2.hasNext();) {
				String fieldName = (String) iterator2.next();
				String fieldType = fields.get(fieldName).getType().toString();
				if (!baseTypes.contains(fields.get(fieldName).getType().toString())) {
					isPrimitiveOnly = false;
					usedComplexTypes.add(fieldType);
				}
			}
			if (isPrimitiveOnly) {
				// System.out.println("DEBUG -- " + c.getName() + " uses only base types, building now ");
				datatypeMap.put(excelfile+"::"+c.getName(), build(c));
				built++;
			} else {
				// System.out.println("DEBUG -- " + c.getName() + " uses complex types, to be processed later ");
				complexDataTypes.put(c.getName(), usedComplexTypes);
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
						//System.out.println("DEBUG -- building " + typeName);						
						datatypeMap.put(excelfile+"::"+typeName, build(map.get("org.openl.this::"+typeName)));
						built++;
					} else {
						//System.out.println("DEBUG -- postponing the build of " + typeName);
					}
				}
			}	
		}
		
		for (Iterator<String> iterator = datatypeMap.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			System.out.println(type + ": " + datatypeMap.get(type));
		}

	}

	private static ValueObject build(IOpenClass c) {
		ValueObject dataType = new ValueObject();
		dataType.setClassName("org.openl.generated.beans." + c.getName());

		Map<String, IOpenField> fields = c.getDeclaredFields();
		for (Iterator<String> iterator2 = fields.keySet().iterator(); iterator2.hasNext();) {				
			String fieldName = (String) iterator2.next();
			String fieldTypeName = fields.get(fieldName).getType().toString();
			if (!baseTypes.contains(fieldTypeName)) {
				fieldTypeName = "org.openl.generated.beans." + fieldTypeName;
			}
			dataType.addField(fieldName, fieldTypeName);
		}
		dataType.build();
		return dataType;
	}
	
}
