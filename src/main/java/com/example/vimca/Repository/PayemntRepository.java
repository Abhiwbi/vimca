package com.example.vimca.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.vimca.Model.Payment;

public interface PayemntRepository extends JpaRepository<Payment, Long> {

	@Query("select d from Payment d where d.paymentId=:id AND d.deleted=:isDeleted")
	Payment getPaymentById(Long id, boolean isDeleted);

	@Query("select d from Payment d where d.appUser.appUserId=:userId AND d.deleted=:isDeleted")
	List<Payment> getAllPaymentByUserId(Long userId, boolean isDeleted);

}