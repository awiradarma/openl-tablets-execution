package andre.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.openl.rules.runtime.RulesEngineFactory;

public class SampleExecution {

	public static void main(String[] args) throws Exception {
		RulesEngineFactory<Simple > rulesFactory =
			       new RulesEngineFactory<Simple>("/Users/awiradarma/openl/DT.xlsx",
			                                        Simple.class);
			Simple rules = (Simple) rulesFactory.newInstance();
			System.out.println(rules.Greeting(13));

			OpenLRule greetingRule = findRule("/Users/awiradarma/openl/DT.xlsx", "Greeting", "java.lang.Integer");
		System.out.println(greetingRule.execute(19));
		
		OpenLRule carPriceRule = findRule("/Users/awiradarma/openl/DT.xlsx", "CarPrice", "java.lang.String", "java.lang.String", "java.lang.String");
		System.out.println(carPriceRule.execute("Belarus","BMW","Z4 sDRIVE28i"));
		
	}

	public static OpenLRule findRule(String filename, String ruleName, String...ruleParameterTypes ) throws Exception {
			RulesEngineFactory<?> rulesFactory =  new RulesEngineFactory<Object>(filename);
			Object ruleFactory = rulesFactory.newInstance();
			
			ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
			for(String value : ruleParameterTypes)
			{
				Class<?> c = Class.forName(value);
				classList.add(c);
			}

			Class<?>[] classes = new Class[classList.size()];
			classes = classList.toArray(classes);
			
			Class<?> clazz = rulesFactory.getInterfaceClass();
			try{
			       Method method = clazz.getMethod(ruleName, classes);
			       OpenLRule rule = new OpenLRule();
			       rule.setRuleFactory(ruleFactory).setRuleName(method);
			       return rule;
			}catch(Exception e){
				return null;
			}
	}
	
}
