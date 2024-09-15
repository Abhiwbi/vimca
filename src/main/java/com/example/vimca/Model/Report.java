package com.example.vimca.Model;


import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@SQLDelete(sql = "UPDATE report SET deleted = true WHERE report_Id=?")
@FilterDef(name = "deletedReportFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedReportFilter", condition = "deleted = :isDeleted")
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reportId;
	private String reason;
	private Long repoterId;
	private Long reportedId;
	private Long matchInfoId;
	private LocalDateTime createdDate = LocalDateTime.now();
	private String updatedDate;
	private String attachmentFilename;
	private String attachmentUrl;
	private boolean isReportResolved=Boolean.FALSE;
	
	@JsonIgnore
	private boolean deleted = Boolean.FALSE;

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getRepoterId() {
		return repoterId;
	}

	public void setRepoterId(Long repoterId) {
		this.repoterId = repoterId;
	}

	public Long getReportedId() {
		return reportedId;
	}

	public void setReportedId(Long reportedId) {
		this.reportedId = reportedId;
	}

	public Long getMatchInfoId() {
		return matchInfoId;
	}

	public void setMatchInfoId(Long matchInfoId) {
		this.matchInfoId = matchInfoId;
	}

	

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getAttachmentFilename() {
		return attachmentFilename;
	}

	public void setAttachmentFilename(String attachmentFilename) {
		this.attachmentFilename = attachmentFilename;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	public boolean isReportResolved() {
		return isReportResolved;
	}

	public void setReportResolved(boolean isReportResolved) {
		this.isReportResolved = isReportResolved;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Report() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}