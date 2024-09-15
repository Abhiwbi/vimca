package com.example.vimca.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.vimca.GlobalException.MyException;
import com.example.vimca.Model.Friend;
import com.example.vimca.dto.FriendListDto;
import com.example.vimca.service.AppUserService;
import com.example.vimca.service.BlockedUserService;
import com.example.vimca.service.FriendService;

@RestController
@CrossOrigin
@RequestMapping("/app/v1/friend")
public class FriendController {

	@Autowired
	private FriendService friendService;

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private BlockedUserService blockedUserService;

	private static final Logger logger = LoggerFactory.getLogger(BrokerController.class);

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<FriendListDto>> getAllFriendsByUserId(@PathVariable Long userId) {
		try {
			List<Friend> friends = friendService.getAllFriendsByUserId(userId, false);
			List<FriendListDto> friendDtos = new ArrayList<>();
			for (Friend f : friends) {
				FriendListDto fdto = mapFriendListToDto(f, userId);
				friendDtos.add(fdto);
			}
			if (friendDtos.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.ok(friendDtos);
			}
		} catch (Exception e) {
			logger.error("Error fetching friends for userId {}: {}", userId, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/remove/{userId}/{friendId}")
	public ResponseEntity<?> deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
		logger.info("Request to delete friend with ID: {}", friendId);
		try {
			friendService.deleteFriendById(userId, friendId);
			return new ResponseEntity<>("Friend deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while deleting friend with ID: {}. Error: {}", friendId, e.getMessage(), e);
			return new ResponseEntity<>("An error occurred while deleting the friend",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/block")
	public ResponseEntity<?> blockUser(@RequestParam Long userId, @RequestParam Long blockedUserId) {
		try {
			logger.info("Request to block user {} by user {}", blockedUserId, userId);

			blockedUserService.blockUser(userId, blockedUserId);

			logger.info("User {} successfully blocked user {}", userId, blockedUserId);
			return ResponseEntity.ok("User blocked successfully");

		} catch (MyException e) {
			logger.error("Failed to block user {} by user {}: {}", blockedUserId, userId, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(e.getMessage());

		} catch (Exception e) {
			logger.error("An unexpected error occurred while blocking user {} by user {}: {}", blockedUserId, userId,
					e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
		}
	}

	private FriendListDto mapFriendListToDto(Friend friend ,Long id) {
	    FriendListDto dto = new FriendListDto();
	    dto.setFriendId(friend.getFriendId());
         if(friend.getUser()!=null) {
        	 dto.setUserId(friend.getUser().getAppUserId());
         }
         if(friend.getUser().getAppUserId().equals(id)) {
        	 dto.setFriendUserId(friend.getFriend().getAppUserId());
        	    dto.setName(friend.getFriend().getName());
        	    dto.setEmail(friend.getFriend().getEmail());
        	    dto.setMobile(friend.getFriend().getMobile());
        	    dto.setCity(friend.getFriend().getCity());
        	    dto.setProfileImageUrl(friend.getFriend().getProfileImageUrl());
        	   
         }
         else if(friend.getFriend().getAppUserId().equals(id)) {
        	 dto.setFriendUserId(friend.getUser().getAppUserId());
        	    dto.setName(friend.getUser().getName());
        	    dto.setEmail(friend.getUser().getEmail());
        	    dto.setMobile(friend.getUser().getMobile());
        	    dto.setCity(friend.getUser().getCity());
        	    dto.setProfileImageUrl(friend.getUser().getProfileImageUrl());
        	   
         }
	    return dto;
	}

}
