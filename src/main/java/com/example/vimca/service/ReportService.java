package com.example.vimca.service;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vimca.GlobalException.MyException;
import com.example.vimca.Model.Report;
import com.example.vimca.Repository.ReportRepository;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    public Report createReport(Report report) {
        logger.info("Creating new report with name: {}", report.getReportId());
        return reportRepository.save(report);
    }

    public Report getReportById(Long reportId) {
        logger.info("Fetching report with ID: {}", reportId);
        return reportRepository.getReportById(reportId, false);
    }

    public Iterable<Report> findAllReports(boolean isDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedReportFilter");
        filter.setParameter("isDeleted", isDeleted);
        Iterable<Report> reportEntities = reportRepository.findAll();
        session.disableFilter("deletedReportFilter");
        return reportEntities;
    }

    public void deleteReportById(Long id) {
        try {
            Report report = reportRepository.getReportById(id, false);
            if (report == null) {
                throw new MyException("Report not present to delete");
            }
            logger.info("Deleting report with ID: {}", id);
            reportRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("An error occurred while deleting the report with ID: {}. Error: {}", id, e.getMessage(), e);
            throw new RuntimeException("An error occurred while deleting the report", e);
        }
    }

	public boolean hasReportedRecently(Long reporterId, Long reportedId) {
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        return reportRepository.existsByReporterIdAndReportedIdAndCreatedDateAfter(reporterId, reportedId, twentyFourHoursAgo);
    }
}
