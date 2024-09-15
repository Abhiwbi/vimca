package com.example.vimca.Repository;


import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.vimca.Model.Report;

public interface ReportRepository  extends JpaRepository<Report, Long>{

	@Query("SELECT r FROM Report r WHERE r.reportId = :reportId AND r.deleted = :isDeleted")
    Report getReportById(@Param("reportId") Long reportId, @Param("isDeleted") boolean isDeleted);
	 @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Report r WHERE r.repoterId = :reporterId AND r.reportedId = :reportedId AND r.createdDate > :date")
	    boolean existsByReporterIdAndReportedIdAndCreatedDateAfter(@Param("reporterId") Long reporterId,
	                                                               @Param("reportedId") Long reportedId,
	                                                               @Param("date") LocalDateTime date);
	}
