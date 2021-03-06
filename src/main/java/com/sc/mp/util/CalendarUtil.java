package com.sc.mp.util;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

	public static void main(String[] args) {
		
//		System.out.println(getMonth(new Date()));
		
//		System.out.println(getDay(new Date()));
		
//		System.out.println(getYear(new Date()));
		
//		System.out.println(getFirstDayOfGivenMonth(new Date()));
//		System.out.println(getFirstDayOfNextMonth(new Date(120,11,30)));
		
//		System.out.println(dateAddDays(new Date(), 1));
		System.out.println(monthAdds(new Date(), -1));
		System.out.println(yearAdds(new Date(), -1));
		
	}
	
	/**
	 * 获取年份
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		
		return year;
	}
	
	/**
	 * 获取当月的号数
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		return day;
	}
	
	/**
	 * 获取月份
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH);
		
		return month + 1;
	}
	
	/**
	 * 获取指定日期当月的第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfGivenMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.add(Calendar.MONTH, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定日期下个月的第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfNextMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}
	
	/**
	 * 日期加减天数
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date dateAddDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);//加days天
		
		return calendar.getTime();
	}
	
	/**
	 * 月份加减
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date monthAdds(Date date, int cs) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, cs);//加cs天
		
		return calendar.getTime();
	}
	
	/**
	 * 年份加减
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date yearAdds(Date date, int cs) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, cs);//加cs天
		
		return calendar.getTime();
	}

}
