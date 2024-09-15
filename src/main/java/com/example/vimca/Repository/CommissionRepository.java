package com.example.vimca.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.vimca.Broker.Commission;

public interface CommissionRepository  extends JpaRepository<Commission, Long>{

	@Query("select c from Commission c where c.commissionId=:id AND c.deleted=:isDeleted")
	public Commission getCommissionById(Long id, boolean isDeleted);

	
	@Query("select c from Commission c where c.passBroker.brokerId=:id AND c.deleted=:isDeleted")
	public List<Commission> getAllCommissionByBrokerId(Long id, boolean isDeleted);

}
