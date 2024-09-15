package com.example.vimca.service;

import javax.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vimca.Model.SubscriptionEntity;
import com.example.vimca.Repository.SubscriptionRepository;

@Service
public class SubscriptionEntityService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private EntityManager entityManager;

	public SubscriptionEntity save(SubscriptionEntity subscriptionEntity) {
		return subscriptionRepository.save(subscriptionEntity);
	}

	public SubscriptionEntity getSubscriptionEntityById(Long id, boolean isDeleted) {
		try {
			SubscriptionEntity subscriptionEntity = subscriptionRepository.getSubscriptionEntityById(id, false);
			return subscriptionEntity;
		} catch (Exception e) {
			return null;
		}
	}

	public Iterable<SubscriptionEntity> findAllSubscriptionEntity(boolean isDeleted) {
		Session session = entityManager.unwrap(Session.class);
		Filter filter = session.enableFilter("deletedSubscriptionEntityFilter");
		filter.setParameter("isDeleted", isDeleted);
		Iterable<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAll();
		session.disableFilter("deletedSubscriptionEntityFilter");
		return subscriptionEntities;
	}

	public SubscriptionEntity deleteSubscriptionEntityById(Long id, boolean b) {
		try {
			SubscriptionEntity subscriptionEntity = subscriptionRepository.getSubscriptionEntityById(id, false);
			subscriptionRepository.deleteById(id);
			return subscriptionEntity;
		} catch (Exception e) {
			return null;
		}
	}

	public SubscriptionEntity updateSubscription(SubscriptionEntity existingSubscription,
			SubscriptionEntity newSubscription) {
		if (newSubscription == null) {
			throw new IllegalArgumentException("New subscription data must not be null");
		}
		if (newSubscription.getPlanName() != null && !newSubscription.getPlanName().isEmpty()) {
			existingSubscription.setPlanName(newSubscription.getPlanName());
		}
		if (newSubscription.getOffer() > 0) {
			existingSubscription.setOffer(newSubscription.getOffer());
		}
		if (newSubscription.getValidity() > 0) {
			existingSubscription.setValidity(newSubscription.getValidity());
		}
		if (newSubscription.getPlanAmount() != null && !newSubscription.getPlanAmount().isEmpty()) {
			existingSubscription.setPlanAmount(newSubscription.getPlanAmount());
		}
		if (newSubscription.getCreatedDate() != null && !newSubscription.getCreatedDate().isEmpty()) {
			existingSubscription.setCreatedDate(newSubscription.getCreatedDate());
		}
		if (newSubscription.getUpdatedDate() != null && !newSubscription.getUpdatedDate().isEmpty()) {
			existingSubscription.setUpdatedDate(newSubscription.getUpdatedDate());
		}
		if (newSubscription.getIsGenderFilter() != null) {
			existingSubscription.setIsGenderFilter(newSubscription.getIsGenderFilter());
		}
		if (newSubscription.getGenderFilterQuantity() > 0) {
			existingSubscription.setGenderFilterQuantity(newSubscription.getGenderFilterQuantity());
		}
		if (newSubscription.getOffOnSupperMatchGloabl() != null
				&& !newSubscription.getOffOnSupperMatchGloabl().isEmpty()) {
			existingSubscription.setOffOnSupperMatchGloabl(newSubscription.getOffOnSupperMatchGloabl());
		}
		if (newSubscription.getOffOnSupperMatchDomestic() != null
				&& !newSubscription.getOffOnSupperMatchDomestic().isEmpty()) {
			existingSubscription.setOffOnSupperMatchDomestic(newSubscription.getOffOnSupperMatchDomestic());
		}
		if (newSubscription.getIsNickNameCanBeChanged() != null) {
			existingSubscription.setIsNickNameCanBeChanged(newSubscription.getIsNickNameCanBeChanged());
		}
		if (newSubscription.getRemoveAds() != null) {
			existingSubscription.setRemoveAds(newSubscription.getRemoveAds());
		}
		if (newSubscription.getGemsAmount() != null) {
			existingSubscription.setGemsAmount(newSubscription.getGemsAmount());
		}

		return existingSubscription;
	}

}
