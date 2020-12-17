package com.sc.mp.model;

import java.util.Date;

public class WebScCalendar {
	private String calendarId;
	private Integer userId;
	private String title;
	private Date startTime;
	private Date endTime;
	private String memo;
	// 前端页面展示内容
	private String name;	// 医生姓名
	private String phone;	// 医生电话
	private String dept;	// 备案信息
	private String begin;	// 开始时间
	private String end;		// 开始时间
	private String day;		// 备休日
	
	public String getCalendarId() {
		return calendarId;
	}
	
	public void setCalendarId(String calendarId) {
		this.calendarId = calendarId == null ? null : calendarId.trim();
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getMemo() {
		return memo;
	}
	
	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getDept() {
		return dept;
	}
	
	public void setDept(String dept) {
		this.dept = dept;
	}
	
	public String getBegin() {
		return begin;
	}
	
	public void setBegin(String begin) {
		this.begin = begin;
	}
	
	public String getEnd() {
		return end;
	}
	
	public void setEnd(String end) {
		this.end = end;
	}
	
	public String getDay() {
		return day;
	}
	
	public void setDay(String day) {
		this.day = day;
	}
	
}