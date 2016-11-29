package andre.test;

import java.lang.reflect.Method;
import org.openl.generated.beans.Expense;

import org.openl.rules.runtime.RulesEngineFactory;

public class SampleExecution {

	public static void main(String[] args) throws Exception {
		RulesEngineFactory<Simple > rulesFactory =
			       new RulesEngineFactory<Simple>("/Users/andre/openl-tablets/DT.xlsx",
			                                        Simple.class);
			Simple rules = (Simple) rulesFactory.newInstance();
			System.out.println(rules.Greeting(13));

			OpenLRule greetingRule = findRule("/Users/andre/openl-tablets/DT.xlsx", "Greeting", "java.lang.Integer");
		System.out.println(greetingRule.execute(19));
		
		OpenLRule carPriceRule = findRule("/Users/andre/openl-tablets/DT.xlsx", "CarPrice", "java.lang.String", "java.lang.String", "java.lang.String");
		System.out.println(carPriceRule.execute("Belarus","BMW","Z4 sDRIVE28i"));

		Expense myExpense = new Expense();
		myExpense.setArea("Hardware");
		myExpense.setMoney(Double.valueOf(150000));
		OpenLRule expenseRule = findRule("/Users/andre/openl-tablets/DT4.xlsx", "NeedApprovalOf", "org.openl.generated.beans.Expense");
		System.out.println(expenseRule.execute(myExpense));
	}

	public static OpenLRule findRule(String filename, String ruleName, String...ruleParameterTypes ) throws Exception {
			RulesEngineFactory<?> rulesFactory =  new RulesEngineFactory<Object>(filename);
			Object ruleFactory = rulesFactory.newInstance();
			Method ruleMethod = null;
			Method[] methods = rulesFactory.getInterfaceClass().getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equalsIgnoreCase(ruleName)) {
					System.out.println("Found a match!");
					ruleMethod = methods[i]; // assume / create standard that there should not be more than one rule with the same name and different argument
					break;
				}
			}
			
			try{
			       OpenLRule rule = new OpenLRule();
			       rule.setRuleFactory(ruleFactory).setRuleName(ruleMethod);
			       return rule;
			}catch(Exception e){
				return null;
			}
	}
	
}
