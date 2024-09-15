package com.example.vimca.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.vimca.Model.BlockedUser;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long>{

	@Query("select d from BlockedUser d where d.blocker.appUserId=:id AND d.deleted=:isDeleted")
	List<BlockedUser> getAllBlockedUserByBlockerId(Long id, boolean isDeleted);

	@Query("select d from BlockedUser d where  d.blocker.appUserId=:blockerId AND d.blocked.appUserId=:blockedId  AND d.deleted=:isDeleted")
	BlockedUser findByUserAndBlockedUser(Long blockerId, Long blockedId, boolean isDeleted);

}