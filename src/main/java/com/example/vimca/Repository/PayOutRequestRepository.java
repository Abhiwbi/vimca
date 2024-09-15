package com.example.vimca.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.vimca.Broker.PayOutRequest;

public interface PayOutRequestRepository extends JpaRepository<PayOutRequest, Long> {

}