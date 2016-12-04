package org.openl.generated.beans;

import java.util.Date;

public class Policy {

	private String policyID;
	private String driverID;
	private String vehicleID;
	private Date effectiveDate;
	private Boolean isRenewal;
	public String getPolicyID() {
		return policyID;
	}
	public void setPolicyID(String policyID) {
		this.policyID = policyID;
	}
	public String getDriverID() {
		return driverID;
	}
	public void setDriverID(String driverID) {
		this.driverID = driverID;
	}
	public String getVehicleID() {
		return vehicleID;
	}
	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Boolean getIsRenewal() {
		return isRenewal;
	}
	public void setIsRenewal(Boolean isRenewal) {
		this.isRenewal = isRenewal;
	}
	
	
}
