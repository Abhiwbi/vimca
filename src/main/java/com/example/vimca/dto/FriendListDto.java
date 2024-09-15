package com.example.vimca.dto;

public class FriendListDto {
	  private Long friendId;
	    private Long userId;
	    private Long friendUserId;
	    private String name;
	    private String email;
	    private String mobile;
	    private String city;
	    private String profileImageUrl;
	    private boolean isBanned;
		public Long getFriendId() {
			return friendId;
		}
		public void setFriendId(Long friendId) {
			this.friendId = friendId;
		}
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public Long getFriendUserId() {
			return friendUserId;
		}
		public void setFriendUserId(Long friendUserId) {
			this.friendUserId = friendUserId;
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
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getProfileImageUrl() {
			return profileImageUrl;
		}
		public void setProfileImageUrl(String profileImageUrl) {
			this.profileImageUrl = profileImageUrl;
		}
		public boolean isBanned() {
			return isBanned;
		}
		public void setBanned(boolean isBanned) {
			this.isBanned = isBanned;
		}
		public FriendListDto() {
			super();
			// TODO Auto-generated constructor stub
		}

	    
}
