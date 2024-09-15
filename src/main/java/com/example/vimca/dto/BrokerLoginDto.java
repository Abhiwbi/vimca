package com.example.vimca.dto;

public class BrokerLoginDto {
	private Long brokerId;

	private String name;
	private String email;
	private String password;
	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public BrokerLoginDto() {
		super();
		// TODO Auto-generated constructor stub
	}

}