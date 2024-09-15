package com.example.vimca.dto;

public class CommissionDto {
	
	private Long commissionId;
	private String brokerName;
	private String brokerId;
	private String commissionAdded;
	private String userId;
	private String userName;
	private String userCountry;
	private String purchaseType;
	public Long getCommissionId() {
		return commissionId;
	}
	public void setCommissionId(Long commissionId) {
		this.commissionId = commissionId;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}
	public String getCommissionAdded() {
		return commissionAdded;
	}
	public void setCommissionAdded(String commissionAdded) {
		this.commissionAdded = commissionAdded;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserCountry() {
		return userCountry;
	}
	public void setUserCountry(String userCountry) {
		this.userCountry = userCountry;
	}
	public String getPurchaseType() {
		return purchaseType;
	}
	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}
	public CommissionDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
