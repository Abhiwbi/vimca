package com.example.vimca.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vimca.Model.AppUser;
import com.example.vimca.Model.Payment;
import com.example.vimca.Model.Product;
import com.example.vimca.Model.SubscriptionEntity;
import com.example.vimca.service.AppUserService;
import com.example.vimca.service.BrokerService;
import com.example.vimca.service.PaymentService;
import com.example.vimca.service.ProductService;
import com.example.vimca.service.SubscriptionEntityService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin
@RequestMapping("/app/v1/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private ProductService productService;

	@Autowired
	private BrokerService brokerService;

	@Autowired
	private SubscriptionEntityService subscriptionService;

	private static final Logger logger = LoggerFactory.getLogger(BrokerController.class);

	@PostMapping("/purchase/product")
	public ResponseEntity<?> purchaseProduct(@RequestBody Payment payment) {

		try {
			System.out.println("issue");
			logger.info("Received request to add product with payment details: {}", payment);

			if (payment.getAppUser() != null) {
				logger.info("Fetching AppUser with ID: {}", payment.getAppUser().getAppUserId());
				AppUser appUser = appUserService.getAppUserById(payment.getAppUser().getAppUserId(), false);
				if (appUser == null) {
					logger.warn("AppUser not found with ID: {}", payment.getAppUser().getAppUserId());
					return new ResponseEntity<>("AppUser not present with this ID",
							HttpStatus.NON_AUTHORITATIVE_INFORMATION);
				}
				payment.setAppUser(appUser);
				logger.info("AppUser found and set in payment: {}", appUser);
			}

			if (payment.getPassProduct() != null) {
				logger.info("Fetching Product with ID: {}", payment.getPassProduct().getProductId());
				Product product = productService.getProductById(payment.getPassProduct().getProductId());
				if (product == null) {
					logger.warn("Product not found with ID: {}", payment.getPassProduct().getProductId());
					return new ResponseEntity<>("Product not present with this ID",
							HttpStatus.NON_AUTHORITATIVE_INFORMATION);
				}
				payment.setPassProduct(product);
				payment.setPurchasedGem(product.getBaseGemAmount() + product.getBonusGemAmount());
				logger.info("Product found and set in payment: {}. Total Gems: {}", product, payment.getPurchasedGem());
			}

			logger.info("Saving payment: {}", payment);
			Payment savedPayment = paymentService.save(payment);
			logger.info("Payment saved successfully: {}", savedPayment);
			

			if (savedPayment != null && Boolean.TRUE.equals(savedPayment.getPaymentStatus())) {
				AppUser appUser = savedPayment.getAppUser();
				if (appUser != null) {
					logger.info("Updating AppUser's total gems. Current Gems: {}, Purchased Gems: {}",
							appUser.getTotalGems(), savedPayment.getPurchasedGem());
					appUser.setTotalGems(appUser.getTotalGems() + savedPayment.getPurchasedGem());

					AppUser updatedUser = appUserService.update(appUser);
					if (updatedUser.getBroker() != null) {
						brokerService.addCommission(savedPayment, appUser);
					} // Save the updated AppUser
					logger.info("AppUser updated successfully: {}", appUser);
				}
			}

			return new ResponseEntity<>(savedPayment, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while processing the payment: {}", e.getMessage(), e);
			return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/by/{id}")
	public ResponseEntity<?> getPaymentById(@PathVariable("id") Long id) {

		try {
			logger.info("Fetching payment with ID: {}", id);
			Payment payment = paymentService.getPaymentById(id, false);
			if (payment == null) {
				logger.warn("Payment not found with ID: {}", id);
				return new ResponseEntity<>("Payment not present with this ID",
						HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			} else {
				logger.info("Payment found with ID: {}", id);
				return new ResponseEntity<>(payment, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Error fetching payment with ID: {}. Error: {}", id, e.getMessage(), e);
			return new ResponseEntity<>("Internal server error: " + e.getMessage(),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllPayments() {
		try {
			// Fetch all payments (Assuming paymentService.getAllPayments() returns a list)
			List<Payment> payments = (List<Payment>) paymentService.findAllPayments(false);

			if (payments == null || payments.isEmpty()) {
				logger.warn("No payments found");
				return new ResponseEntity<>("No payments available", HttpStatus.NO_CONTENT);
			} else {
				logger.info("Payments retrieved successfully");
				return new ResponseEntity<>(payments, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Error fetching payments. Error: {}", e.getMessage(), e);
			return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/purchase-subscription")
	public ResponseEntity<?> purchaseSubscription(@RequestBody Payment payment) {

		try {
			if (payment.getAppUser() != null) {
				logger.info("Fetching AppUser with ID: {}", payment.getAppUser().getAppUserId());
				AppUser appUser = appUserService.getAppUserById(payment.getAppUser().getAppUserId(), false);
				if (appUser == null) {
					logger.warn("AppUser not found with ID: {}", payment.getAppUser().getAppUserId());
					return new ResponseEntity<>("AppUser not present with this ID",
							HttpStatus.NON_AUTHORITATIVE_INFORMATION);
				}
				payment.setAppUser(appUser);
			}

			if (payment.getPassSubscription() != null) {
				logger.info("Fetching SubscriptionEntity with ID: {}", payment.getPassSubscription().getSubId());
				SubscriptionEntity subscriptionEntity = subscriptionService
						.getSubscriptionEntityById(payment.getPassSubscription().getSubId(), false);
				if (subscriptionEntity == null) {
					logger.warn("SubscriptionEntity not found with ID: {}", payment.getPassSubscription().getSubId());
					return new ResponseEntity<>("Subscription not present with this ID", HttpStatus.NOT_FOUND);
				}
				payment.setPassSubscription(subscriptionEntity);
				payment.setPurchasedGem(subscriptionEntity.getGemsAmount());
			}

			logger.info("Saving Payment for AppUser ID: {}", payment.getAppUser().getAppUserId());
			Payment savedPayment = paymentService.save(payment);

			if (savedPayment != null && Boolean.TRUE.equals(savedPayment.getPaymentStatus())) {
				AppUser appUser = savedPayment.getAppUser();
				if (appUser != null) {
					logger.info("Updating AppUser properties for ID: {}", appUser.getAppUserId());
					appUser.setTotalGems(appUser.getTotalGems() + savedPayment.getPurchasedGem());
					appUser.setSubscriptionStatus(true);
					appUser.setSubscriptionId(savedPayment.getPassSubscription().getSubId());

					// Handle date and time formatting
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDateTime paymentDate = savedPayment.getCreatedDate();
					LocalDateTime subscriptionEndDate = paymentDate
							.plusDays(savedPayment.getPassSubscription().getValidity());
					String formattedEndDate = subscriptionEndDate.format(formatter);

					appUser.setSubscriptionUntil(formattedEndDate);

					logger.info("Subscription until date set to: {}", formattedEndDate);
					AppUser updatedUser = appUserService.update(appUser);

					// Adding commission if broker exists
					if (updatedUser.getBroker() != null) {
						logger.info("Adding commission for Broker ID: {}", updatedUser.getBroker().getBrokerId());
						brokerService.addCommission(savedPayment, appUser);
					}
				}
			}

			logger.info("Payment processed successfully for AppUser ID: {}", payment.getAppUser().getAppUserId());
			return new ResponseEntity<>(savedPayment, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error processing payment: {}", e.getMessage());
			return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all/{userId}")
	public ResponseEntity<?> getAllPaymentByUserId(@PathVariable("userId") Long userId) {
		try {
			AppUser appUser = appUserService.getAppUserById(userId, false);
			if (appUser == null) {
				logger.warn("AppUser not found with ID: {}", userId);
				return new ResponseEntity<>("AppUser not present with this ID", HttpStatus.NOT_FOUND);
			}
			List<Payment> allPayments = paymentService.getAllPaymentByUserId(userId, false);
			if (allPayments == null || allPayments.isEmpty()) {
				logger.info("No payments found for user ID: {}", userId);
				return new ResponseEntity<>("No payments available for the user", HttpStatus.NO_CONTENT);
			}
			logger.info("Payments found for user ID: {}", userId);
			return new ResponseEntity<>(allPayments, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching payments for user ID: {}. Error: {}", userId, e.getMessage(), e);
			return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
