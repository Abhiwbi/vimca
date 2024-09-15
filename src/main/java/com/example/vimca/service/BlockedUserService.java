package com.example.vimca.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.vimca.GlobalException.MyException;
import com.example.vimca.Model.AppUser;
import com.example.vimca.Model.BlockedUser;
import com.example.vimca.Repository.BlockedUserRepository;

@Service
public class BlockedUserService {

	@Autowired
	private BlockedUserRepository blockedUserRepository;

	@Autowired
	private AppUserService appUserService;
	
	@Autowired
	private FriendService friendService;

	public List<BlockedUser> getAllBlockedUserByBlockerId(Long id, boolean isDeleted) {
		return blockedUserRepository.getAllBlockedUserByBlockerId(id, false);
	}

	 @Transactional
	    public void blockUser(Long userId, Long blockedUserId) {
	        AppUser user = appUserService.getAppUserById(userId, false);
	        if (user == null) {
	            throw new ResponseStatusException(HttpStatus.NON_AUTHORITATIVE_INFORMATION,"User not found");
	        }

	        AppUser blockedUser = appUserService.getAppUserById(blockedUserId, false);
	        if (blockedUser == null) {
	            throw new ResponseStatusException(HttpStatus.NON_AUTHORITATIVE_INFORMATION,"BlockedUser not found");
	        }

	        // Check if either user is already blocked
	        if (isUserBlocked(user, blockedUser)) {
	            throw new ResponseStatusException(HttpStatus.NON_AUTHORITATIVE_INFORMATION,"User is already blocked");

	        }
//	        if (isUserBlocked(blockedUser, user)) {
//	            throw new MyException("Cannot block a user who has already blocked you");
//	        }

	        // Remove from friends if they are friends
	        friendService.removeFriendship(user, blockedUser);

	        // Add to block list
	        addToBlockList(user, blockedUser);
	    }

	    public boolean isUserBlocked(AppUser user, AppUser blockedUser) {
	        BlockedUser existingBlock = existByUserAndBlockedUser(user, blockedUser);
	        return existingBlock != null;
	    }

	    

	    private void addToBlockList(AppUser user, AppUser blockedUser) {
	        BlockedUser blockList = new BlockedUser();
	        blockList.setBlocker(user);
	        blockList.setBlocked(blockedUser);
	        blockedUserRepository.save(blockList);
	    }

	    private BlockedUser existByUserAndBlockedUser(AppUser user, AppUser blockedUser) {
	        return blockedUserRepository.findByUserAndBlockedUser(user.getAppUserId(), blockedUser.getAppUserId(),false);
	    }
	
}