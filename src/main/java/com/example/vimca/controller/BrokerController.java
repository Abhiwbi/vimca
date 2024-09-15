package com.example.vimca.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.vimca.Azure.AzureBlobClient;
import com.example.vimca.Broker.Broker;
import com.example.vimca.dto.BrokerBankDTO;
import com.example.vimca.dto.BrokerDTO;
import com.example.vimca.dto.BrokerLoginDto;
import com.example.vimca.service.BrokerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@CrossOrigin
@RequestMapping("/app/v1/broker")
public class BrokerController {

	@Autowired
	private BrokerService brokerService;

	@Value("${my.global.path}")
	private String path;
	@Autowired
	private AzureBlobClient azureBlob;

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(BrokerController.class);

	@PostMapping("/add")
	public ResponseEntity<?> addBroker(@RequestBody Broker broker) {
		try {
			if ((broker.getEmail() == null && broker.getEmail().isEmpty())) {
				return new ResponseEntity<>("Pls provide email or phone number to login !",
						HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			}
			if (broker.getEmail() != null) {
				Broker email = brokerService.getBrokerByEmailId(broker.getEmail(), false);
				if (email != null) {
					return new ResponseEntity<>("Email already exist", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
				}
			}
//			if (broker.getMobile() != null) {
//				Broker mobile = brokerService.getBrokerByMobileNumber(broker.getMobile(), false);
//				if (mobile != null) {
//					return new ResponseEntity<>("Mobile number already exist",
//							HttpStatus.NON_AUTHORITATIVE_INFORMATION);
//				}
//			}
			if (broker.getPassword() != null) {
				broker.setPassword(new BCryptPasswordEncoder().encode(broker.getPassword()));

			}
			Broker addedBroker = brokerService.addBroker(broker);
			return new ResponseEntity<>(addedBroker, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			System.out.println("issue"+e.getMessage());
			return new ResponseEntity<>("An error occurred while adding the user", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/addPersonalDetails/{id}")
	public ResponseEntity<?> addBrokerDetals(@PathVariable("id") Long id,
	                                        @RequestParam(name = "file", required = false) MultipartFile file,
	                                        @RequestParam("data") String data) {
	    Logger logger = LoggerFactory.getLogger(BrokerController.class);

	    try {
	        logger.info("Starting to process addBroker for ID: {}", id);
	        
	        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
	        Broker broker = mapper.readValue(data, Broker.class);
	        Broker existingBroker = brokerService.getBrokerById(id, false);

	        if (existingBroker == null) {
	            logger.error("Broker not found with ID: {}", id);
	            return new ResponseEntity<>("Broker not found with ID: " + id, HttpStatus.NOT_FOUND);
	        }
	        if (file != null) {
	            try {
	                logger.info("Uploading file for broker with ID: {}", id);
	                String fileDownloadUri = azureBlob.uploadFile(file);
	                existingBroker.setProfileImageUrl(path + fileDownloadUri);
	                existingBroker.setProfileImage(file.getOriginalFilename());
	            } catch (Exception e) {
	                logger.error("Error uploading file for broker with ID: {}", id, e.getMessage());
	                return new ResponseEntity<>("Error uploading file: " + e.getMessage(), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
	            }
	        }
	        Broker updatedBroker = brokerService.addBrokerDetails(existingBroker, broker);
	        
	        logger.info("Broker details updated successfully for ID: {}", id);
	        return new ResponseEntity<>(updatedBroker, HttpStatus.OK);

	    } catch (Exception e) {
	        logger.error("An error occurred while processing addBroker for ID: {}", id, e.getMessage());
	        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
	    }
	}
	
	@PostMapping("/add-bank-details/{id}")
	public ResponseEntity<?> addBankDetails(@PathVariable Long id, @RequestBody Broker broker) {
	    try {
	        Broker existingBroker = brokerService.getBrokerById(id, false);

	        if (existingBroker == null) {
	            logger.error("Broker not found with ID: {}", id);
	            return new ResponseEntity<>("Broker not found with ID: " + id, HttpStatus.NOT_FOUND);
	        }

	        Broker updatedBroker = brokerService.addBankDetails(broker, existingBroker);

	        logger.info("Bank details successfully added for Broker with ID: {}", id);
	        return new ResponseEntity<>(updatedBroker, HttpStatus.OK);

	    } catch (Exception e) {
	        logger.error("An error occurred while adding bank details for Broker with ID: {}", id, e);
	        return new ResponseEntity<>("An error occurred while adding bank details: " + e.getMessage(),
	                                    HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	@PutMapping("/password-reset")
	public ResponseEntity<?> resetBrokerPassword(@RequestBody BrokerLoginDto newBroker) {
	    try {
	        Broker broker = brokerService.getBrokerByEmailId(newBroker.getEmail(), false);

	        if (broker != null) {
	            if (newBroker.getEmail() != null && newBroker.getPassword() != null) {
	                String encodedPassword = new BCryptPasswordEncoder().encode(newBroker.getPassword());
	                broker.setPassword(encodedPassword);
	                Broker updatedBroker = brokerService.updateBroker_Password_Details(broker);
	                
	                logger.info("Password for broker with email {} updated successfully.", newBroker.getEmail());
	                return new ResponseEntity<>("Password updated successfully!", HttpStatus.OK);
	            } else {
	                logger.warn("Email or password is null for password reset request.");
	                return new ResponseEntity<>("Email or password cannot be null", HttpStatus.BAD_REQUEST);
	            }
	        } else {
	            logger.error("No broker found with email: {}", newBroker.getEmail());
	            return new ResponseEntity<>("No broker found with the provided email", HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        logger.error("An error occurred while resetting password for email: {}", newBroker.getEmail(), e);
	        return new ResponseEntity<>("An error occurred while processing the request: " + e.getMessage(),
	                                    HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteBrokerById(@PathVariable Long id) {
	    try {
	        Broker broker = brokerService.getBrokerById(id, false);

	        if (broker == null) {
	            logger.error("No broker found with ID: {}", id);
	            return new ResponseEntity<>("No broker found with id: " + id, HttpStatus.NOT_FOUND);
	        }

	        brokerService.deleteBrokerById(id);
	        logger.info("Broker with ID: {} deleted successfully.", id);
	        return new ResponseEntity<>("Broker with id " + id + " deleted successfully", HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while deleting broker with ID: {}", id, e);
	        return new ResponseEntity<>("An error occurred while processing the request: " + e.getMessage(),
	                                    HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	@PostMapping("/brokerLogin")
	public ResponseEntity<?> brokerLogin(@RequestBody BrokerLoginDto broker) {
	    Logger logger = LoggerFactory.getLogger(getClass());

	    logger.info("Received login request for email: {}", broker.getEmail());

	    try {
	        Broker brokerEntity = brokerService.getBrokerByEmailId(broker.getEmail(), false);
	        if (brokerEntity != null) {
	            if (passwordEncoder.matches(broker.getPassword(), brokerEntity.getPassword())) {
	                BrokerLoginDto brokerDto = mapBroker(brokerEntity);
	                brokerDto.setBrokerId(brokerEntity.getBrokerId());

	                logger.info("Successful login for broker ID: {}", brokerEntity.getBrokerId());
	                return new ResponseEntity<>(brokerDto, HttpStatus.OK);
	            } else {
	                logger.warn("Password mismatch for email: {}", broker.getEmail());
	                return new ResponseEntity<>("Invalid email or password!", HttpStatus.BAD_REQUEST);
	            }
	        } else {
	            logger.warn("No broker found with email: {}", broker.getEmail());
	            return new ResponseEntity<>("Invalid email or password!", HttpStatus.BAD_REQUEST);
	        }
	    } catch (Exception e) {
	        logger.error("Error occurred during login for email: {}. Error: {}", broker.getEmail(), e.getMessage(), e);
	        return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping("/by/{id}")
	public ResponseEntity<?> getBrokerById(@PathVariable("id") Long id) {
	    Logger logger = LoggerFactory.getLogger(getClass());

	    logger.info("Received request to fetch broker with ID: {}", id);

	    try {
	        Broker fetchBroker = brokerService.getBrokerById(id, false);
	        if (fetchBroker == null) {
	            logger.warn("Broker not found with ID: {}", id);
	            return new ResponseEntity<>("Broker not found with this id " + id + "!", HttpStatus.NOT_FOUND);
	        } else {
	            BrokerDTO brokerDTO = convertToBrokerDTO(fetchBroker);
	            logger.info("Successfully fetched broker with ID: {}", id);
	            return new ResponseEntity<>(brokerDTO, HttpStatus.OK);
	        }
	    } catch (Exception e) {
	        logger.error("Error occurred while fetching broker with ID: {}. Error: {}", id, e.getMessage(), e);
	        return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	@GetMapping("/getAllBroker")
	public ResponseEntity<?> getAllBroker() {
	    Logger logger = LoggerFactory.getLogger(getClass());

	    logger.info("Received request to fetch all brokers");

	    try {
	        List<Broker> brokerList = (List<Broker>) brokerService.findAllBrokers(false);
	        List<BrokerDTO> brokerDtoList = new ArrayList<>();
	        
	        for (Broker b : brokerList) {
	            BrokerDTO brokerDTO = convertToBrokerDTO(b);
	            brokerDtoList.add(brokerDTO);
	        }

	        if (!brokerDtoList.isEmpty()) {
	            logger.info("Successfully fetched {} brokers", brokerDtoList.size());
	            return new ResponseEntity<>(brokerDtoList, HttpStatus.OK);
	        } else {
	            logger.warn("No brokers found");
	            return new ResponseEntity<>("No Broker Found!", HttpStatus.NO_CONTENT);
	        }
	    } catch (Exception e) {
	        logger.error("Error occurred while fetching all brokers. Error: {}", e.getMessage(), e);
	        return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping("/bank-details/{id}")
	public ResponseEntity<?> getBrokerBankDetails(@PathVariable("id") Long id) {
	    Logger logger = LoggerFactory.getLogger(getClass());

	    logger.info("Received request to fetch broker with ID: {}", id);

	    try {
	        Broker fetchBroker = brokerService.getBrokerById(id, false);
	        if (fetchBroker == null) {
	            logger.warn("Broker not found with ID: {}", id);
	            return new ResponseEntity<>("Broker not found with this id " + id + "!", HttpStatus.NOT_FOUND);
	        } else {
	            BrokerBankDTO brokerDTO = convertToBrokerbankDTO(fetchBroker);
	            logger.info("Successfully fetched broker Bank detils with ID: {}", id);
	            return new ResponseEntity<>(brokerDTO, HttpStatus.OK);
	        }
	    } catch (Exception e) {
	        logger.error("Error occurred while fetching broker with ID: {}. Error: {}", id, e.getMessage(), e);
	        return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	private BrokerBankDTO convertToBrokerbankDTO(Broker fetchBroker) {
	    if (fetchBroker == null) {
	        throw new IllegalArgumentException("Broker entity cannot be null");
	    }

	    BrokerBankDTO brokerBankDTO = new BrokerBankDTO();
	    
	    // Map basic broker details
	    brokerBankDTO.setBrokerId(fetchBroker.getBrokerId() != null ? fetchBroker.getBrokerId() : null);
	    brokerBankDTO.setName(fetchBroker.getName() != null ? fetchBroker.getName() : null);
	    brokerBankDTO.setEmail(fetchBroker.getEmail() != null ? fetchBroker.getEmail() : null);
	    brokerBankDTO.setMobile(fetchBroker.getMobile() != null ? fetchBroker.getMobile() : null);
	    
	    // Map commission details
	    brokerBankDTO.setLifeTimeCommission(fetchBroker.getLifeTimeCommission());
	    brokerBankDTO.setBalanceCommission(fetchBroker.getBalanceCommission());

	    // Map bank details
	    brokerBankDTO.setBankAccountNumber(fetchBroker.getBankAccountNumber() != null ? fetchBroker.getBankAccountNumber() : null);
	    brokerBankDTO.setBankName(fetchBroker.getBankName() != null ? fetchBroker.getBankName() : null);
	    brokerBankDTO.setBankBranch(fetchBroker.getBankBranch() != null ? fetchBroker.getBankBranch() : null);
	    brokerBankDTO.setSwiftCode(fetchBroker.getSwiftCode() != null ? fetchBroker.getSwiftCode() : null);
	    brokerBankDTO.setIfscCode(fetchBroker.getIfscCode() != null ? fetchBroker.getIfscCode() : null);

	    // Map PayPal details
	    brokerBankDTO.setPaypalEmailId(fetchBroker.getPaypalEmailId() != null ? fetchBroker.getPaypalEmailId() : null);

	    return brokerBankDTO;
	}


	private BrokerDTO convertToBrokerDTO(Broker fetchBroker) {
	    if (fetchBroker == null) {
	        throw new IllegalArgumentException("Broker entity cannot be null");
	    }

	    BrokerDTO brokerDTO = new BrokerDTO();
	    
	    brokerDTO.setBrokerId(fetchBroker.getBrokerId() != null ? fetchBroker.getBrokerId() : null);
	    brokerDTO.setName(fetchBroker.getName() != null ? fetchBroker.getName() : null);
	    brokerDTO.setEmail(fetchBroker.getEmail() != null ? fetchBroker.getEmail() : null);
	    brokerDTO.setMobile(fetchBroker.getMobile() != null ? fetchBroker.getMobile() : null);
	    brokerDTO.setGender(fetchBroker.getGender() != null ? fetchBroker.getGender() : null);
	    brokerDTO.setBirthday(fetchBroker.getBirthday() != null ? fetchBroker.getBirthday() : null);
	    brokerDTO.setReferralCode(fetchBroker.getReferralCode() != null ? fetchBroker.getReferralCode() : null);
	    brokerDTO.setCountry(fetchBroker.getCountry() != null ? fetchBroker.getCountry() : null);
	    brokerDTO.setLanguage(fetchBroker.getLanguage() != null ? fetchBroker.getLanguage() : null);
	    brokerDTO.setState(fetchBroker.getState() != null ? fetchBroker.getState() : null);
	    brokerDTO.setCity(fetchBroker.getCity() != null ? fetchBroker.getCity() : null);
	    brokerDTO.setZipCode(fetchBroker.getZipCode() != null ? fetchBroker.getZipCode() : null);
	    brokerDTO.setTimezone(fetchBroker.getTimezone() != null ? fetchBroker.getTimezone() : null);
	    brokerDTO.setProfileImage(fetchBroker.getProfileImage() != null ? fetchBroker.getProfileImage() : null);
	    brokerDTO.setProfileImageUrl(fetchBroker.getProfileImageUrl() != null ? fetchBroker.getProfileImageUrl() : null);

	    return brokerDTO;
	}


	private BrokerLoginDto mapBroker(Broker brokerEntity) {
		BrokerLoginDto dto=new BrokerLoginDto();
		dto.setEmail(brokerEntity.getEmail());
		dto.setName(brokerEntity.getName());
		dto.setBrokerId(brokerEntity.getBrokerId());
		return dto;
	}
}
