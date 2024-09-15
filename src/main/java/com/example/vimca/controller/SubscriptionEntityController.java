package com.example.vimca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vimca.Model.SubscriptionEntity;
import com.example.vimca.service.SubscriptionEntityService;

@RestController
@CrossOrigin
@RequestMapping("/app/v1/subscription")

public class SubscriptionEntityController {
	
	@Autowired
	private SubscriptionEntityService subscriptionEntityService;
	
	@PostMapping("/create")
	public ResponseEntity<?> addNewSubscription(@RequestBody SubscriptionEntity subscriptionEntity){
		try {
			SubscriptionEntity entity = subscriptionEntityService.save(subscriptionEntity);
			if (entity == null) {
				return new ResponseEntity<>("Subscription  cannot be added", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			}
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(" Internal Server Error" + e.getMessage(),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
	}

	@PutMapping("/updatBy/{id}")
	public ResponseEntity<?> updateSubscriptionById(@PathVariable("id") Long id ,@RequestBody SubscriptionEntity newSubscription){
		try {
			SubscriptionEntity existingSubscription = subscriptionEntityService.getSubscriptionEntityById(id, false);
			if (existingSubscription == null) {
				return new ResponseEntity<>("Subscription  not found with this id", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			}
			SubscriptionEntity updated=subscriptionEntityService.updateSubscription(existingSubscription,newSubscription);
			return new ResponseEntity<>(updated, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(" Internal Server Error" + e.getMessage(),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
	}
	
	@GetMapping("/ById/{id}")
	public ResponseEntity<?> getSubscriptionEntityById(@PathVariable("id") Long id) {
	    try {
	        SubscriptionEntity subscriptionEntity = subscriptionEntityService.getSubscriptionEntityById(id, false);
	        if (subscriptionEntity == null) {
	            return new ResponseEntity<>("SubscriptionEntity not present with this id ", HttpStatus.NOT_FOUND);
	        } else {
	            return new ResponseEntity<>(subscriptionEntity, HttpStatus.OK);
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAllSubscriptionEntities() {
	    List<SubscriptionEntity> subscriptionEntities = (List<SubscriptionEntity>) subscriptionEntityService.findAllSubscriptionEntity(false);
	    if (subscriptionEntities.isEmpty()) {
	        return new ResponseEntity<>("There are no SubscriptionEntities present!", HttpStatus.NOT_FOUND);
	    } else {
	        return new ResponseEntity<>(subscriptionEntities, HttpStatus.OK);
	    }
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteSubscriptionEntityById(@PathVariable Long id) {
	    try {
	        SubscriptionEntity deletedSubscriptionEntity = subscriptionEntityService.getSubscriptionEntityById(id, false);

	        if (deletedSubscriptionEntity == null) {
	            return new ResponseEntity<>("No SubscriptionEntity found with id: " + id, HttpStatus.NOT_FOUND);
	        }

	        subscriptionEntityService.deleteSubscriptionEntityById(id, false);
	        return new ResponseEntity<>("SubscriptionEntity with id " + id + " deleted successfully", HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>("An error occurred while processing the request: " + e.getMessage(),
	                HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
