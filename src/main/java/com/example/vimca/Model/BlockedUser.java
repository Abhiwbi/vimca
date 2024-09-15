package com.example.vimca.Model;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@SQLDelete(sql = "UPDATE blocked_user SET deleted = true WHERE blocked_Id=?")
@FilterDef(name = "deletedBlockedUserFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedBlockedUserFilter", condition = "deleted = :isDeleted")
public class BlockedUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long blockedId;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "appUserId", scope = AppUser.class)
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "blocker_user_id", nullable = false)
	private AppUser blocker;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "appUserId", scope = AppUser.class)
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "blocked_user_id", nullable = false)
	private AppUser blocked;

	private String createdDate;
	private String updateDate;
	@JsonIgnore
	private boolean deleted = Boolean.FALSE;
	public Long getBlockedId() {
		return blockedId;
	}
	public void setBlockedId(Long blockedId) {
		this.blockedId = blockedId;
	}
	public AppUser getBlocker() {
		return blocker;
	}
	public void setBlocker(AppUser blocker) {
		this.blocker = blocker;
	}
	public AppUser getBlocked() {
		return blocked;
	}
	public void setBlocked(AppUser blocked) {
		this.blocked = blocked;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public BlockedUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}