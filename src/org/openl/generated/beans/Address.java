package org.openl.generated.beans;

public class Address {
	private String street1;
	private String street2;
	private String street;
	ZipCode zip;
	USState state;
	String city;
	public String getStreet1() {
		return street1;
	}
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public ZipCode getZip() {
		return zip;
	}
	public void setZip(ZipCode zip) {
		this.zip = zip;
	}
	public USState getState() {
		return state;
	}
	public void setState(USState state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public String toString() {
		return "Address [street1=" + street1 + ", street2=" + street2
				+ ", street=" + street + ", zip=" + zip + ", state=" + state
				+ ", city=" + city + "]";
	}
	

}
