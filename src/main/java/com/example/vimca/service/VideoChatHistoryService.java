package com.example.vimca.service;


import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vimca.GlobalException.MyException;
import com.example.vimca.Model.VideoChatHistory;
import com.example.vimca.Repository.VideoChatHistoryRepository;

@Service
public class VideoChatHistoryService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VideoChatHistoryRepository videoChatHistoryRepository;

    public void addVideoChatHistory(VideoChatHistory videoChatHistory) {
        videoChatHistoryRepository.save(videoChatHistory);
    }

    public VideoChatHistory getVideoChatHistoryById(Long historyId) {
        return videoChatHistoryRepository.getVideoChatHistoryById(historyId, false);
    }

    public Iterable<VideoChatHistory> findAllVideoChatHistories(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedVideoChatHistoryFilter");
        filter.setParameter("isDeleted", isDeleted);
        Iterable<VideoChatHistory> histories = videoChatHistoryRepository.findAll();
        session.disableFilter("deletedVideoChatHistoryFilter");
        return histories;
    }

    public void deleteVideoChatHistoryById(Long id) {
        try {
            VideoChatHistory history = videoChatHistoryRepository.getVideoChatHistoryById(id, false);
            if (history == null) {
                throw new MyException("Video chat history not present to delete");
            }
            videoChatHistoryRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("An error occurred while deleting the video chat history: " + e.getMessage());
        }
    }
    public List<VideoChatHistory> getChatHistoryListByUserId(Long id){
    	return videoChatHistoryRepository.getChatHistoryListByUserId(id,false);
    }
}