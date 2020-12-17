package com.sc.mp.bean;

/**
 * 行政区划
 * @author aisino
 *
 */
public class District {
	private String code;		// 行政区划代码
	private String name;		// 行政区划名称
	private String parentCode;	// 上级行政区划代码
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getParentCode() {
		return parentCode;
	}
	
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
}
