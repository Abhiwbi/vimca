package com.example.vimca.Repository;


import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.vimca.Model.FriendRequest;
import com.example.vimca.enums.FriendRequestStatus;


public interface FriendRequestRepository  extends JpaRepository<FriendRequest, Long> {

	@Query("select d from FriendRequest d where d.sender.appUserId=:senderId AND d.receiver.appUserId=:reciverId AND d.deleted=:isDeleted")
	FriendRequest existsByUserAndFriend(Long senderId, Long reciverId, boolean isDeleted);

	@Query("select d from FriendRequest d where  d.receiver.appUserId=:reciverId AND d.sender.appUserId=:senderId  AND d.deleted=:isDeleted")
	FriendRequest existsByFriendAndUser(Long reciverId, Long senderId, boolean isDeleted);

	@Query("select d from FriendRequest d where  d.receiver.appUserId=:reciverId AND d.status=:requestStatus AND d.deleted=:isDeleted")
	FriendRequest getPendingFriendRequestByReciverId(Long reciverId, FriendRequestStatus requestStatus, boolean isDeleted);

	@Query("SELECT COUNT(fr) FROM FriendRequest fr WHERE fr.sender.appUserId = :senderId AND fr.createdDate >= :startOfDay AND fr.createdDate <= :endOfDay")
	int countFriendRequestsSentToday(@Param("senderId") Long senderId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

	@Query("select d from FriendRequest d where  d.requestId=:id AND d.deleted=:isDeleted")
	FriendRequest getFriendRequestById(Long id, boolean isDeleted);

}