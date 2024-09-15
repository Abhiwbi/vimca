package com.example.vimca.service;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vimca.GlobalException.MyException;
import com.example.vimca.Model.AppUser;
import com.example.vimca.Repository.AppUserRepository;
import com.example.vimca.controller.BrokerController;

@Service
public class AppUserService {
	
	
	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private EntityManager entityManager;
    private static final Logger logger = LoggerFactory.getLogger(AppUserService.class);

	public AppUser addAppUser(AppUser appUser) {
	    AppUser savedAppUser = appUserRepository.save(appUser);
	    return savedAppUser;
	}

	public AppUser getAppUserById(Long id, boolean isDeleted) {
	    try {
	        AppUser appUserEntity = appUserRepository.getUserById(id, false);
	        return appUserEntity;
	    } catch (Exception e) {
	        return null;
	    }
	}

	public AppUser getAppUserByMobileNumber(String phoneNumber, boolean isDeleted) {
	    try {
	        AppUser appUserEntity = appUserRepository.getUserByMobileNumber(phoneNumber, false);
	        return appUserEntity;
	    } catch (Exception e) {
	        return null;
	    }
	}

	public Iterable<AppUser> findAllAppUser(boolean isDeleted) {
	    Session session = entityManager.unwrap(Session.class);
	    Filter filter = session.enableFilter("deletedAppUserFilter");
	    filter.setParameter("isDeleted", isDeleted);
	    Iterable<AppUser> appUserEntities = appUserRepository.findAll();
	    session.disableFilter("deletedAppUserFilter");
	    return appUserEntities;
	}

	public AppUser deleteAppUserById(Long id) {
	    try {
	        AppUser appUser = appUserRepository.getUserById(id, false);
	        appUserRepository.deleteById(id);
	        return appUser;
	    } catch (Exception e) {
	        return null;
	    }
	}

	public AppUser getAppUserByEmailId(String email, boolean b) {
	    return appUserRepository.getByEmailId(email, false);
	}

	public AppUser updateAppUser(AppUser existingAppUser, AppUser updatedAppUser) {
	    if (existingAppUser != null && updatedAppUser != null) {
	        // Ensure the updatedAppUser has the ID of the existingAppUser
	        updatedAppUser.setAppUserId(existingAppUser.getAppUserId());

	        // Check if phone number needs to be updated and is not already taken
	        if (updatedAppUser.getMobile() != null 
	            && !updatedAppUser.getMobile().equals(existingAppUser.getMobile())) {
	            AppUser userMobile = appUserRepository.getUserByMobileNumber(updatedAppUser.getMobile(), false);
	            if (userMobile != null) {
	                throw new MyException("Phone number is already present, please try again with a new number");
	            }
	            existingAppUser.setMobile(updatedAppUser.getMobile());
	        }

	        // Check if email needs to be updated and is not already taken
	        if (updatedAppUser.getEmail() != null) {
	            AppUser userByEmail = appUserRepository.getByEmailId(updatedAppUser.getEmail(), false);
	            if (userByEmail == null || userByEmail.getEmail().equals(existingAppUser.getEmail())) {
	                existingAppUser.setEmail(updatedAppUser.getEmail());
	            } else {
	                throw new MyException("Email is already present, please try again with a new email id");
	            }
	        }

	        // Update other fields if they are provided
	        if (updatedAppUser.getName() != null) {
	            existingAppUser.setName(updatedAppUser.getName());
	        }
	        if (updatedAppUser.getGender() != null) {
	            existingAppUser.setGender(updatedAppUser.getGender());
	        }

	        // Update birthday if provided
	        if (updatedAppUser.getBirthday() != null) {
	            existingAppUser.setBirthday(updatedAppUser.getBirthday());
	        }

	        // Update language if provided
	        if (updatedAppUser.getLanguage() != null) {
	            existingAppUser.setLanguage(updatedAppUser.getLanguage());
	        }

	        // Update the date
	        if (updatedAppUser.getUpdatedDate() == null) {
	            ZoneId timeZone = ZoneId.of("Asia/Kolkata");
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	            ZonedDateTime currentDateTime = ZonedDateTime.now(timeZone);
	            String formattedDateTime = currentDateTime.format(formatter);
	            existingAppUser.setUpdatedDate(formattedDateTime);
	        } else {
	            existingAppUser.setUpdatedDate(updatedAppUser.getUpdatedDate());
	        }

	        // Save the updated user
	        AppUser updatedAppUserEntity = appUserRepository.save(existingAppUser);
	        return updatedAppUserEntity;
	    } else {
	        throw new MyException("User or updated information is null");
	    }
	}

	public AppUser update(AppUser appUser) {
	    return appUserRepository.save(appUser);
	}

	public AppUser addAppuser(AppUser existingAppUser, AppUser appUser) {
	    try {
	        if (appUser == null) {
	            logger.error("AppUser object is null");
	            throw new IllegalArgumentException("AppUser details cannot be null");
	        }

	        // Update fields
	        if (appUser.getName() != null) {
	            existingAppUser.setName(appUser.getName());
	            logger.debug("Updated name to: {}", appUser.getName());
	        }

	        if (appUser.getGender() != null) {
	            existingAppUser.setGender(appUser.getGender());
	            logger.debug("Updated gender to: {}", appUser.getGender());
	        }

	        if (appUser.getUserDiscription() != null) {
	            existingAppUser.setUserDiscription(appUser.getUserDiscription());
	            logger.debug("Updated user Discription to: {}", appUser.getUserDiscription());
	        }
	        if (appUser.getBirthday() != null) {
	            existingAppUser.setBirthday(appUser.getBirthday());
	            logger.debug("Updated birthday to: {}", appUser.getBirthday());
	        }

	        if (appUser.getLanguage() != null) {
	            existingAppUser.setLanguage(appUser.getLanguage());
	            logger.debug("Updated language to: {}", appUser.getLanguage());
	        }

	        if (appUser.getCountry() != null) {
	            existingAppUser.setCountry(appUser.getCountry());
	            logger.debug("Updated country to: {}", appUser.getCountry());
	        }

	        if (appUser.getState() != null) {
	            existingAppUser.setState(appUser.getState());
	            logger.debug("Updated state to: {}", appUser.getState());
	        }

	        if (appUser.getCity() != null) {
	            existingAppUser.setCity(appUser.getCity());
	            logger.debug("Updated city to: {}", appUser.getCity());
	        }

	        if (appUser.getZipCode() != null) {
	            existingAppUser.setZipCode(appUser.getZipCode());
	            logger.debug("Updated zip code to: {}", appUser.getZipCode());
	        }

	        if (appUser.getTimezone() != null) {
	            existingAppUser.setTimezone(appUser.getTimezone());
	            logger.debug("Updated timezone to: {}", appUser.getTimezone());
	        }
	        

	        // Updating timestamps
	        existingAppUser.setUpdatedDate(new Date().toString());
	        logger.debug("Updated date set to: {}", existingAppUser.getUpdatedDate());

	        // Save the updated AppUser
	        // Assuming you have a repository or service method to save the updated user
	        AppUser updatedAppUser = appUserRepository.save(existingAppUser);
	        logger.info("Successfully updated AppUser with ID: {}", updatedAppUser.getAppUserId());

	        return updatedAppUser;
	    } catch (Exception e) {
	        logger.error("Error updating AppUser: {}", e.getMessage(), e);
	        throw new RuntimeException("An error occurred while updating AppUser", e);
	    }
	}

}