package andre.test;

import java.lang.reflect.Method;

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
		try {
			//System.out.println(this.ruleFactory);
			return this.rule.invoke(this.ruleFactory, objects);
		} catch (Exception e) {
			e.printStackTrace();
			throw (e.getCause());
			//return null;
		}
	}
}
