package com.example.vimca.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.vimca.GlobalException.MyException;
import com.example.vimca.Model.FriendRequest;
import com.example.vimca.service.FriendRequestService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/friend-requests")
public class FriendRequestController {

	@Autowired
	private FriendRequestService friendRequestService;
	private static final Logger logger = LoggerFactory.getLogger(FriendRequestController.class);

	@PostMapping("/send")
	public ResponseEntity<FriendRequest> sendFriendRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
	    try {
	        logger.info("Attempting to send friend request from user {} to user {}", senderId, receiverId);
	        FriendRequest friendRequest = friendRequestService.sendFriendRequest(senderId, receiverId);
	        logger.info("Friend request sent successfully from user {} to user {}", senderId, receiverId);
	        return ResponseEntity.ok(friendRequest);
	    } catch (MyException e) {
	        logger.error("Failed to send friend request from user {} to user {}: {}", senderId, receiverId, e.getMessage(), e);
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
	    } catch (Exception e) {
	        logger.error("An unexpected error occurred while sending friend request from user {} to user {}: {}", senderId, receiverId, e.getMessage(), e);
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
	    }
	}


	@PutMapping("/accept/{requestId}")
	public ResponseEntity<?> acceptFriendRequest(@PathVariable Long requestId) {
		try {
			logger.info("Accepting friend request with ID: {}", requestId);
			friendRequestService.acceptFriendRequest(requestId);
			logger.info("Friend request with ID {} accepted successfully", requestId);
			
			return ResponseEntity.ok("Friend request accepted successfully");
		} catch (IllegalArgumentException e) {
			logger.warn("Failed to accept friend request with ID {}: {}", requestId, e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			logger.error("Unexpected error while accepting friend request with ID {}: {}", requestId, e.getMessage(),
					e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
		}
	}
	
	

	@GetMapping("/pending/{id}")
	public ResponseEntity<?> getPendingFriendRequestByReceiverId(@PathVariable("id") Long receiverId) {
		try {
			logger.info("Retrieving pending friend request for receiver ID: {}", receiverId);
			FriendRequest pendingRequest = friendRequestService.getPendingFriendRequestByReciverId(receiverId);
			if (pendingRequest != null) {
				logger.info("Pending friend request found for receiver ID: {}", receiverId);
				return ResponseEntity.ok(pendingRequest);
			} else {
				logger.info("No pending friend request found for receiver ID: {}", receiverId);
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No pending friend request found");
			}
		} catch (Exception e) {
			logger.error("Unexpected error while retrieving pending friend request for receiver ID {}: {}", receiverId,
					e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
		}
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteRequestById(@PathVariable Long id) {
	    logger.info("Request to delete Friend Request with ID: {}", id);
	    try {
	        friendRequestService.deleteFriendRequestById(id);
	        return ResponseEntity.ok("Friend Request deleted successfully");
	    } catch (ResponseStatusException ex) {
	        logger.warn("Failed to delete Friend Request with ID: {}. Reason: {}", id, ex.getReason(), ex);
	        return ResponseEntity.status(ex.getStatus()).body(ex.getReason());
	    } catch (Exception e) {
	        logger.error("Error occurred while deleting Friend Request with ID: {}. Error: {}", id, e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the Friend Request");
	    }
	}

}
