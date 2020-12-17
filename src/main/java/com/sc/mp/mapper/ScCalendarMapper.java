package com.sc.mp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sc.mp.model.WebScCalendar;
import com.sc.mp.model.WebScUser;

public interface ScCalendarMapper {
	int deleteByPrimaryKey(String calendarId);
	
	int insert(WebScCalendar record);
	
	int insertSelective(WebScCalendar record);
	
	WebScCalendar selectByPrimaryKey(String calendarId);
	
	List<WebScCalendar> selectCalendarsByDate(@Param("date") String date, @Param("user") WebScUser user);
	
	List<WebScCalendar> selectByDoctor(@Param("userId") Integer doctorId);
	
	List<WebScCalendar> selectAll();
	
	int updateByPrimaryKeySelective(WebScCalendar record);
	
	int updateByPrimaryKey(WebScCalendar record);
}