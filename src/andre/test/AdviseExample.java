package andre.test;

public class AdviseExample {

	public static void main(String[] args) throws Throwable {
		System.out.println("Parsing the excel file");
		OpenLRuleManager manager = new OpenLRuleManager("/Users/andre/Documents/Advise.xlsx");
		System.out.println("Finished parsing, ready to execute now..");
		OpenLRule rule = manager.findRule("NewUsedOrLeaseRule");
		System.out.println(rule.execute(4,20000,0.7,1000));
		
//		ValueObject member = manager.newValueObject("Member");
//		member.set("debt_to_incomeRatio", 0.2);
//		member.set("emergencySavings", 500);
//		
//		ValueObject vehicle = manager.newValueObject("Vehicle");
//		vehicle.set("turnoverFrequency", 5);
//		vehicle.set("annualMileage", 20000);
//		String[] narratives = (String []) manager.findRule("getNarratives").execute(null);
//		int narrativeID = (Integer) manager.findRule("NewUsedOrLeaseRuleV2").execute(member, vehicle);
//		System.out.println(narratives[narrativeID]);
	}

}
