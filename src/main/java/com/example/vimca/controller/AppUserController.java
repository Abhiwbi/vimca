package com.example.vimca.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.vimca.Azure.AzureBlobClient;
import com.example.vimca.Broker.Broker;
import com.example.vimca.Model.AppUser;
import com.example.vimca.dto.AppUserDto;
import com.example.vimca.dto.UserDto;
import com.example.vimca.service.AppUserService;
import com.example.vimca.service.BrokerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@CrossOrigin
@RequestMapping("/app/v1/user")
public class AppUserController {

	@Autowired
	private AppUserService appUserService;
	@Value("${my.global.path}")
	private String path;
	@Autowired
	private AzureBlobClient azureBlob;

	@Autowired
	private BrokerService brokerService;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private static final Logger logger = LoggerFactory.getLogger(BrokerController.class);

	@PostMapping("/add")
	public ResponseEntity<?> addUser(@RequestBody AppUser appUser) {
		try {
			// Validate input
			if (appUser.getEmail() == null || appUser.getEmail().isEmpty()) {
				return new ResponseEntity<>("Please provide an email address!", HttpStatus.BAD_REQUEST);
			}

			// Check for existing email
			if (appUser.getEmail() != null) {
				AppUser existingUserByEmail = appUserService.getAppUserByEmailId(appUser.getEmail(), false);
				if (existingUserByEmail != null) {
					return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
				}
			}

			// Check for existing phone number
			if (appUser.getMobile() != null) {
				AppUser existingUserByPhone = appUserService.getAppUserByMobileNumber(appUser.getMobile(), false);
				if (existingUserByPhone != null) {
					return new ResponseEntity<>("Mobile number already exists", HttpStatus.CONFLICT);
				}
			}
			if (appUser.getDeviceType() != null) {
				appUser.setDeviceType(appUser.getDeviceType());
			}

			if (appUser.getReferralType() != null) {
				Broker broker = brokerService.findBrokerByReferralCode(appUser.getReferralCode(), false);
				if (broker == null) {
					return new ResponseEntity<>("Broker not found", HttpStatus.OK);
				}
				appUser.setBroker(broker);
			}
			// Encode password if present
			if (appUser.getPassword() != null) {
				appUser.setPassword(new BCryptPasswordEncoder().encode(appUser.getPassword()));
			}

			// Add new user
			AppUser addedUser = appUserService.addAppUser(appUser);
			return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			logger.error("Invalid argument: {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("An error occurred while adding the user: {}", e.getMessage());
			System.out.println(e.getMessage());
			return new ResponseEntity<>("An error occurred while adding the user", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/add-user-details/{id}")
	public ResponseEntity<?> addNewAppUser(@PathVariable("id") Long id,
			@RequestParam(name = "file", required = false) MultipartFile file, @RequestParam("data") String data) {
		try {
			// Parse the JSON data to AppUser object
			ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
			AppUser appUser = mapper.readValue(data, AppUser.class);

			// Fetch existing AppUser by ID
			AppUser existingAppUser = appUserService.getAppUserById(id, false);

			// Handle file upload if present
			if (file != null) {
				try {
					logger.info("Uploading file for user with ID: {}", id);
					String fileDownloadUri = azureBlob.uploadFile(file);
					existingAppUser.setProfileImageUrl(path + fileDownloadUri);
					existingAppUser.setProfileImage(file.getOriginalFilename());
				} catch (Exception e) {
					logger.error("Error uploading file for user with ID: {}", id, e);
					return new ResponseEntity<>("Error uploading file: " + e.getMessage(),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
          if(appUser.getIsTestuser()!=null) {
        	  existingAppUser.setIsTestuser(appUser.getIsTestuser());
          }
			// Save or update the AppUser
			AppUser savedUser = appUserService.addAppuser(existingAppUser, appUser);
			return new ResponseEntity<>(savedUser, HttpStatus.OK);
		} catch (JsonProcessingException e) {
			logger.error("Error processing JSON data: {}", e.getMessage());
			return new ResponseEntity<>("Error processing JSON data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("An error occurred while processing the request: {}", e.getMessage());
			return new ResponseEntity<>("An error occurred while processing the request: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/by/{id}")
	public ResponseEntity<?> getAppUserById(@PathVariable("id") Long id) {
		try {
			AppUser user = appUserService.getAppUserById(id, false);
			if (user == null) {
				return new ResponseEntity<>("appUser not present with this id ",
						HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			} else {
				user.setPassword(null);
				return new ResponseEntity<>(user, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("internal server error =>> " + e.getMessage(),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAllAppUser() {
		// use 0 for page key
		List<AppUser> appUserPage = (List<AppUser>) appUserService.findAllAppUser(false);
		if (appUserPage.isEmpty()) {
			return new ResponseEntity<>("There are no AppUsers present!", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(appUserPage, HttpStatus.OK);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteAppUserById(@PathVariable Long id) {
		Logger logger = LoggerFactory.getLogger(getClass());

		logger.info("Received request to delete AppUser with ID: {}", id);

		try {
			AppUser deletedAppUser = appUserService.getAppUserById(id, false);

			if (deletedAppUser == null) {
				logger.warn("No AppUser found with ID: {}", id);
				return new ResponseEntity<>("No AppUser found with id: " + id, HttpStatus.NOT_FOUND);
			}

			appUserService.deleteAppUserById(id);
			logger.info("AppUser with ID: {} deleted successfully", id);
			return new ResponseEntity<>("AppUser with id " + id + " deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while deleting AppUser with ID: {}. Error: {}", id, e.getMessage(), e);
			return new ResponseEntity<>("An error occurred while processing the request: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/MobileNo/{mobile}")
	public ResponseEntity<?> getAppUserByMobileNo(@PathVariable("mobile") String mobile) {
		Logger logger = LoggerFactory.getLogger(getClass());

		logger.info("Received request to fetch AppUser by mobile number: {}", mobile);

		try {
			AppUser fetchAppUser = appUserService.getAppUserByMobileNumber(mobile, false);

			if (fetchAppUser == null) {
				logger.warn("AppUser not found with mobile number: {}", mobile);
				return new ResponseEntity<>("AppUser not found with this mobile number " + mobile + "!",
						HttpStatus.NOT_FOUND);
			} else {
				AppUserDto appUserDto = convertToAppUserDto(fetchAppUser);
				logger.info("AppUser found with mobile number: {}", mobile);
				return new ResponseEntity<>(appUserDto, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Error occurred while fetching AppUser by mobile number: {}. Error: {}", mobile,
					e.getMessage(), e);
			return new ResponseEntity<>("Something went wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/Login")
	public ResponseEntity<?> appUserLogin(@RequestBody UserDto appUserDto) {
		Logger logger = LoggerFactory.getLogger(getClass());

		logger.info("Received login request for email: {}", appUserDto.getEmail());

		try {
			// Fetch AppUser by email
			AppUser appUserEntity = appUserService.getAppUserByEmailId(appUserDto.getEmail(), false);

			if (appUserEntity != null) {
				// Check if the provided password matches
				if (passwordEncoder.matches(appUserDto.getPassword(), appUserEntity.getPassword())) {
					// Map the AppUser to AppUserDto for response
					AppUserDto responseDto = convertToAppUserDto(appUserEntity);
					responseDto.setUserId(appUserEntity.getAppUserId().toString());

					logger.info("Successful login for AppUser ID: {}", appUserEntity.getAppUserId());
					return new ResponseEntity<>(responseDto, HttpStatus.OK);
				} else {
					logger.warn("Password mismatch for email: {}", appUserDto.getEmail());
					return new ResponseEntity<>("Invalid email or password!", HttpStatus.BAD_REQUEST);
				}
			} else {
				logger.warn("No AppUser found with email: {}", appUserDto.getEmail());
				return new ResponseEntity<>("Invalid email or password!", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.error("Error occurred during login for email: {}. Error: {}", appUserDto.getEmail(), e.getMessage(),
					e);
			return new ResponseEntity<>("An error occurred while processing your request.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private AppUserDto convertToAppUserDto(AppUser fetchAppUser) {
		if (fetchAppUser == null) {
			logger.error("AppUser entity is null");
			throw new IllegalArgumentException("AppUser entity cannot be null");
		}
		AppUserDto appUserDto = new AppUserDto();
		appUserDto.setUserId(fetchAppUser.getAppUserId() != null ? fetchAppUser.getAppUserId().toString() : "");
		appUserDto.setName(fetchAppUser.getName() != null ? fetchAppUser.getName() : "");
		appUserDto.setEmail(fetchAppUser.getEmail() != null ? fetchAppUser.getEmail() : "");
		appUserDto.setPhone(fetchAppUser.getMobile() != null ? fetchAppUser.getMobile() : "");
		appUserDto.setCountry(fetchAppUser.getCountry() != null ? fetchAppUser.getCountry() : "");
		appUserDto.setLanguage(fetchAppUser.getLanguage() != null ? fetchAppUser.getLanguage() : "");
		appUserDto.setGender(fetchAppUser.getGender() != null ? fetchAppUser.getGender() : "");
		appUserDto.setBirthDate(fetchAppUser.getBirthday() != null ? fetchAppUser.getBirthday() : "");
		appUserDto.setTotalgems(
				fetchAppUser.getTotalGems() != null ? fetchAppUser.getTotalGems().toString() : "0.0"); // Double
		appUserDto.setDeviceType(fetchAppUser.getDeviceType() != null ? fetchAppUser.getDeviceType() : "");
		appUserDto.setIsSubscription(
				fetchAppUser.getSubscriptionStatus() != null ? fetchAppUser.getSubscriptionStatus() : Boolean.FALSE);
		logger.info("Converted AppUser to AppUserDto with ID: {}", fetchAppUser.getAppUserId());

		return appUserDto;
	}
	
	

}
