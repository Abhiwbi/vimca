package com.example.vimca.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.vimca.Azure.AzureBlobClient;
import com.example.vimca.Model.AppUser;
import com.example.vimca.Model.Report;
import com.example.vimca.service.AppUserService;
import com.example.vimca.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@CrossOrigin
@RequestMapping("/app/v1/reports")
public class ReportController {

	@Autowired
	private ReportService reportService;
	@Autowired
	private AppUserService appUserService;
	@Value("${my.global.path}")
	private String path;
	@Autowired
	private AzureBlobClient azureBlob;

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	@PostMapping("/create{reporterId}")
    public ResponseEntity<?> createReport(@PathVariable Long reporterId,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("data") String data) {
        try {
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            Report report = mapper.readValue(data, Report.class);

            AppUser reporter = appUserService.getAppUserById(reporterId, false);
            AppUser reported = appUserService.getAppUserById(report.getReportedId(), false);
            if (reporter == null) {
                logger.error("Reporter with ID {} not found.", report.getRepoterId());
                return new ResponseEntity<>("Reporter not found.", HttpStatus.BAD_REQUEST);
            }
            report.setRepoterId(reporterId);
            if (reported == null) {
                logger.error("Reported user with ID {} not found.", report.getReportedId());
                return new ResponseEntity<>("Reported user not found.", HttpStatus.BAD_REQUEST);
            }
            // Check if the user has already reported the same user in the last 24 hours
            if (reportService.hasReportedRecently(report.getRepoterId(), report.getReportedId())) {
                logger.error("User with ID {} has already reported user with ID {} in the last 24 hours.", 
                             report.getRepoterId(), report.getReportedId());
                return new ResponseEntity<>("You have already reported this user in the last 24 hours.", HttpStatus.FORBIDDEN);
            }
            // Handle file upload if present
            if (file != null && !file.isEmpty()) {
                try {
                    logger.info("Uploading file for report with ID: {}", report.getReportId());
                    String fileDownloadUri = azureBlob.uploadFile(file);
                    report.setAttachmentUrl(fileDownloadUri);
                    report.setAttachmentFilename(file.getOriginalFilename());
                } catch (Exception e) {
                    logger.error("Error uploading file: {}", e.getMessage(), e);
                    return new ResponseEntity<>("Error uploading file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            // Save the report
            Report createdReport = reportService.createReport(report);
            reported.setTotalReport(reported.getTotalReport()+1);
            reported.setRecentReport(reported.getRecentReport()+1);
            AppUser updateReportedId=appUserService.update(reported);
            
            logger.info("Report created successfully with ID: {}", createdReport.getReportId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
        } catch (IOException e) {
            logger.error("Error parsing report data: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid report data.");
        } catch (Exception e) {
            logger.error("Error occurred while creating report: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


	@GetMapping("/{id}")
	public ResponseEntity<Report> getReportById(@PathVariable Long id) {
		try {
			logger.info("Request to fetch report with ID: {}", id);
			Report report = reportService.getReportById(id);
			if (report != null) {
				logger.info("Report fetched successfully: {}", report);
				return ResponseEntity.ok(report);
			} else {
				logger.warn("Report not found with ID: {}", id);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception e) {
			logger.error("Error occurred while fetching report with ID: {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<Iterable<Report>> findAllReports(
			@RequestParam(required = false, defaultValue = "false") boolean isDeleted) {
		try {
			logger.info("Request to fetch all reports with isDeleted: {}", isDeleted);
			Iterable<Report> reports = reportService.findAllReports(isDeleted);
			logger.info("Reports fetched successfully");
			return ResponseEntity.ok(reports);
		} catch (Exception e) {
			logger.error("Error occurred while fetching reports: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteReportById(@PathVariable Long id) {
		try {
			logger.info("Request to delete report with ID: {}", id);
			reportService.deleteReportById(id);
			logger.info("Report deleted successfully with ID: {}", id);
			return ResponseEntity.ok("Report deleted successfully");
		} catch (Exception e) {
			logger.error("Error occurred while deleting report with ID: {}: {}", id, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while deleting the report");
		}
	}
	
	
}
