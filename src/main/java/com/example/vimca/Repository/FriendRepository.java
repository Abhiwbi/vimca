package com.example.vimca.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.vimca.Model.Friend;

public interface FriendRepository  extends JpaRepository<Friend, Long>{

	@Query("select d from Friend d where  d.user.appUserId=:userId AND d.friend.appUserId=:friendId  AND d.deleted=:isDeleted")
	Friend getFriendByUserAndFriend(Long userId, Long friendId, boolean isDeleted);

	@Query("SELECT d FROM Friend d WHERE (d.user.appUserId = :userId OR d.friend.appUserId = :userId) AND d.deleted = :isDeleted")
	List<Friend> getAllFriendsByUserId(@Param("userId") Long userId, @Param("isDeleted") boolean isDeleted);




}