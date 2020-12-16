package com.sc.mp.model;

import java.io.Serializable;

public class WebScUser implements Serializable {

    private static final long serialVersionUID = -3200103254689137288L;
    
	private Integer userId;
 
	private String userName;
	
	private String loginName;
	
	private String loginPwd;
	
	private String salt;
	
	private String sex;
	
	private int age;
	
	private String phone;
	
	private String email;
	
	private String idCard;
	
	private String certificateNo;
	
	private String occupationalNo;
	
	private String titles;
	
	private String titlesNo;
	
	private String roleId;
	
	private String roleName;
	
	private String roleTypeId;
	
	private String roleTypeName;
	
	private String province;
	
	private String city;
	
	private String area;
	
	private String patientType;
	
	private String operationType;
	
	private String status;
	
	private String lastLoginTime;
	
	private String createTime;
	
	private String modifyTime;
	
	private String photo;
	
	private String wxOpenid;
	
	private String wxUserid;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getOccupationalNo() {
		return occupationalNo;
	}

	public void setOccupationalNo(String occupationalNo) {
		this.occupationalNo = occupationalNo;
	}

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public String getTitlesNo() {
		return titlesNo;
	}

	public void setTitlesNo(String titlesNo) {
		this.titlesNo = titlesNo;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getWxOpenid() {
		return wxOpenid;
	}

	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}

	public String getWxUserid() {
		return wxUserid;
	}

	public void setWxUserid(String wxUserid) {
		this.wxUserid = wxUserid;
	}

	public String getPatientType() {
		return patientType;
	}

	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getRoleTypeId() {
		return roleTypeId;
	}

	public void setRoleTypeId(String roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	public String getRoleTypeName() {
		return roleTypeName;
	}

	public void setRoleTypeName(String roleTypeName) {
		this.roleTypeName = roleTypeName;
	}
}
