package andre.test;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class OpenLRule {
	private Method rule;
	private Object ruleFactory;

	public OpenLRule() {
		
	}
	
	OpenLRule setRuleName(Method rulename) {
		this.rule = rulename;
		return this;
	}
	
	OpenLRule setRuleFactory(Object rulefactoryname) {
		this.ruleFactory = rulefactoryname;
		return this;
	}
	
	public Object execute(Object...objects ) throws Throwable {
		Object[] newArgs = null;
		if (objects != null) { 
			newArgs = new Object[objects.length];
			for (int i = 0; i < objects.length; i++) {
				String typeName = objects[i].getClass().getTypeName();
				if (typeName.equalsIgnoreCase("andre.test.ValueObject")) {
					// Extract the actual object from the passed in ValueObject
					newArgs[i] = ((ValueObject) objects[i]).getWrappedObject();
				} else if (typeName.equalsIgnoreCase("andre.test.ValueObject[]")) {
					// Convert array of ValueObjects to array of Objects
					ValueObject[] vos =(ValueObject[]) objects[i];
					Object[] array = (Object[]) Array.newInstance(vos[0].getWrappedObject().getClass(), vos.length);
					for (int j = 0; j < vos.length; j++) {
						array[j] = vos[j].getWrappedObject();
					}
					newArgs[i] = array;
				} else {
					newArgs[i] = objects[i];
				}
				//System.out.println("arg["+i+"]" + newArgs[i] + " : " + newArgs[i].getClass().getName());
			} 
		}
		try {
			return this.rule.invoke(this.ruleFactory, newArgs);
		} catch (Exception e) {
			e.printStackTrace();
			throw (e.getCause());
			//return null;
		}
	}
	
	String getParameterTypes(Method m) {
		Parameter[] params = m.getParameters();
		StringBuilder s = new StringBuilder(m.getName());
		s.append(" - numParams:").append(m.getParameterCount()).append("\n");
		for (int i = 0; i < params.length; i++) {
			Parameter parameter = params[i];
			s.append(parameter.getName()).append(":").append(parameter.getType().getName()).append("\n");
		}
		
		return s.toString();
	}
}
