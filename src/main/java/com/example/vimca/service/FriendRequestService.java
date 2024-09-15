package com.example.vimca.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.vimca.GlobalException.MyException;
import com.example.vimca.Model.AppUser;
import com.example.vimca.Model.Friend;
import com.example.vimca.Model.FriendRequest;
import com.example.vimca.Model.Product;
import com.example.vimca.Repository.FriendRepository;
import com.example.vimca.Repository.FriendRequestRepository;
import com.example.vimca.enums.FriendRequestStatus;

@Service
public class FriendRequestService {

	@Autowired
	private FriendRequestRepository friendRequestRepository;

	@Autowired
	private AppUserService userService;

	@Autowired
	private FriendRepository friendRepository;

	@Autowired
	private BlockedUserService blockService;

	private static final int MAX_REQUESTS_PER_DAY = 50;

	@Transactional
	public FriendRequest sendFriendRequest(Long senderId, Long receiverId) {

		LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
		int requestsSentToday = friendRequestRepository.countFriendRequestsSentToday(senderId, startOfDay, endOfDay);
		if (requestsSentToday >= MAX_REQUESTS_PER_DAY) {
			throw new MyException("You have reached the daily limit of 50 friend requests.");
		}
		AppUser sender = userService.getAppUserById(senderId, false);
		if (sender == null) {
			throw new MyException("Sender not found");
		}
		AppUser receiver = userService.getAppUserById(receiverId, false);
		if (receiver == null) {
			throw new MyException("Receiver not found");
		}

		FriendRequest existingFriendRequest = findExistingFriendRequest(sender, receiver);
		if (existingFriendRequest != null) {
			return existingFriendRequest;
		}

		if (blockService.isUserBlocked(sender, receiver) || blockService.isUserBlocked(receiver, sender)) {
			throw new MyException("You cannot send a friend request to a user who has blocked you or vice versa.");
		}
		FriendRequest friendRequest = new FriendRequest();
		friendRequest.setSender(sender);
		friendRequest.setReceiver(receiver);
		return friendRequestRepository.save(friendRequest);
	}

	private FriendRequest findExistingFriendRequest(AppUser sender, AppUser receiver) {
		FriendRequest existsFriendRequest = friendRequestRepository.existsByUserAndFriend(sender.getAppUserId(),
				receiver.getAppUserId(), false);
		if (existsFriendRequest != null) {
			return existsFriendRequest;
		}

		FriendRequest existsReceivedRequest = friendRequestRepository.existsByFriendAndUser(receiver.getAppUserId(),
				sender.getAppUserId(), false);
		return existsReceivedRequest;
	}

	public void acceptFriendRequest(Long requestId) {
		FriendRequest friendRequest = friendRequestRepository.findById(requestId)
				.orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

		if (friendRequest.getStatus() != FriendRequestStatus.PENDING) {
			throw new IllegalArgumentException("This friend request has already been processed");
		}

		friendRequest.setStatus(FriendRequestStatus.ACCEPTED);
		friendRequest.setUpdatedDate(LocalDateTime.now());
		friendRequestRepository.save(friendRequest);

		Friend friendship = new Friend();
		friendship.setUser(friendRequest.getSender());
		friendship.setFriend(friendRequest.getReceiver());
		friendRepository.save(friendship);
	}

	public FriendRequest getPendingFriendRequestByReciverId(Long reciverId) {
		FriendRequestStatus requestStatus = FriendRequestStatus.PENDING;
		return friendRequestRepository.getPendingFriendRequestByReciverId(reciverId, requestStatus, false);
	}

	public FriendRequest deleteFriendRequestById(Long id) {
	    try {
	        FriendRequest friendRequest = friendRequestRepository.getFriendRequestById(id, false);
	        if (friendRequest == null) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend request not found");
	        }

	        friendRequestRepository.delete(friendRequest);
	        return friendRequest; // Return the deleted FriendRequest

	    } catch (EmptyResultDataAccessException ex) {
	        // This exception occurs if the entity was already deleted
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend request not found", ex);

	    } catch (Exception e) {
	        // Catch any other unexpected exceptions
	        System.err.println("An error occurred while deleting the friend request: " + e.getMessage());
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete friend request", e);
	    }
	}

}
