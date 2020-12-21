package com.sc.mp.bean;

/**
 * 按月统计手术量封装类
 * @author aisino
 *
 */
public class OperationCount {
	// 按年统计要素
	private String year;
	private String month;
	
	// 按省统计要素
	private String proviceCode;
	private String proviceName;
	
	// 手术量
	private Integer count;
	
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getMonth() {
		return month;
	}
	
	public void setMonth(String month) {
		this.month = month;
	}
	
	public String getProviceCode() {
		return proviceCode;
	}

	public void setProviceCode(String proviceCode) {
		this.proviceCode = proviceCode;
	}
	
	public String getProviceName() {
		return proviceName;
	}
	
	public void setProviceName(String proviceName) {
		this.proviceName = proviceName;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
