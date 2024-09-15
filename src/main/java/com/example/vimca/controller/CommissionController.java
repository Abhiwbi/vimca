package com.example.vimca.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vimca.Broker.Broker;
import com.example.vimca.Broker.Commission;
import com.example.vimca.dto.CommissionDto;
import com.example.vimca.service.BrokerService;
import com.example.vimca.service.CommissionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin
@RequestMapping("/app/v1/Commission")
public class CommissionController {

	@Autowired
	private BrokerService brokerService;

	@Autowired
	private CommissionService commissionService;

	private static final Logger logger = LoggerFactory.getLogger(BrokerController.class);

	@GetMapping("/broker/{id}")
	public ResponseEntity<?> getAllCommissionByBrokerId(@PathVariable Long id) {

		Broker existingBroker = brokerService.getBrokerById(id, false);

		if (existingBroker == null) {
			logger.error("Broker not found with ID: {}", id);
			return new ResponseEntity<>("Broker not found with ID: " + id, HttpStatus.NOT_FOUND);
		}
		List<Commission> commissionList = commissionService.getAllCommissionByBrokerId(id, false);

		if (commissionList.isEmpty()) {
			logger.info("No commissions found for Broker ID: {}", id);
			return new ResponseEntity<>("No commissions found for this broker.", HttpStatus.NO_CONTENT);
		}

		List<CommissionDto> commissionDtoList = commissionList.stream().map(this::mapAllCommission)
				.collect(Collectors.toList());

		logger.info("Returning {} commissions for Broker ID: {}", commissionDtoList.size(), id);
		return new ResponseEntity<>(commissionDtoList, HttpStatus.OK);
	}

	 @GetMapping("/all")
	    public ResponseEntity<?> getAllCommissions(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "50") int size) {
	        
	        logger.info("Request to fetch all commissions with page: {} and size: {}", page, size);
	        try {
	            Page<Commission> commissionsPage = commissionService.findAllCommissions(page, size, false);

	            if (commissionsPage.isEmpty()) {
	                logger.info("No commissions found.");
	                return new ResponseEntity<>("No commissions found.", HttpStatus.NO_CONTENT);
	            }

	            // Convert the list of Commission to a list of CommissionDto
	            List<CommissionDto> commissionDtoList = commissionsPage.stream()
	                .map(this::mapAllCommission)
	                .collect(Collectors.toList());

	            logger.info("Returning {} commissions", commissionDtoList.size());
	            return new ResponseEntity<>(commissionDtoList, HttpStatus.OK);
	        } catch (Exception e) {
	            logger.error("Error occurred while fetching all commissions. Error: {}", e.getMessage(), e);
	            return new ResponseEntity<>("An error occurred while fetching the commissions", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	private CommissionDto mapAllCommission(Commission commission) {
		CommissionDto dto = new CommissionDto();
		dto.setCommissionId(commission.getCommissionId());
		dto.setBrokerName(commission.getPassBroker().getName());
		dto.setBrokerId(commission.getPassBroker().getBrokerId().toString());
		dto.setCommissionAdded(String.valueOf(commission.getNewCommission()));
		dto.setUserId(commission.getAppUser().getAppUserId().toString());
		dto.setUserName(commission.getAppUser().getName());
		dto.setUserCountry(commission.getAppUser().getCountry());
		dto.setPurchaseType(commission.getPurchaseType());
		return dto;
	}
}
