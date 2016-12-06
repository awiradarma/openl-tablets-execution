package andre.test;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.openl.CompiledOpenClass;
import org.openl.meta.DoubleValue;
import org.openl.rules.runtime.RulesEngineFactory;
import org.openl.types.IOpenClass;
import org.openl.types.IOpenField;

public class SampleExecution {

	public static void main(String[] args) throws Throwable {
//		RulesEngineFactory<Simple > rulesFactory =
//			       new RulesEngineFactory<Simple>("/Users/andre/openl-tablets/DT.xlsx",
//			                                        Simple.class);
//			Simple rules = (Simple) rulesFactory.newInstance();
//			System.out.println(rules.Greeting(13));
//
//
//		OpenLRule greetingRule = findRule("/Users/andre/openl-tablets/DT.xlsx", "Greeting");
//		System.out.println(greetingRule.execute(19));
//		
//		OpenLRule carPriceRule = findRule("/Users/andre/openl-tablets/DT.xlsx", "CarPrice");
//		System.out.println(carPriceRule.execute("Belarus","BMW","Z4 sDRIVE28i"));
//
//		ValueObject myExpense = new ValueObject();
//		myExpense.setClassName("org.openl.generated.beans.Expense")
//			.addField("area", "String")
//			.addField("money", "Double").build();
//		
//		myExpense.set("area", "Hardware")
//			.set("money", Double.valueOf(150000.00));	
//		OpenLRule expenseRule = findRule("/Users/andre/openl-tablets/DT4.xlsx", "NeedApprovalOf");
//		System.out.println(expenseRule.execute(myExpense));
		
		// Define and build (load) the Driver value object
//		ValueObject driver = new ValueObject();
//		driver.setClassName("org.openl.generated.beans.Driver")
//			.addField("age", "Integer")
//			.addField("creditRating", "String")
//			.addField("drivingExperience", "Integer")
//			.addField("gender", "String")
//			.addField("hasGoodDriverStatus", "Boolean")
//			.addField("infractionPoints", "Integer")
//			.addField("maritalStatus", "String")
//			.build();
//		
//		driver.set("age", 35)
//			.set("gender", "Male")
//			.set("maritalStatus", "Married")
//			.set("drivingExperience", 11)
//			.set("infractionPoints",5)
//			.set("creditRating", "Good")
//			.set("hasGoodDriverStatus", Boolean.TRUE);
		
// IMPORTANT -- make sure that the value object class definition has been defined and built
// before we try to find a rule in a spreadsheet that contains that datatype.
// Otherwise, openl will generate its own class definition in a separate classloader, resulting in argument type mismatch exception		
//		OpenLRule driverScoreRule = findRule("/Users/andre/openl-tablets/DT4.xlsx", "DriverScoring");
//		System.out.println(driverScoreRule.execute(driver));

//		// to obtain values from a Data table, the 'rule name' is get + <data table name>, so use getPhrases for phrases data table
//		String[] phrases = null;
//		OpenLRule phrasesList = findRule("/Users/andre/openl-tablets/DT2.xlsx", "getPhrases");
//		phrases = (String[]) phrasesList.execute(null);
//		for (int k = 0; k < phrases.length; k++ ) {
//			System.out.println(phrases[k]);
//		}
//		
//		OpenLRule rangesList = findRule("/Users/andre/openl-tablets/DT2.xlsx", "getRanges");
//		System.out.println(rangesList.execute(null).getClass());
//		
//		ValueObject person = new ValueObject();
//		person.setClassName("org.openl.generated.beans.Person")
//			.addField("name", "String")
//			.addField("gender", "String")
//			.addField("ssn", "String")
//			.addField("maritalStatus", "String")
//			.addField("dob", "Date")
//			.build();
//		OpenLRule personList = findRule("/Users/andre/openl-tablets/DT2.xlsx", "getPerson3");
//		Object[] persons = (Object[])personList.execute(null);
//		System.out.println(persons.getClass().getName());
//		for (int i = 0; i < persons.length; i++) {
//			ValueObject o = new ValueObject(persons[i]);
//			System.out.println(o.get("name") + ", " + o.get("gender") + ", " + o.getObject().getClass().getName());
//		}
//		
//		OpenLRule customerList = findRule("/Users/andre/openl-tablets/DT2.xlsx", "getCustomers");
//		Object[] customers = (Object[]) customerList.execute(null);
//		for (int i = 0; i < customers.length; i++) {
//			ValueObject customer = new ValueObject(customers[i]);
//			System.out.println(customer.get("ssn"));
//		}
//
//		OpenLRule Hr24ToAmPmRule = findRule("/Users/andre/openl-tablets/DT3.xlsx", "Hr24ToAmPm");
//		System.out.println(Hr24ToAmPmRule.execute(17));
//
//		OpenLRule RegionRule = findRule("/Users/andre/openl-tablets/DT3.xlsx", "Region2");
//		System.out.println(RegionRule.execute("TX"));
//
//		OpenLRule Greeting3Rule = findRule("/Users/andre/openl-tablets/DT3.xlsx", "Greeting3");
//		System.out.println(Greeting3Rule.execute(17));
//
//		OpenLRule AddressList = findRule("/Users/andre/openl-tablets/DT3.xlsx", "getAddresses1");
//		Object[] addresses = (Object[]) AddressList.execute(null);
//		for (int i = 0; i < addresses.length; i++) {
//			ValueObject address = new ValueObject(addresses[i]);
//			System.out.println(address);
//		}

//		ValueObject p = new ValueObject();
//		p.setClassName("org.openl.generated.beans.Policy")
//			.addField("policyID", "String")
//			.addField("driverID", "String")
//			.addField("vehicleID", "String")
//			.addField("effectiveDate", "Date")
//			.addField("isRenewal", "Boolean")
//			.build();
//		p.set("policyID","12345"); // if there's no driver specified, a runtime exception would be thrown

		// An error within tbasic table basically throws a runtime exception
//		OpenLRule ValidationPolicy = findRule("/Users/andre/openl-tablets/DT5.xlsx", "ValidationPolicy");
//		try {
//			System.out.println(ValidationPolicy.execute(p.getObject()));
//		} catch (Throwable e) {
//			System.out.println(e.getCause().getMessage());
//		}
//
		OpenLRule factorialRule = findRule("/Users/andre/openl-tablets/DT5.xlsx", "Factorial");
//		System.out.println(factorialRule.execute(5));
//		
//		ValueObject loan = new ValueObject();
//		loan.setClassName("org.openl.generated.beans.Loan")
//			.addField("amount", "Double")
//			.addField("purpose", "String")
//			.addField("rate", "Double")
//			.addField("term", "Integer")
//			.build();
//		
//		loan.set("amount", 25000.00)
//			.set("rate", 0.04)
//			.set("term", 5)
//			.set("purpose", "Car");
//		
//		OpenLRule listOfPaymentsRule = findRule("/Users/andre/openl-tablets/DT5.xlsx", "ListLoanPayments");
//
//		ValueObject payments = new ValueObject(listOfPaymentsRule.execute(loan));
//		System.out.println(payments);
//		OpenLRule totalLoanPaymentRule = findRule("/Users/andre/openl-tablets/DT5.xlsx", "TotalLoanPayment");
//		System.out.println(totalLoanPaymentRule.execute(loan));
//		
//		OpenLRule incomeForecastRule = findRule("/Users/andre/openl-tablets/DT6.xlsx", "IncomeForecast");
//		Object o = incomeForecastRule.execute(new Double(0.10), new Double(25.0));
//		
//		ValueObject sheetResult = new ValueObject(o);
//		System.out.println("VO: " + sheetResult.get("$TotalAmount$MaxSalary"));
//		System.out.println("VO: " + sheetResult.get("$Year1$MaxSalary"));
		// when you execute a spreadsheet table that returns a SpreadsheetResult, the generated class will also have a bunch of getter and setter, based on row names and column names
//		System.out.println(retrieveSpreadsheetResult(o, "$TotalAmount", "$MaxSalary"));

		// the setter method of the returned spreadsheet result object doesn't force a recalculation
//		Method m1 = findMethod(o, "set$Year1$Salary");
//		Parameter[] params = m1.getParameters();
//		for (int i = 0; i < params.length; i++) {
//			Parameter parameter = params[i];
//			System.out.println(parameter.getType().getName());
//		}
//		m1.invoke(o, new DoubleValue(50000.0));
//		Method m2 = findMethod(o, "get$TotalAmount$MaxSalary");
//		System.out.println(m2.invoke(o, null));
//		System.out.println(o);
		
		ValueObject loss1 = new ValueObject();
		loss1.setClassName("org.openl.generated.beans.Loss");
		loss1.addField("amount", "Double")
			.addField("date", "Date")
			.addField("type", "String")
			.build();
		ValueObject loss2 = loss1.generateEmptyCopy();
		loss1.set("amount", 1000.0)
			.set("date", new Date("01/01/2010"))
			.set("type", "Liability");
		loss2.set("amount", 500.0)
			.set("date", new Date("09/19/1999"))
			.set("type", "Liability");

		Object array = Array.newInstance(loss1.getObject().getClass(), 2);
		Object[] losses = (Object[]) array;
		losses[0] = loss1.getObject();
		losses[1] = loss2.getObject();
		
		OpenLRule lossFreeDiscountCalcRule = findRule("/Users/andre/openl-tablets/DT6.xlsx", "LossFreeDiscountCalc");
		System.out.println(lossFreeDiscountCalcRule.execute(losses, new Date("12/26/2012")));
	
	}

