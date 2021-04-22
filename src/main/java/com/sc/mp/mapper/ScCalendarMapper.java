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
	
	/**
	 * 查询指定日期是否有医生备休 返回日期
	 * @param date	为null或者空字符串表示查询当前日期及以后
	 * @param user
	 * @return
	 */
	List<String> selectDateCalendar(@Param("user") WebScUser user);
	
	/**
	 * 查询指定日期备休列表
	 * @param date
	 * @param user
	 * @return
	 */
	List<WebScCalendar> selectCalendarsByDate(@Param("date") String date, @Param("user") WebScUser user);
	
	List<WebScCalendar> selectCalendars(@Param("user") WebScUser user);
	
	List<WebScCalendar> selectByDoctor(@Param("userId") String doctorId);
	
	List<WebScCalendar> selectAll();
	
	int updateByPrimaryKeySelective(WebScCalendar record);
	
	int updateByPrimaryKey(WebScCalendar record);
}