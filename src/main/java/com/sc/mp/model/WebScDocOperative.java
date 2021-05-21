package com.sc.mp.model;

import java.util.Date;

import com.sc.mp.util.DateUtils;

public class WebScDocOperative {
    private String documentOperativeId;

    private String documentId;

    private String operativeId;

    private String anestheticId;

    private String patientName;

    private Integer patientAge;

    private String patientSex;

    private String patientNum;

    private String patientBednum;

    private String patienttypeId;

    private String operationtypeId;

    private String documentTitle;

    private String documentType;

    private String operateUser;

    private String operateAide;

    private String applyUserId;

    private String adminUserId;

    private String qaUserId;

    private String qaTeamId;

    private String orgId;

    private String hospitalMemo;

    private String qaMemo;

    private String adminMemo;

    private String memo;

    private String documentState;

    private String operateStartTime;

    private String operateEndTime;

    private Date createDate;

    private Float hospitalEvaluate;

    private String hospitalEvaluateMemo;

    private Float doctorEvaluate;

    private String doctorEvaluateMemo;

    private String transferUserId;

    private String oldDocumentState;

    private String deleteReason;

    private String qxRadio;
    
    public WebScDocOperative() {}
    
    public WebScDocOperative(WebScDoc webScDoc, String operativeId) {
    	this.adminMemo = webScDoc.getAdminMemo();
    	this.adminUserId = webScDoc.getAdminUserId();
    	this.anestheticId = webScDoc.getAnestheticId();
    	this.applyUserId = webScDoc.getApplyUserId();
    	this.createDate = DateUtils.parseDate(webScDoc.getCreateDate());
    	this.deleteReason = webScDoc.getDeleteReason();
    	this.doctorEvaluate = webScDoc.getDoctorEvaluate();
    	this.doctorEvaluateMemo = webScDoc.getDoctorEvaluateMemo();
    	this.documentId = webScDoc.getDocumentId();
    	this.documentOperativeId = webScDoc.getDocumentId()+"_"+operativeId;
    	this.documentState = webScDoc.getDocumentState();
    	this.documentTitle = webScDoc.getDocumentTitle();
    	this.documentType = webScDoc.getDocumentType();
    	this.hospitalEvaluate = webScDoc.getHospitalEvaluate();
    	this.hospitalEvaluateMemo = webScDoc.getHospitalEvaluateMemo();
    	this.hospitalMemo = webScDoc.getHospitalMemo();
    	this.memo = webScDoc.getMemo();
    	this.oldDocumentState = webScDoc.getOldDocumentState();
    	this.operateAide = webScDoc.getOperateQide();
    	this.operateEndTime = webScDoc.getOperateEndTime();
    	this.operateStartTime = webScDoc.getOperateStartTime();
    	this.operateUser = webScDoc.getOperateUser();
    	this.operationtypeId = webScDoc.getOperationtypeId();
    	this.operativeId = operativeId;
    	this.orgId = webScDoc.getOrgId();
    	this.patientAge = webScDoc.getPatientAge();
    	this.patientBednum = webScDoc.getPatientBednum();
    	this.patientName = webScDoc.getPatientName();
    	this.patientNum = webScDoc.getPatientNum();
    	this.patientSex = webScDoc.getPatientSex();
    	this.patienttypeId = webScDoc.getPatienttypeId();
    	this.qaMemo = webScDoc.getQaMemo();
    	this.qaTeamId = webScDoc.getQaTeamId();
    	this.qaUserId = webScDoc.getQaUserName();
    	this.qxRadio = webScDoc.getQxRadio();
    	this.transferUserId = webScDoc.getTransferUserId();
    	
    }

    public String getDocumentOperativeId() {
        return documentOperativeId;
    }

    public void setDocumentOperativeId(String documentOperativeId) {
        this.documentOperativeId = documentOperativeId == null ? null : documentOperativeId.trim();
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId == null ? null : documentId.trim();
    }

    public String getOperativeId() {
        return operativeId;
    }

    public void setOperativeId(String operativeId) {
        this.operativeId = operativeId == null ? null : operativeId.trim();
    }

    public String getAnestheticId() {
        return anestheticId;
    }

    public void setAnestheticId(String anestheticId) {
        this.anestheticId = anestheticId == null ? null : anestheticId.trim();
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName == null ? null : patientName.trim();
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex == null ? null : patientSex.trim();
    }

    public String getPatientNum() {
        return patientNum;
    }

