package com.example.vimca.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.vimca.Model.SubscriptionEntity;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {


	@Query("select d from SubscriptionEntity d where d.subId=:id AND d.deleted=:isDeleted")
	public SubscriptionEntity getSubscriptionEntityById(Long id, boolean isDeleted);

}