	public static Method findMethod(Object o, String methodName) {
		Method[] methods = o.getClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getName().equalsIgnoreCase(methodName)) return method;
		}
		return null;
	}
	
	public static Object retrieveSpreadsheetResult(Object spreadsheetResult, String columnName, String rowName) throws Exception {
		String methodName = (new StringBuilder("get")).append(columnName).append(rowName).toString();
		//Method[] methods = spreadsheetResult.getClass().getDeclaredMethods();
		Method m = findMethod(spreadsheetResult, methodName);
//		for (int i = 0; i < methods.length; i++) {
//			Method method = methods[i];
//			System.out.println(method.getName());
//			if (method.getName().equals(methodName)) {
//				m = method;
//				// break;
//			}
//		}
		
		if (m != null) {
			return m.invoke(spreadsheetResult, null);
		} else {
			return null;
		}
	}
	
	public static OpenLRule findRule(String filename, String ruleName) throws Exception {
			RulesEngineFactory<?> rulesFactory =  new RulesEngineFactory<Object>(filename);
			Object ruleFactory = rulesFactory.newInstance();
			Method ruleMethod = null;
			Method[] methods = rulesFactory.getInterfaceClass().getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
//				System.out.println(methods[i].getName());
				if (methods[i].getName().equalsIgnoreCase(ruleName)) {
//					System.out.println("Found a match for " + ruleName);
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
	
}
