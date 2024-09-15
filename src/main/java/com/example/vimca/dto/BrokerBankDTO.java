package com.example.vimca.dto;

public class BrokerBankDTO {
	private Long brokerId;

	private String name;
	private String email;
	private String mobile;
	private double lifeTimeCommission = 0.0;
	private double balanceCommission = 0.0;

	// Bank account details for international payments
	private String bankAccountNumber;
	private String bankName;
	private String bankBranch;
	private String swiftCode; // Required for international payments
	private String ifscCode;
	// payapal
	private String paypalEmailId;
	public Long getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(Long brokerId) {
		this.brokerId = brokerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public double getLifeTimeCommission() {
		return lifeTimeCommission;
	}
	public void setLifeTimeCommission(double lifeTimeCommission) {
		this.lifeTimeCommission = lifeTimeCommission;
	}
	public double getBalanceCommission() {
		return balanceCommission;
	}
	public void setBalanceCommission(double balanceCommission) {
		this.balanceCommission = balanceCommission;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	public String getSwiftCode() {
		return swiftCode;
	}
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getPaypalEmailId() {
		return paypalEmailId;
	}
	public void setPaypalEmailId(String paypalEmailId) {
		this.paypalEmailId = paypalEmailId;
	}
	public BrokerBankDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
