package com.sc.mp.model;

public class WebScSendRecord {
	private String recordId;
	private String documentId;
	private Integer userId;
	private String memo;
	private Integer sendUserId;
	private String state;
	private String errmsg;
	
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Integer getSendUserId() {
		return sendUserId;
	}
	public void setSendUserId(Integer sendUserId) {
		this.sendUserId = sendUserId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}
