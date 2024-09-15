package com.example.vimca.dto;

public class AppUserDto {
	
	private String userId;
	private String name;
	private String email;
	private String phone;
	private String country;
	private String language;
	private String gender;
	private String birthDate;
	private String totalgems;
	private String deviceType;
	private String subscriptionType;
	private Boolean isSubscription;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getTotalgems() {
		return totalgems;
	}
	public void setTotalgems(String totalgems) {
		this.totalgems = totalgems;
	}
	public String getSubscriptionType() {
		return subscriptionType;
	}
	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}
	public Boolean getIsSubscription() {
		return isSubscription;
	}
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public void setIsSubscription(Boolean isSubscription) {
		this.isSubscription = isSubscription;
	}
	public AppUserDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
