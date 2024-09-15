package com.example.vimca.Broker;

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

import com.example.vimca.Model.AppUser;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@SQLDelete(sql = "UPDATE commission SET deleted = true WHERE commission_id=?")
@FilterDef(name = "deletedCommissionFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedCommissionFilter", condition = "deleted = :isDeleted")

public class Commission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commissionId;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "appUserId", scope = AppUser.class)
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "app_user_id")
	private AppUser appUser;

	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "brokerId", scope = Broker.class)
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "broker_id", nullable = false)
	private Broker passBroker;
	
	private String purchaseType;

	private Double newCommission;

	private LocalDateTime createdDate;

	@JsonIgnore
	private boolean deleted = Boolean.FALSE;

	public Long getCommissionId() {
		return commissionId;
	}

	public void setCommissionId(Long commissionId) {
		this.commissionId = commissionId;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public Broker getPassBroker() {
		return passBroker;
	}

	public void setPassBroker(Broker passBroker) {
		this.passBroker = passBroker;
	}

	public Double getNewCommission() {
		return newCommission;
	}

	public void setNewCommission(Double newCommission) {
		this.newCommission = newCommission;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isDeleted() {
		return deleted;
	}

	
	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Commission() {
		super();
		// TODO Auto-generated constructor stub
	}

}
