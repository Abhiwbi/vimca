package com.example.vimca.Model;


import java.time.LocalDateTime;

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
@SQLDelete(sql = "UPDATE video_chat_history SET deleted = true WHERE history_id=?")
@FilterDef(name = "deletedVideoChatHistoryFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedVideoChatHistoryrFilter", condition = "deleted = :isDeleted")
public class VideoChatHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long historyId;
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "appUserId", scope = AppUser.class)
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_one_id", nullable = false)
	private AppUser userOne;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "appUserId", scope = AppUser.class)
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_two_id", nullable = false)
	private AppUser userTwo;

	private LocalDateTime startTime = LocalDateTime.now();
	private LocalDateTime endTime;
	private String connectionDuration;

	@JsonIgnore
	private boolean deleted = Boolean.FALSE;

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public AppUser getUserOne() {
		return userOne;
	}

	public void setUserOne(AppUser userOne) {
		this.userOne = userOne;
	}

	public AppUser getUserTwo() {
		return userTwo;
	}

	public void setUserTwo(AppUser userTwo) {
		this.userTwo = userTwo;
	}

	

	public String getConnectionDuration() {
		return connectionDuration;
	}

	public void setConnectionDuration(String connectionDuration) {
		this.connectionDuration = connectionDuration;
	}

	public boolean isDeleted() {
		return deleted;
	}

	
	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public VideoChatHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}