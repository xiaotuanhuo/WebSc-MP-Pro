package com.sc.mp.bean;

/**
 * 统计封装类
 * @author aisino
 *
 */
public class OperationCount {
	// 数量
	private Integer count;
	private String pieConst = "const";	// 饼状图常量
	
	// 按年统计手术量要素
	private String year;
	private String month;
	
	// 按市统计手术量要素
	private String cityCode;
	private String cityName;
	
	// 医疗机构手术量报表统计
	private String orgId;
	private String orgName;
	private String province;
	private String city;
	private String area;
	private String wholeDistrict;	// 完整的行政区划名称
	
	// 医生全职兼职统计要素
	private int work;
	private String workDesc;
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public String getPieConst() {
		return pieConst;
	}
	
	public void setPieConst(String pieConst) {
		this.pieConst = pieConst;
	}
	
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
	
	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String proviceCode) {
		this.cityCode = proviceCode;
	}
	
	public String getCityName() {
		return cityName;
	}
	
	public void setCityName(String proviceName) {
		this.cityName = proviceName;
	}
	
	public String getOrgId() {
		return orgId;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getOrgName() {
		return orgName;
	}
	
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public String getProvince() {
		return province;
	}
	
	public void setProvince(String province) {
		this.province = province;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getArea() {
		return area;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getWholeDistrict() {
		return wholeDistrict;
	}
	
	public void setWholeDistrict(String wholeDistrict) {
		this.wholeDistrict = wholeDistrict;
	}

	public int getWork() {
		return work;
	}

	public void setWork(int work) {
		this.work = work;
	}
	
	public String getWorkDesc() {
		return workDesc;
	}
	
	public void setWorkDesc(String workDesc) {
		this.workDesc = workDesc;
	}
	
}
