package com.example.vimca.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.vimca.Broker.Commission;
import com.example.vimca.GlobalException.MyException;
import com.example.vimca.Repository.CommissionRepository;

@Service
public class CommissionService {

	@Autowired
	private CommissionRepository commissionRepository;

	@Autowired
	private EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(CommissionService.class);

	@Transactional
	public Commission createCommission(Commission commission) {
		logger.info("Creating new commission with id: {}", commission.getCommissionId());
		return commissionRepository.save(commission);
	}

	public Commission getCommissionById(Long commissionId) {
		logger.info("Fetching commission with ID: {}", commissionId);
		return commissionRepository.getCommissionById(commissionId, false);
	}

	public Page<Commission> findAllCommissions(int page, int size, boolean isDeleted) {
		Session session = entityManager.unwrap(Session.class);
		Filter filter = session.enableFilter("deletedCommissionFilter");
		filter.setParameter("isDeleted", isDeleted);
		Pageable pageable = PageRequest.of(page, size);
		Page<Commission> commissions = commissionRepository.findAll(pageable);
		session.disableFilter("deletedCommissionFilter");
		return commissions;
	}

	public void deleteCommissionById(Long id) {
		try {
			Commission commission = commissionRepository.getCommissionById(id, false);
			if (commission == null) {
				throw new MyException("Commission not present to delete");
			}
			logger.info("Deleting commission with ID: {}", id);
			commissionRepository.deleteById(id);
		} catch (Exception e) {
			logger.error("An error occurred while deleting the commission: {}", e.getMessage(), e);
		}
	}

	public List<Commission> getAllCommissionByBrokerId(Long id, boolean b) {
		// TODO Auto-generated method stub
		return commissionRepository.getAllCommissionByBrokerId(id, false);
	}

}
