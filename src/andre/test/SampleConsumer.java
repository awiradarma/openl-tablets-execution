package andre.test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SampleConsumer {

	public static void main(String[] args) throws Throwable { 
	
		OpenLRuleManager DT_RuleManager = new OpenLRuleManager("/Users/andre/openl-tablets/DT.xlsx");
		OpenLRule greetingRule = DT_RuleManager.findRule("Greeting");
		System.out.println(greetingRule.execute(19));
		
		OpenLRule carPriceRule = DT_RuleManager.findRule("CarPrice");
		System.out.println(carPriceRule.execute("Belarus","BMW","Z4 sDRIVE28i"));

		OpenLRuleManager DT4_RM = new OpenLRuleManager("/Users/andre/openl-tablets/DT4.xlsx");

		String input = "{\"area\":\"Hardware\",\"money\":150000.0}";
		Class expenseClass = DT4_RM.obtainClassDefinition("Expense");
		ObjectMapper mapper = new ObjectMapper();		
		Object myExpense = mapper.readValue(input, expenseClass);
		
		ValueObject myExpense1 = DT4_RM.newValueObject("Expense");
		myExpense1.set("area", "Hardware").set("money", 150000.0);
//		System.out.println(mapper.writeValueAsString(myExpense1));
			
		OpenLRule expenseRule = DT4_RM.findRule("NeedApprovalOf");
		System.out.println(expenseRule.execute(myExpense));
		System.out.println(expenseRule.execute(myExpense1));

//		String s = "Hello world";
//		System.out.println(mapper.writeValueAsString(s));
	}

}
