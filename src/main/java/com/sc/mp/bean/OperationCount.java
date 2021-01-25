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
	
	// 近3年总量统计、机构、手术大类
	private String name;	// 图例
	private String date;	// 坐标轴X
	
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
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

	@Override
	public String toString() {
		return "OperationCount [count=" + count + ", pieConst=" + pieConst + ", name=" + name + ", date=" + date
				+ ", cityCode=" + cityCode + ", cityName=" + cityName + ", orgId=" + orgId + ", orgName=" + orgName
				+ ", province=" + province + ", city=" + city + ", area=" + area + ", wholeDistrict=" + wholeDistrict
				+ ", work=" + work + ", workDesc=" + workDesc + "]";
	}
	
}
