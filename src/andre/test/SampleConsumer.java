package andre.test;

import java.lang.reflect.Array;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SampleConsumer {

	public static void main(String[] args) throws Throwable { 

		ObjectMapper mapper = new ObjectMapper(); // A Jackson ObjectMapper
		
		// These spreadsheets are from http://openl-tablets.org/documentation/tutorials - simply renamed for brevity
		OpenLRuleManager DT = new OpenLRuleManager("/Users/andre/openl-tablets/DT.xlsx");
		OpenLRuleManager DT2 = new OpenLRuleManager("/Users/andre/openl-tablets/DT2.xlsx");
		OpenLRuleManager DT3 = new OpenLRuleManager("/Users/andre/openl-tablets/DT3.xlsx");
		OpenLRuleManager DT4 = new OpenLRuleManager("/Users/andre/openl-tablets/DT4.xlsx");
		OpenLRuleManager DT5 = new OpenLRuleManager("/Users/andre/openl-tablets/DT5.xlsx");
		OpenLRuleManager DT6 = new OpenLRuleManager("/Users/andre/openl-tablets/DT6.xlsx");

		// Sample rules from Tutorial 1
		System.out.println(DT.findRule("Greeting4").execute(19));
		
		// A DoubleValue can be wrapped in a ValueObject
		ValueObject result = new ValueObject(DT.findRule("DriverPremium5").execute("Young Driver","Single"));
		System.out.println("Extra premium for a Single, Young Driver is "+ result.get("value"));
		System.out.println("Cost for a BMW Z4 sDRIVE28i in Belarus is " + DT.findRule("CarPrice").execute("Belarus","BMW","Z4 sDRIVE28i"));

		// Sample rules from Tutorial 2
		// A data table can be retrieved using the rule name 'get' + Data table name
		System.out.println(mapper.writeValueAsString(DT2.findRule("getPhrases").execute(null))); 
		System.out.println(mapper.writeValueAsString(DT2.findRule("getPerson3").execute(null))); 
		
		// To convert the returned datatype to objects, simply wrap the returned object with ValueObject
		// in this case, it's an array of Customer
		ValueObject[] customers = ValueObject.generateValueObjectArray((Object[]) DT2.findRule("getCustomers").execute(null));
		for (int i = 0; i < customers.length; i++) {
			System.out.println(customers[i].get("name") + ":" + customers[i].get("billing")); 
		}
		
		// Sample rules from Tutorial 3
		System.out.println("17:00 is the same as " + DT3.findRule("Hr24ToAmPm").execute(17));
		System.out.println("MA state is in region: " + DT3.findRule("Region2").execute("MA"));
		System.out.println("At 19:00 we say : " + DT3.findRule("Greeting4").execute(19));
		System.out.println(mapper.writeValueAsString(DT3.findRule("getAddresses1").execute(null)));
		
		// Sample rules from Tutorial 4
		// To pass in a non primitive/base type argument, you can use use a JSON string that represent the input object
		String input = "{\"area\":\"Hardware\",\"money\":150000.0}";
		// Then obtain the class definition (which we have dynamically define and load into the classloader when we parse the excel spreadsheet
		Class expenseClass = DT4.obtainClassDefinition("Expense");
		// Then use Jackson's ObjectMapper to convert the input string to the proper data type
		Object myExpense = mapper.readValue(input, expenseClass);
		// And pass that object as the argument 
		System.out.println("When we buy hardware that costs 150000, we need the approval of : " + DT4.findRule("NeedApprovalOf").execute(myExpense));

		// We can also build the input object manually
		// First, we create a new value object, by passing the datatype name
		ValueObject myExpense1 = DT4.newValueObject("Expense");
		// ValueObject allows you to set the value of the fields
		myExpense1.set("area", "Software").set("money", 1000.0);
		System.out.println("When we buy software that costs 1000, we need the approval of : " + DT4.findRule("NeedApprovalOf").execute(myExpense1));

		// Another example of building an input object using JSON string
		String driverJSON = "{\"age\":35,"
				+ "\"gender\":\"Male\","
				+ "\"maritalStatus\":\"Married\","
				+ "\"drivingExperience\":11,"
				+ "\"infractionPoints\":5,"
				+ "\"creditRating\":\"Good\","
				+ "\"hasGoodDriverStatus\":true}";
		
		Object driver = mapper.readValue(driverJSON, DT4.obtainClassDefinition("Driver"));
		System.out.println("Driver score : " + DT4.findRule("DriverScoring").execute(driver));
		System.out.println("Driver score rating : " + DT4.findRule("DriverScoreRating").execute(driver));

		
		// Sample rules from Tutorial 5
		ValueObject policy = DT5.newValueObject("Policy");
		policy.set("policyID","12345"); // if there's no driver specified, a runtime exception would be thrown
		// An error within tbasic table basically throws a runtime exception
		OpenLRule ValidationPolicy = DT5.findRule("ValidationPolicy");
		try {
			System.out.println(ValidationPolicy.execute(policy.getWrappedObject()));
		} catch (Throwable e) {
			System.out.println(e.getCause().getMessage());
		}

		System.out.println(DT5.findRule("Factorial").execute(5));
		
		ValueObject loan = DT5.newValueObject("Loan");		
		loan.set("amount", 25000.00).set("rate", 0.04).set("term", 5).set("purpose", "Car");
		System.out.println("Total loan payment is " + DT5.findRule("TotalLoanPayment").execute(loan));
		ValueObject payments = new ValueObject(DT5.findRule("ListLoanPayments").execute(loan));
		
		System.out.println(mapper.writeValueAsString(payments.getWrappedObject()));
		
		Double[] amounts = (Double[]) payments.get("amounts");
		Double[] interests = (Double[]) payments.get("interests");
		for (int i = 0; i < (Integer) payments.get("number"); i++) {
			System.out.println("Payment " + i+1 + " is " + (amounts[i] + interests[i]));
		}
		
		// Sample rules from Tutorial 6
		OpenLRule incomeForecastRule = DT6.findRule("IncomeForecast");		
		// A spreadsheet table typically returns a SpreadsheetResult type, which should also be wrapped in a ValueObject for easy access
		ValueObject sheetResult = new ValueObject(incomeForecastRule.execute(new Double(0.10), new Double(25.0)));
		// To access a particular cell, use the $ColumnName$RowName
		System.out.println("Total Amount of Max Salary : " + sheetResult.get("$TotalAmount$MaxSalary"));
		System.out.println("Year1 Max Salary : " + sheetResult.get("$Year1$MaxSalary"));

		ValueObject[] losses = DT6.newValueObjectArrayOf("Loss", 2);
		losses[0].set("amount", 1000.0)
			.set("date", new Date("01/01/2010"))
			.set("type", "Liability");
		losses[1].set("amount", 500.0)
			.set("date", new Date("09/19/1999"))
			.set("type", "Liability");

		OpenLRule lossFreeDiscountCalcRule = DT6.findRule("LossFreeDiscountCalc");
		System.out.println("Discount obtained based on losses provided : " + lossFreeDiscountCalcRule.execute(losses, new Date("12/26/2012")));

		// Same data using JSON string as the input
		String lossesJSON = "[{\"amount\":1000.0,\"date\":\"2010-01-01T00:00:00.000Z\",\"type\":\"Liability\"},{\"amount\":500.0,\"date\":\"1999-09-19T00:00:00.000Z\",\"type\":\"Liability\"}]";
		Object[] myLosses = (Object[]) Array.newInstance(DT6.obtainClassDefinition("Loss"), 1);
		myLosses = mapper.readValue(lossesJSON, myLosses.getClass());

		System.out.println("Discount obtained based on losses provided (using JSON string input ) : " + lossFreeDiscountCalcRule.execute(myLosses, new Date("12/26/2012")));
		
		ValueObject assetComparisonResult = new ValueObject(DT6.findRule("AssetsCompare").execute(null));
		System.out.println(assetComparisonResult); // SpreadsheetResult already provides a tabular toString() implementation 
		//System.out.println(assetComparisonResult.get("$Value$AssetsCalc2012"));
		//System.out.println(assetComparisonResult.get("$Value$TotalAssets2011"));
		//System.out.println(assetComparisonResult.get("$Value$TotalAssets2012"));
	}

}
