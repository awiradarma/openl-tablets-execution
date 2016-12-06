package andre.test;

public class SampleConsumer {

	public static void main(String[] args) throws Throwable { 
	
		OpenLRule greetingRule = OpenLRuleManager.findRule("/Users/andre/openl-tablets/DT.xlsx", "Greeting");
		System.out.println(greetingRule.execute(19));
		
		OpenLRule carPriceRule = OpenLRuleManager.findRule("/Users/andre/openl-tablets/DT.xlsx", "CarPrice");
		System.out.println(carPriceRule.execute("Belarus","BMW","Z4 sDRIVE28i"));

		OpenLRuleManager.initialize("/Users/andre/openl-tablets/DT4.xlsx");
		ValueObject myExpense = OpenLRuleManager.newValueObject("/Users/andre/openl-tablets/DT4.xlsx", "Expense");
		
		myExpense.set("area", "Hardware")
			.set("money", Double.valueOf(150000.00));	
		OpenLRule expenseRule = OpenLRuleManager.findRule("/Users/andre/openl-tablets/DT4.xlsx", "NeedApprovalOf");
		System.out.println(expenseRule.execute(myExpense));

	}

}
