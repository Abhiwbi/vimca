package com.example.vimca.Model;


public class MatchingConnectionRequest {
	private Long connectionId;
	private boolean isglobleMatch;
	private boolean isGenderSpecific;
    private String preferredGender; 
	private String createdDate;
	private String updatedDate;
	public Long getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(Long connectionId) {
		this.connectionId = connectionId;
	}
	public boolean isIsglobleMatch() {
		return isglobleMatch;
	}
	public void setIsglobleMatch(boolean isglobleMatch) {
		this.isglobleMatch = isglobleMatch;
	}
	public boolean isGenderSpecific() {
		return isGenderSpecific;
	}
	public void setGenderSpecific(boolean isGenderSpecific) {
		this.isGenderSpecific = isGenderSpecific;
	}
	public String getPreferredGender() {
		return preferredGender;
	}
	public void setPreferredGender(String preferredGender) {
		this.preferredGender = preferredGender;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public MatchingConnectionRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}