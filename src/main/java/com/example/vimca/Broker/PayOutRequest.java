package com.example.vimca.Broker;


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
@SQLDelete(sql = "UPDATE pay_out_request SET deleted = true WHERE pay_out_Id=?")
@FilterDef(name = "deletedPayOutRequestFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedPayOutRequestFilter", condition = "deleted = :isDeleted")
public class PayOutRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long payOutId;


	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "brokerId", scope = Broker.class)
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "broker_id", nullable = false)
	private Broker broker;
	
	private Double payOutAmount=0.0;
	private String payOutStatus;
	private String createdDate;
	private boolean isPayOutSuccess=Boolean.FALSE;
	private String payOutMode;
	
	@JsonIgnore
	private boolean deleted = Boolean.FALSE;

	public Long getPayOutId() {
		return payOutId;
	}

	public void setPayOutId(Long payOutId) {
		this.payOutId = payOutId;
	}

	public Broker getBroker() {
		return broker;
	}

	public void setBroker(Broker broker) {
		this.broker = broker;
	}

	public Double getPayOutAmount() {
		return payOutAmount;
	}

	public void setPayOutAmount(Double payOutAmount) {
		this.payOutAmount = payOutAmount;
	}

	public String getPayOutStatus() {
		return payOutStatus;
	}

	public void setPayOutStatus(String payOutStatus) {
		this.payOutStatus = payOutStatus;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isPayOutSuccess() {
		return isPayOutSuccess;
	}

	public void setPayOutSuccess(boolean isPayOutSuccess) {
		this.isPayOutSuccess = isPayOutSuccess;
	}

	public String getPayOutMode() {
		return payOutMode;
	}

	public void setPayOutMode(String payOutMode) {
		this.payOutMode = payOutMode;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public PayOutRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


	
	

}