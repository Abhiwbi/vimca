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
@SQLDelete(sql = "UPDATE payment SET deleted = true WHERE payment_Id=?")
@FilterDef(name = "deletedPaymentFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedPaymentFilter", condition = "deleted = :isDeleted")
public class Payment  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private Double payableAmount;
    private Integer purchasedGem;
    private String paymentMode;
    private String transactionId;
	private LocalDateTime createdDate = LocalDateTime.now();
	private Double discountPercentage;
	private Double discountPrice;
    private Boolean paymentStatus=Boolean.FALSE;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "appUserId", scope = AppUser.class)
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "app_user_id")
	private AppUser appUser;
    
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "productId",scope = Product.class)
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private Product passProduct;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "subId",scope = SubscriptionEntity.class)
	@JsonIdentityReference(alwaysAsId = true)
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "subscription_id")
	private SubscriptionEntity passSubscription;

    @JsonIgnore
    private boolean deleted = Boolean.FALSE;


	public Long getPaymentId() {
		return paymentId;
	}


	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}


	public Double getPayableAmount() {
		return payableAmount;
	}


	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}


	


	public Integer getPurchasedGem() {
		return purchasedGem;
	}


	public void setPurchasedGem(Integer purchasedGem) {
		this.purchasedGem = purchasedGem;
	}


	public SubscriptionEntity getPassSubscription() {
		return passSubscription;
	}


	public void setPassSubscription(SubscriptionEntity passSubscription) {
		this.passSubscription = passSubscription;
	}


	public String getPaymentMode() {
		return paymentMode;
	}


	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}


	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	


	public LocalDateTime getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}


	public Double getDiscountPercentage() {
		return discountPercentage;
	}


	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}


	public Double getDiscountPrice() {
		return discountPrice;
	}


	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}


	

	public Boolean getPaymentStatus() {
		return paymentStatus;
	}


	public void setPaymentStatus(Boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}


	public AppUser getAppUser() {
		return appUser;
	}


	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}


	public Product getPassProduct() {
		return passProduct;
	}


	public void setPassProduct(Product passProduct) {
		this.passProduct = passProduct;
	}


	public boolean isDeleted() {
		return deleted;
	}


	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}


}