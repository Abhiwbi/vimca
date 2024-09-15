package com.example.vimca.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@SQLDelete(sql = "UPDATE subscription_entity SET deleted = true WHERE sub_id=?")
@FilterDef(name = "deletedSubscriptionEntityFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedSubscriptionEntityFilter", condition = "deleted = :isDeleted")
public class SubscriptionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subId;

	private String planName;
	private double offer;
	private int validity;
	private String planAmount;
	private String createdDate;
	private String updatedDate;
	private Boolean isGenderFilter = Boolean.FALSE;
	private int genderFilterQuantity;
	private String offOnSupperMatchGloabl;
	private String offOnSupperMatchDomestic;
	private Boolean isNickNameCanBeChanged = Boolean.FALSE;
	private Boolean removeAds = Boolean.FALSE;
	private Integer gemsAmount;
	@JsonIgnore
	private boolean deleted = Boolean.FALSE;
	public Long getSubId() {
		return subId;
	}
	public void setSubId(Long subId) {
		this.subId = subId;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public double getOffer() {
		return offer;
	}
	public void setOffer(double offer) {
		this.offer = offer;
	}
	public int getValidity() {
		return validity;
	}
	public void setValidity(int validity) {
		this.validity = validity;
	}
	public String getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(String planAmount) {
		this.planAmount = planAmount;
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
	public Boolean getIsGenderFilter() {
		return isGenderFilter;
	}
	public void setIsGenderFilter(Boolean isGenderFilter) {
		this.isGenderFilter = isGenderFilter;
	}
	public int getGenderFilterQuantity() {
		return genderFilterQuantity;
	}
	public void setGenderFilterQuantity(int genderFilterQuantity) {
		this.genderFilterQuantity = genderFilterQuantity;
	}
	public String getOffOnSupperMatchGloabl() {
		return offOnSupperMatchGloabl;
	}
	public void setOffOnSupperMatchGloabl(String offOnSupperMatchGloabl) {
		this.offOnSupperMatchGloabl = offOnSupperMatchGloabl;
	}
	public String getOffOnSupperMatchDomestic() {
		return offOnSupperMatchDomestic;
	}
	public void setOffOnSupperMatchDomestic(String offOnSupperMatchDomestic) {
		this.offOnSupperMatchDomestic = offOnSupperMatchDomestic;
	}
	public Boolean getIsNickNameCanBeChanged() {
		return isNickNameCanBeChanged;
	}
	public void setIsNickNameCanBeChanged(Boolean isNickNameCanBeChanged) {
		this.isNickNameCanBeChanged = isNickNameCanBeChanged;
	}
	public Boolean getRemoveAds() {
		return removeAds;
	}
	public void setRemoveAds(Boolean removeAds) {
		this.removeAds = removeAds;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	public Integer getGemsAmount() {
		return gemsAmount;
	}
	public void setGemsAmount(Integer gemsAmount) {
		this.gemsAmount = gemsAmount;
	}
	public SubscriptionEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
