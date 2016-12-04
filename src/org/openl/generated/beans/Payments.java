package org.openl.generated.beans;

import java.util.Arrays;

public class Payments {
	private Double[] amounts;
	private Double[] interests;
	private Integer number;
	public Double[] getAmounts() {
		return amounts;
	}
	public void setAmounts(Double[] amounts) {
		this.amounts = amounts;
	}
	public Double[] getInterests() {
		return interests;
	}
	public void setInterests(Double[] interests) {
		this.interests = interests;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "Payments [amounts=" + Arrays.toString(amounts) + ", interests="
				+ Arrays.toString(interests) + ", number=" + number + "]";
	}

}
