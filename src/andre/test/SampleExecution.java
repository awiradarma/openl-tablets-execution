package andre.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

import org.openl.generated.beans.Address;
import org.openl.generated.beans.Customer;
import org.openl.generated.beans.Driver;
import org.openl.generated.beans.Expense;
import org.openl.generated.beans.Loan;
import org.openl.generated.beans.Loss;
import org.openl.generated.beans.Payments;
import org.openl.generated.beans.Person;
import org.openl.generated.beans.Policy;
import org.openl.meta.DoubleValue;
import org.openl.rules.runtime.RulesEngineFactory;

public class SampleExecution {

	public static void main(String[] args) throws Throwable {
//		RulesEngineFactory<Simple > rulesFactory =
//			       new RulesEngineFactory<Simple>("/Users/andre/openl-tablets/DT.xlsx",
//			                                        Simple.class);
//			Simple rules = (Simple) rulesFactory.newInstance();
//			System.out.println(rules.Greeting(13));
//
//			OpenLRule greetingRule = findRule("/Users/andre/openl-tablets/DT.xlsx", "Greeting", "java.lang.Integer");
//		System.out.println(greetingRule.execute(19));
//		
//		OpenLRule carPriceRule = findRule("/Users/andre/openl-tablets/DT.xlsx", "CarPrice", "java.lang.String", "java.lang.String", "java.lang.String");
//		System.out.println(carPriceRule.execute("Belarus","BMW","Z4 sDRIVE28i"));
//
//		Expense myExpense = new Expense();
//		myExpense.setArea("Hardware");
//		myExpense.setMoney(Double.valueOf(150000));
//		OpenLRule expenseRule = findRule("/Users/andre/openl-tablets/DT4.xlsx", "NeedApprovalOf", "org.openl.generated.beans.Expense");
//		System.out.println(expenseRule.execute(myExpense));
		
//		Driver driver = new Driver();
//		driver.setAge(35);
//		driver.setGender("Male");
//		driver.setMaritalStatus("Married");
//		driver.setDrivingExperience(11);
//		driver.setInfractionPoints(5);
//		driver.setCreditRating("Good");
//		driver.setHasGoodDriverStatus(Boolean.TRUE);
//		OpenLRule driverScoreRule = findRule("/Users/andre/openl-tablets/DT4.xlsx", "DriverScoring", "whatever");
//		System.out.println(driverScoreRule.execute(driver));
//
//		// to obtain values from a Data table, the 'rule name' is get + <data table name>, so use getPhrases for phrases data table
//		String[] phrases = null;
//		OpenLRule phrasesList = findRule("/Users/andre/openl-tablets/DT2.xlsx", "getPhrases", "whatever");
//		phrases = (String[]) phrasesList.execute(null);
//		for (int k = 0; k < phrases.length; k++ ) {
//			System.out.println(phrases[k]);
//		}
//		
//		OpenLRule rangesList = findRule("/Users/andre/openl-tablets/DT2.xlsx", "getRanges", "whatever");
//		System.out.println(rangesList.execute(null).getClass());
//		
//		OpenLRule personList = findRule("/Users/andre/openl-tablets/DT2.xlsx", "getPerson3", "whatever");
//		Person[] persons = (Person[]) personList.execute(null);
//		for (int i = 0; i < persons.length; i++) {
//			System.out.println(persons[i].getName() + ", " + persons[i].getGender());
//		}
//		
//		OpenLRule customerList = findRule("/Users/andre/openl-tablets/DT2.xlsx", "getCustomers", "whatever");
//		//System.out.println(customerList.execute(null).getClass());
//		Customer[] customers = (Customer[]) customerList.execute(null);
//		for (int i = 0; i < customers.length; i++) {
//			Customer customer = customers[i];
//			System.out.println(customer.getSsn());
//		}
//
		OpenLRule Hr24ToAmPmRule = findRule("/Users/andre/openl-tablets/DT3.xlsx", "Hr24ToAmPm", "whatever");
		System.out.println(Hr24ToAmPmRule.execute(17));
//
//		OpenLRule RegionRule = findRule("/Users/andre/openl-tablets/DT3.xlsx", "Region2", "whatever");
//		System.out.println(RegionRule.execute("TX"));
//
//		OpenLRule Greeting3Rule = findRule("/Users/andre/openl-tablets/DT3.xlsx", "Greeting3", "whatever");
//		System.out.println(Greeting3Rule.execute(17));
//
//		OpenLRule AddressList = findRule("/Users/andre/openl-tablets/DT3.xlsx", "getAddresses1", "whatever");
//		Address[] addresses = (Address[]) AddressList.execute(null);
//		for (int i = 0; i < addresses.length; i++) {
//			Address address = addresses[i];
//			System.out.println(address);
//		}


		// An error within tbasic table basically throws a runtime exception
//		OpenLRule ValidationPolicy = findRule("/Users/andre/openl-tablets/DT5.xlsx", "ValidationPolicy", "whatever");
//		Policy p = new Policy();
//		try {
//			System.out.println(ValidationPolicy.execute(p));
//		} catch (Throwable e) {
//			System.out.println(e.getCause().getMessage());
//		}

		OpenLRule factorialRule = findRule("/Users/andre/openl-tablets/DT5.xlsx", "Factorial", "whatever");
		System.out.println(factorialRule.execute(5));
		
		Loan loan = new Loan();
		loan.setAmount(25000.00);
		loan.setRate(0.04);
		loan.setTerm(5);
		loan.setPurpose("Car");
		OpenLRule listOfPaymentsRule = findRule("/Users/andre/openl-tablets/DT5.xlsx", "ListLoanPayments", "whatever");
		Payments payments = (Payments) listOfPaymentsRule.execute(loan);
		System.out.println(payments);
		OpenLRule totalLoanPaymentRule = findRule("/Users/andre/openl-tablets/DT5.xlsx", "TotalLoanPayment", "whatever");
		System.out.println(totalLoanPaymentRule.execute(loan));
		
		OpenLRule incomeForecastRule = findRule("/Users/andre/openl-tablets/DT6.xlsx", "IncomeForecast", "whatever");
		Object o = incomeForecastRule.execute(new Double(0.10), new Double(25.0));
		// when you execute a spreadsheet table that returns a SpreadsheetResult, the generated class will also have a bunch of getter and setter, based on row names and column names
		System.out.println(retrieveSpreadsheetResult(o, "$TotalAmount", "$MaxSalary"));

		// the setter method of the returned spreadsheet result object doesn't force a recalculation
		Method m1 = findMethod(o, "set$Year1$Salary");
		Parameter[] params = m1.getParameters();
		for (int i = 0; i < params.length; i++) {
			Parameter parameter = params[i];
			System.out.println(parameter.getType().getName());
		}
		m1.invoke(o, new DoubleValue(50000.0));
		Method m2 = findMethod(o, "get$TotalAmount$MaxSalary");
		System.out.println(m2.invoke(o, null));
		System.out.println(o);
		
		Loss[] losses = new Loss[2];
		losses[0] = new Loss();
		losses[1] = new Loss();
		losses[0].setAmount(1000.0);
		losses[0].setDate(new Date("01/01/2010"));
		losses[0].setType("Liability");
		losses[1].setAmount(500.0);
		losses[1].setDate(new Date("09/19/1999"));
		losses[1].setType("Liability");
		
		OpenLRule lossFreeDiscountCalcRule = findRule("/Users/andre/openl-tablets/DT6.xlsx", "LossFreeDiscountCalc", "whatever");
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
	
	public static OpenLRule findRule(String filename, String ruleName, String...ruleParameterTypes ) throws Exception {
			RulesEngineFactory<?> rulesFactory =  new RulesEngineFactory<Object>(filename);
			Object ruleFactory = rulesFactory.newInstance();
			Method ruleMethod = null;
			Method[] methods = rulesFactory.getInterfaceClass().getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				System.out.println(methods[i].getName());
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
