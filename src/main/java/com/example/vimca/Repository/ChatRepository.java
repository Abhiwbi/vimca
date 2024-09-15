package com.example.vimca.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vimca.Model.Chat;

public interface ChatRepository  extends JpaRepository<Chat, Long>{

}