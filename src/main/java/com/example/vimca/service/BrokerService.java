package com.example.vimca.service;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vimca.Broker.Broker;
import com.example.vimca.Broker.Commission;
import com.example.vimca.GlobalException.MyException;
import com.example.vimca.Model.AppUser;
import com.example.vimca.Model.Payment;
import com.example.vimca.Repository.BrokerRepository;

@Service
public class BrokerService {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private BrokerRepository brokerRepository;

	@Autowired
	private CommissionService commissionService;

	private static final int RANDOM_CODE_LENGTH = 6; // Length of the random part of the referral code
	private static final String PREFIX = "REF"; // Custom prefix
	private static final Logger logger = LoggerFactory.getLogger(BrokerService.class);

	public static String generateReferralCode(Long userId) {
		String randomCode = RandomStringUtils.randomAlphanumeric(RANDOM_CODE_LENGTH).toUpperCase();
		return PREFIX + userId + randomCode; // Concatenate the prefix, user ID, and random code
	}

	public Broker addBroker(Broker broker) {
		Broker savedBroker = brokerRepository.save(broker);
		return savedBroker;
	}

	public Broker getBrokerById(Long id, boolean isDeleted) {
		try {
			Broker brokerEntity = brokerRepository.getBrokerById(id, isDeleted);
			return brokerEntity;
		} catch (Exception e) {
			return null;
		}
	}

	public Broker getBrokerByMobileNumber(String phoneNumber, boolean isDeleted) {
		try {
			Broker brokerEntity = brokerRepository.getBrokerByMobileNumber(phoneNumber, isDeleted);
			return brokerEntity;
		} catch (Exception e) {
			return null;
		}
	}

	public Iterable<Broker> findAllBrokers(boolean isDeleted) {
		Session session = entityManager.unwrap(Session.class);
		Filter filter = session.enableFilter("deletedBrokerFilter");
		filter.setParameter("isDeleted", isDeleted);
		Iterable<Broker> brokerEntities = brokerRepository.findAll();
		session.disableFilter("deletedBrokerFilter");
		return brokerEntities;
	}

	public Broker deleteBrokerById(Long id) {
		try {
			Broker broker = brokerRepository.getBrokerById(id, false);
			brokerRepository.deleteById(id);
			return broker;
		} catch (Exception e) {
			return null;
		}
	}

	public Broker getBrokerByEmailId(String email, boolean isDeleted) {
		return brokerRepository.getBrokerByEmailId(email, isDeleted);
	}

	public Broker addBrokerDetails(Broker existingBroker, Broker broker) {

		try {
			if (broker == null) {
				logger.error("Broker details are null!");
				throw new MyException("Please provide broker details!");
			}

			logger.info("Updating broker details for ID: {}", existingBroker.getBrokerId());

			if (broker.getName() != null && !broker.getName().isEmpty()) {
				existingBroker.setName(broker.getName());
				logger.debug("Updated broker name to: {}", broker.getName());
			}

			if (broker.getGender() != null && !broker.getGender().isEmpty()) {
				existingBroker.setGender(broker.getGender());
				logger.debug("Updated broker gender to: {}", broker.getGender());
			}

			if (broker.getBirthday() != null) {
				existingBroker.setBirthday(broker.getBirthday());
				logger.debug("Updated broker birthday to: {}", broker.getBirthday());
			}

			if (broker.getLanguage() != null && !broker.getLanguage().isEmpty()) {
				existingBroker.setLanguage(broker.getLanguage());
				logger.debug("Updated broker language to: {}", broker.getLanguage());
			}

			if (broker.getCountry() != null && !broker.getCountry().isEmpty()) {
				existingBroker.setCountry(broker.getCountry());
				logger.debug("Updated broker country to: {}", broker.getCountry());
			}
			if (broker.getCity() != null && !broker.getCity().isEmpty()) {
				existingBroker.setCity(broker.getCity());
				logger.debug("Updated broker City to: {}", broker.getCity());
			}

			if (broker.getState() != null && !broker.getState().isEmpty()) {
				existingBroker.setState(broker.getState());
				logger.debug("Updated broker State to: {}", broker.getState());
			}

			if (broker.getTimezone() != null && !broker.getTimezone().isEmpty()) {
				existingBroker.setTimezone(broker.getTimezone());
				logger.debug("Updated broker Time to: {}", broker.getTimezone());
			}
			if (broker.getZipCode() != null && !broker.getZipCode().isEmpty()) {
				existingBroker.setZipCode(broker.getZipCode());
				logger.debug("Updated broker ZipCode to: {}", broker.getZipCode());
			}
			if (broker.getMobile() != null && !broker.getMobile().isEmpty()
					&& (!existingBroker.getMobile().equals(broker.getMobile()))) {
				Broker mobile = getBrokerByMobileNumber(broker.getMobile(), false);
				if (mobile != null) {
					throw new MyException("Mobile number already exist");

				}
				existingBroker.setMobile(broker.getMobile());
				logger.debug("Set  Mobile Number: {}", broker.getMobile());

			}

			if (existingBroker.getReferralCode() == null || existingBroker.getReferralCode().isEmpty()) {
				String referralCode = generateReferralCode(existingBroker.getBrokerId());
				existingBroker.setReferralCode(referralCode);
				logger.debug("Generated and set referral code: {}", referralCode);
			}

			logger.info("Successfully updated broker details for ID: {}", existingBroker.getBrokerId());
			Broker brokerData=updateBroker(existingBroker);
			return brokerData;
		} catch (Exception e) {

			logger.error("Error while updating broker details for ID: {}", existingBroker.getBrokerId(),
					e.getMessage());
			return null;
		}
	}

