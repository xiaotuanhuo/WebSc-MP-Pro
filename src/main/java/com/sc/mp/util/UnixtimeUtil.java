package com.sc.mp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnixtimeUtil {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static long ADAPTVALUE = 28800000;
	
	public static long getUnixMinute(long millis) {
		return (millis + ADAPTVALUE) / 1000 / 60;
	}
	
	public static long getUnixHour(long millis) {
		return (millis + ADAPTVALUE) / 1000 / 60 / 60;
	}
	
	public static long getUnixDay(long millis) {
		return (millis + ADAPTVALUE) / 1000 / 60 / 60 / 24 ;
	}
	
	public static String getStringDay(long unixDay) {
		return UnixtimeUtil.sdf.format(unixDay * 1000 * 60 * 60 * 24 + ADAPTVALUE);
	}
	
	public static void main(String[] args) throws ParseException {
		Date date = new Date();
//		System.out.println(date);
//		System.out.println(getUnixDay(date.getTime()));
		//System.out.println(getUnixDay(System.currentTimeMillis()));
//		System.out.println(getUnixDay(sdf.parse("2018-10-31").getTime()));
//		System.out.println(getStringDay(getUnixDay(date.getTime())));
		System.out.println(getUnixHour(date.getTime()));
	}

}
