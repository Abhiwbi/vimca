package com.example.vimca.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.vimca.Model.VideoChatHistory;

public interface VideoChatHistoryRepository  extends JpaRepository<VideoChatHistory, Long>{

	@Query("Select c from VideoChatHistory c where c.historyId=:historyId AND c.deleted=:isDeleted")
	VideoChatHistory getVideoChatHistoryById(Long historyId, boolean isDeleted);

	@Query("SELECT c FROM VideoChatHistory c WHERE (c.userOne.appUserId = :id OR c.userTwo.appUserId = :id) AND c.deleted = :isDeleted")
	List<VideoChatHistory> getChatHistoryListByUserId(@Param("id") Long id, @Param("isDeleted") boolean isDeleted);

}