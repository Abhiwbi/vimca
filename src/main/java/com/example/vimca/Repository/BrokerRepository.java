package com.example.vimca.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.vimca.Broker.Broker;

public interface BrokerRepository extends JpaRepository<Broker, Long> {

	@Query("select d from Broker d where d.brokerId=:id AND d.deleted=:isDeleted")
	public Broker getBrokerById(Long id, boolean isDeleted);

	@Query("select d from Broker d where d.mobile=:mobile AND d.deleted=:isDeleted")
	public Broker getBrokerByMobileNumber(String mobile, boolean isDeleted);

	@Query("select d from Broker d where d.email=:email AND d.deleted=:isDeleted")
	public Broker getBrokerByEmailId(String email, boolean isDeleted);

	@Query("select d from Broker d where d.referralCode=:referralCode AND d.deleted=:isDeleted")
	public Broker getBrokerByReferralCode(String referralCode, boolean isDeleted);

}