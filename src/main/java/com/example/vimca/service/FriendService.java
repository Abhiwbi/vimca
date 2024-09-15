package com.example.vimca.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.vimca.Model.AppUser;
import com.example.vimca.Model.Friend;
import com.example.vimca.Repository.FriendRepository;
import com.example.vimca.controller.BrokerController;

@Service
public class FriendService {

	@Autowired
	private FriendRepository friendRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(BrokerController.class);

	public Friend getFriendByUserAndFriend(Long userId, Long friendId) {
		return friendRepository.getFriendByUserAndFriend(userId, friendId, false);
	}

	public void removeFriendship(AppUser user, AppUser blockedUser) {
		Friend friend = getFriendByUserAndFriend(user.getAppUserId(), blockedUser.getAppUserId());
		if (friend != null) {
			deleteFriendship(user.getAppUserId(), blockedUser.getAppUserId(), false);

			deleteFriendship(blockedUser.getAppUserId(), user.getAppUserId(), false);
		}
	}

//to check friend remove option
	public void deleteFriendship(Long userId, Long friendId, boolean isDeleted) {
		Friend friend = getFriendByUserAndFriend(userId, friendId);

		friendRepository.delete(friend);
	}

	public void deleteFriendById(Long userId,Long friendId) {
		try {
			Friend friend = getFriendByUserAndFriend(userId, friendId);

			if (friend == null) {
				throw new ResponseStatusException(HttpStatus.NON_AUTHORITATIVE_INFORMATION,
						"Friend not present to delete");
			}
			logger.info("Deleting friend with ID: {}", friendId);

			friendRepository.deleteById(friend.getFriendId());
		} catch (Exception e) {
			logger.error("An error occurred while deleting the friend with ID: {}. Error: {}", friendId, e.getMessage(), e);
			throw new RuntimeException("An error occurred while deleting the friend", e);
		}
	}

	public List<Friend> getAllFriendsByUserId(Long id, boolean b) {
		// TODO Auto-generated method stub
		return friendRepository.getAllFriendsByUserId(id, false);
	}

}