    public void setPatientNum(String patientNum) {
        this.patientNum = patientNum == null ? null : patientNum.trim();
    }

    public String getPatientBednum() {
        return patientBednum;
    }

    public void setPatientBednum(String patientBednum) {
        this.patientBednum = patientBednum == null ? null : patientBednum.trim();
    }

    public String getPatienttypeId() {
        return patienttypeId;
    }

    public void setPatienttypeId(String patienttypeId) {
        this.patienttypeId = patienttypeId == null ? null : patienttypeId.trim();
    }

    public String getOperationtypeId() {
        return operationtypeId;
    }

    public void setOperationtypeId(String operationtypeId) {
        this.operationtypeId = operationtypeId == null ? null : operationtypeId.trim();
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle == null ? null : documentTitle.trim();
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType == null ? null : documentType.trim();
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser == null ? null : operateUser.trim();
    }

    public String getOperateAide() {
        return operateAide;
    }

    public void setOperateAide(String operateAide) {
        this.operateAide = operateAide == null ? null : operateAide.trim();
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId == null ? null : applyUserId.trim();
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId == null ? null : adminUserId.trim();
    }

    public String getQaUserId() {
        return qaUserId;
    }

    public void setQaUserId(String qaUserId) {
        this.qaUserId = qaUserId == null ? null : qaUserId.trim();
    }

    public String getQaTeamId() {
        return qaTeamId;
    }

    public void setQaTeamId(String qaTeamId) {
        this.qaTeamId = qaTeamId == null ? null : qaTeamId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getHospitalMemo() {
        return hospitalMemo;
    }

    public void setHospitalMemo(String hospitalMemo) {
        this.hospitalMemo = hospitalMemo == null ? null : hospitalMemo.trim();
    }

    public String getQaMemo() {
        return qaMemo;
    }

    public void setQaMemo(String qaMemo) {
        this.qaMemo = qaMemo == null ? null : qaMemo.trim();
    }

    public String getAdminMemo() {
        return adminMemo;
    }

    public void setAdminMemo(String adminMemo) {
        this.adminMemo = adminMemo == null ? null : adminMemo.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getDocumentState() {
        return documentState;
    }

    public void setDocumentState(String documentState) {
        this.documentState = documentState == null ? null : documentState.trim();
    }

    public String getOperateStartTime() {
        return operateStartTime;
    }

    public void setOperateStartTime(String operateStartTime) {
        this.operateStartTime = operateStartTime == null ? null : operateStartTime.trim();
    }

    public String getOperateEndTime() {
        return operateEndTime;
    }

    public void setOperateEndTime(String operateEndTime) {
        this.operateEndTime = operateEndTime == null ? null : operateEndTime.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Float getHospitalEvaluate() {
        return hospitalEvaluate;
    }

    public void setHospitalEvaluate(Float hospitalEvaluate) {
        this.hospitalEvaluate = hospitalEvaluate;
    }

    public String getHospitalEvaluateMemo() {
        return hospitalEvaluateMemo;
    }

    public void setHospitalEvaluateMemo(String hospitalEvaluateMemo) {
        this.hospitalEvaluateMemo = hospitalEvaluateMemo == null ? null : hospitalEvaluateMemo.trim();
    }

    public Float getDoctorEvaluate() {
        return doctorEvaluate;
    }

    public void setDoctorEvaluate(Float doctorEvaluate) {
        this.doctorEvaluate = doctorEvaluate;
    }

    public String getDoctorEvaluateMemo() {
        return doctorEvaluateMemo;
    }

    public void setDoctorEvaluateMemo(String doctorEvaluateMemo) {
        this.doctorEvaluateMemo = doctorEvaluateMemo == null ? null : doctorEvaluateMemo.trim();
    }

    public String getTransferUserId() {
        return transferUserId;
    }

    public void setTransferUserId(String transferUserId) {
        this.transferUserId = transferUserId == null ? null : transferUserId.trim();
    }

    public String getOldDocumentState() {
        return oldDocumentState;
    }

    public void setOldDocumentState(String oldDocumentState) {
        this.oldDocumentState = oldDocumentState == null ? null : oldDocumentState.trim();
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason == null ? null : deleteReason.trim();
    }

    public String getQxRadio() {
        return qxRadio;
    }

    public void setQxRadio(String qxRadio) {
        this.qxRadio = qxRadio == null ? null : qxRadio.trim();
    }
}