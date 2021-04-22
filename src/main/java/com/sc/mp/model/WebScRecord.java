package com.sc.mp.model;

import java.util.Date;

public class WebScRecord {
	private Integer recordId;
	private String userId;
	private String orgId;
	private String doctorName;
	private String doctorPhone;
	private String doctorCity;
	private String orgName;
	private Date recordDate;
	private Date endDate;
	private Date createDate;
	
	public Integer getRecordId() {
		return recordId;
	}
	
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}
	
	public String getOrgId() {
		return orgId;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId == null ? null : orgId.trim();
	}
	
	public String getDoctorName() {
		return doctorName;
	}
	
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName == null ? null : doctorName.trim();
	}
	
	public String getDoctorPhone() {
		return doctorPhone;
	}
	
	public void setDoctorPhone(String doctorPhone) {
		this.doctorPhone = doctorPhone == null ? null : doctorPhone.trim();
	}
	
	public String getDoctorCity() {
		return doctorCity;
	}
	
	public void setDoctorCity(String doctorCity) {
		this.doctorCity = doctorCity == null ? null : doctorCity.trim();
	}
	
	public String getOrgName() {
		return orgName;
	}
	
	public void setOrgName(String orgName) {
		this.orgName = orgName == null ? null : orgName.trim();
	}
	
	public Date getRecordDate() {
		return recordDate;
	}
	
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}