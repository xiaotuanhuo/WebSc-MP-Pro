package com.sc.mp.bean;

import java.util.List;

import com.sc.mp.model.WebScCalendar;

/**
 * 按日备休封装类
 * @author aisino
 *
 */
public class DayCalendars {
	private String day;
	private List<WebScCalendar> calendars;
	
	public String getDay() {
		return day;
	}
	
	public void setDay(String day) {
		this.day = day;
	}
	
	public List<WebScCalendar> getCalendars() {
		return calendars;
	}
	
	public void setCalendars(List<WebScCalendar> calendars) {
		this.calendars = calendars;
	}
	
}
