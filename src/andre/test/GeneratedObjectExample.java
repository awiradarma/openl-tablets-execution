package andre.test;

import java.util.Date;

public class GeneratedObjectExample {

	public static void main(String[] args) throws Exception {
		GeneratedObject o = new GeneratedObject();
		o.setClassName("andre.dynamic.Loan").addField("loanID", "java.lang.Integer")
		.addField("loanOwner", "java.lang.String")
		.addField("loanAmount", "java.lang.Double")
		.addField("effectiveDate", "java.util.Date")
		.build();
		
		o.set("loanOwner", "Joe");
		o.set("loanID", 12345);
		o.set("effectiveDate", new Date());
		o.set("loanAmount", 25000.00);
		
		System.out.println(o.get("loanOwner") + " owes " + o.get("loanAmount"));
	}

}