	public Broker addBankDetails(Broker broker, Broker existingBroker) {
		try {
			if (broker == null) {
				logger.error("Broker details are null!");
				throw new MyException("Please provide broker details!");
			}

			if (broker.getBankAccountNumber() != null && !broker.getBankAccountNumber().isEmpty()) {
				// Optional: Add validation for bank account number format here
				existingBroker.setBankAccountNumber(broker.getBankAccountNumber());
				logger.debug("Updated broker bank account to: {}", broker.getBankAccountNumber());
			}

			if (broker.getBankName() != null && !broker.getBankName().isEmpty()) {
				existingBroker.setBankName(broker.getBankName());
				logger.debug("Updated broker bank name to: {}", broker.getBankName());
			}

			if (broker.getBankBranch() != null && !broker.getBankBranch().isEmpty()) {
				existingBroker.setBankBranch(broker.getBankBranch());
				logger.debug("Updated broker bank branch to: {}", broker.getBankBranch());
			}

			if (broker.getSwiftCode() != null && !broker.getSwiftCode().isEmpty()) {
				// Optional: Add validation for SWIFT code format here
				existingBroker.setSwiftCode(broker.getSwiftCode());
				logger.debug("Updated broker SWIFT code to: {}", broker.getSwiftCode());
			}

			if (broker.getIfscCode() != null && !broker.getIfscCode().isEmpty()) {
				// Optional: Add validation for IFSC code format here
				existingBroker.setIfscCode(broker.getIfscCode());
				logger.debug("Updated broker IFSC code to: {}", broker.getIfscCode());
			}
			if(broker.getPaypalEmailId()!=null) {
				existingBroker.setPaypalEmailId(broker.getPaypalEmailId());
			}

			return existingBroker;
		} catch (Exception e) {
			logger.error("An error occurred while updating broker bank details: {}", e.getMessage(), e);
			throw new MyException("An error occurred while updating broker bank details: " + e.getMessage());
		}
	}

	public Broker updateBroker_Password_Details(Broker broker) {

		return brokerRepository.save(broker);
	}

	public Broker findBrokerByReferralCode(String referralCode, boolean b) {
		// TODO Auto-generated method stub
		return brokerRepository.getBrokerByReferralCode(referralCode, false);
	}

	public Broker updateBroker(Broker broker) {
		return brokerRepository.save(broker);
	}

	@Transactional
	public void addCommission(Payment savedPayment, AppUser appUser) {

		try {
			logger.info("Starting to add commission for AppUser: {}", appUser.getAppUserId());

			Commission newCommission = new Commission();
			Broker broker = appUser.getBroker();

			newCommission.setPassBroker(broker);
			newCommission.setAppUser(appUser);
			newCommission.setCreatedDate(LocalDateTime.now());

			Double paidAmount = savedPayment.getPayableAmount();
			logger.info("Payable amount from payment: {}", paidAmount);

			Double amount = 0.30 * paidAmount;
			logger.info("Calculated commission amount (30% of payable amount): {}", amount);

			newCommission.setNewCommission(amount);

			Double brokerBalance = broker.getBalanceCommission();
			logger.info("Current broker balance: {}", brokerBalance);

			broker.setBalanceCommission(brokerBalance + amount);
			broker.setLifeTimeCommission(broker.getLifeTimeCommission() + amount);

			logger.info("Updated broker balance: {}", broker.getBalanceCommission());
			logger.info("Updated broker lifetime commission: {}", broker.getLifeTimeCommission());

			updateBroker(broker);
			logger.info("Broker updated successfully.");
			if(savedPayment.getPassProduct()!=null) {
				newCommission.setPurchaseType("PRODUCT");
				
			}
			else if(savedPayment.getPassSubscription()!=null) {
				newCommission.setPurchaseType("SUBSCRIPTION");
				
			}
			commissionService.createCommission(newCommission);
			logger.info("New commission created successfully for Broker ID: {} and AppUser ID: {}",
					broker.getBrokerId(), appUser.getAppUserId());
		} catch (Exception e) {
			logger.error("An error occurred while adding commission: {}", e.getMessage(), e);
		}
	}

}