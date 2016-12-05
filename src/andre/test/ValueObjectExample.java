package andre.test;

import java.util.Date;

public class ValueObjectExample {

	public static void main(String[] args) throws Exception {
		ValueObject o = new ValueObject();
		o.setClassName("andre.dynamic.Loan").addField("loanID", "Integer")
		.addField("loanOwner", "String")
		.addField("loanAmount", "Double")
		.addField("effectiveDate", "Date")
		.build();
		
		o.set("loanOwner", "Joe");
		o.set("loanID", 12345);
		o.set("effectiveDate", new Date());
		o.set("loanAmount", 25000.00);
		
		System.out.println(o.get("loanOwner") + " owes " + o.get("loanAmount"));

		o = new ValueObject();
		o.setClassName("andre.dynamic.Loan").addField("loanID", "Integer")
		.addField("loanOwner", "String")
		.addField("loanAmount", "Double")
		.addField("effectiveDate", "Date")
		.build();
		
		o.set("loanOwner", "Tom");
		o.set("loanID", 56789);
		o.set("effectiveDate", new Date());
		o.set("loanAmount", 215000.00);
		
		System.out.println(o.get("loanOwner") + " owes " + o.get("loanAmount"));

		System.out.println(o);
		
		o = new ValueObject();
		o.setClassName("andre.dynamic.Person")
		.addField("name", "String")
		.addField("gender", "String")
		.build();
		
		o.set("name", "Joe");
		o.set("gender", "male");
		
		ValueObject o1 = new ValueObject();
		o1.setClassName("andre.dynamic.Employee").addField("person", "andre.dynamic.Person")
		.addField("employeeID", "Integer")
		.build();
		
		o1.set("person", o.getObject());
		o1.set("employeeID", 12);
				
		ValueObject o2 = new ValueObject(o1.get("person"));
		System.out.println("Employee #" + o1.get("employeeID") + " name is " +o2.get("name"));
		
		System.out.println(o1);
	}

}
