package andre.test;

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
				if (objects[i].getClass().getTypeName().equalsIgnoreCase("andre.test.ValueObject")) {
//					System.out.println("Converting..");
					newArgs[i] = ((ValueObject) objects[i]).getObject();
				} else {
					newArgs[i] = objects[i];
				}
//				System.out.println("arg["+i+"]" + newArgs[i] + " : " + newArgs[i].getClass().getName());
			} 
		}
		try {
//			Method[] ms = this.ruleFactory.getClass().getDeclaredMethods();
//			for (int i = 0; i < ms.length; i++) {
//				Method method = ms[i];
//				if (method.getName().equalsIgnoreCase(this.rule.getName())) {
//					System.out.println(getParameterTypes(method));
//					System.out.println(getParameterTypes(this.rule));
//				}
//			}
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
