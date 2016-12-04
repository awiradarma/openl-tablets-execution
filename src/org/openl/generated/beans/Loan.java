package org.openl.generated.beans;

public class Loan {
	private String purpose;
	private Double amount;
	private Double rate;
	private Integer term;
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Integer getTerm() {
		return term;
	}
	public void setTerm(Integer term) {
		this.term = term;
	}
	@Override
	public String toString() {
		return "Loan [purpose=" + purpose + ", amount=" + amount + ", rate="
				+ rate + ", term=" + term + "]";
	}
	

}
