package com.sc.mp.bean;

import java.io.Serializable;

public class WebScDoc implements Serializable {
	/**   
	 * @Fields serialVersionUID : TODO
	 *
	*/
	private static final long serialVersionUID = 495917542461743605L;
	
	private String documentId;
	private String operativeId;
	private String operativeName;
	private String anestheticId;
	private String anestheticName;
	private String patientName;
	private Integer patientAge;;
	private String patientSex;
	private String patientNum;
	private String patientBednum;
	private String patienttypeId;
	private String operationtypeId;
	private String documentTitle;
	private String documentType;
	private String operateUser;
	private String operateQide;
	private String applyUserId;
	private String adminUserId;
	private String qaUserId;
	private String qaTeamId;
	private String hospitalMemo;
	private String qaMemo;
	private String adminMemo;
	private String memo;
	private String documentState;
	private String operateStartTime;
	private String operateEndTime;
	private String createDate;
	private Integer hospitalEvaluate;
	private String hospitalEvaluateMemo;
	private Integer doctorEvaluate;
	private String doctorEvaluateMemo;
	private String transferUserId;
	private String oldDocumentState;
	private String deleteReason;
	
	private String qaUserName;
	private String orgId;
	private String orgName;
	private String orgAddress;
	private String orgTel;
	private String leaderName;
	private String leaderTel;
	private String orgProvince;
	private String orgCity;
	private String orgArea;

	//单据发布者所在省，市，区
	private String province;
	private String city;
	private String area;
	
	//角色【临时判断用】
	private String roleId;
	
	//模板
	private String tmpOperativeId;
	private String tmpOperativeName;
	private String tmpAnestheticId;
	private String tmpAnestheticName;
	private String tmpPatientName;
	private Integer tmpPatientAge;
	private String tmpPatientSex;
	
	private Double height;
	private Double weight;
	private String asa;
	private String xtbs;
	private String shs;
	private Integer shcs;
	private String sskssj;
	private String ssjssj;
	private Integer sssc;
	private String jmjs;
	private String ywsjHxxt;
	private String ywsjHxxtQt;
	private String ywsjXhxt;
	private String ywsjXhxtQt;
	private String ywsjGm;
	private String ywsjGmAocre;
	private String ywsjQt;
	private String shblZw;
	private String shblHbtt;
	private String shblEx;
	private String shblXy;
	private String shblOt;
	private Integer shblOtScore;
	private String shttzl;
	private String photo_1;
	private String photo_2;
	private String photo_3;
	private String photo_4;
	private String photo_5;
	private String status;
	
	private String photo;

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getOperativeId() {
		return operativeId;
	}

	public void setOperativeId(String operativeId) {
		this.operativeId = operativeId;
	}

	public String getOperativeName() {
		return operativeName;
	}

	public void setOperativeName(String operativeName) {
		this.operativeName = operativeName;
	}

	public String getAnestheticId() {
		return anestheticId;
	}

	public void setAnestheticId(String anestheticId) {
		this.anestheticId = anestheticId;
	}

	public String getAnestheticName() {
		return anestheticName;
	}

	public void setAnestheticName(String anestheticName) {
		this.anestheticName = anestheticName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(Integer patientAge) {
		this.patientAge = patientAge;
	}

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public String getPatientNum() {
		return patientNum;
	}

	public void setPatientNum(String patientNum) {
		this.patientNum = patientNum;
	}

	public String getPatientBednum() {
		return patientBednum;
	}

	public void setPatientBednum(String patientBednum) {
		this.patientBednum = patientBednum;
	}

	public String getPatienttypeId() {
		return patienttypeId;
	}

	public void setPatienttypeId(String patienttypeId) {
		this.patienttypeId = patienttypeId;
	}

	public String getOperationtypeId() {
		return operationtypeId;
	}

	public void setOperationtypeId(String operationtypeId) {
		this.operationtypeId = operationtypeId;
	}

	public String getDocumentTitle() {
		return documentTitle;
	}

	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getOperateUser() {
		return operateUser;
	}

	public void setOperateUser(String operateUser) {
		this.operateUser = operateUser;
	}

	public String getOperateQide() {
		return operateQide;
	}

	public void setOperateQide(String operateQide) {
		this.operateQide = operateQide;
	}

	public String getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public String getQaUserId() {
		return qaUserId;
	}

	public void setQaUserId(String qaUserId) {
		this.qaUserId = qaUserId;
	}

	public String getQaTeamId() {
		return qaTeamId;
	}

	public void setQaTeamId(String qaTeamId) {
		this.qaTeamId = qaTeamId;
	}

	public String getHospitalMemo() {
		return hospitalMemo;
	}

	public void setHospitalMemo(String hospitalMemo) {
		this.hospitalMemo = hospitalMemo;
	}

	public String getQaMemo() {
		return qaMemo;
	}

	public void setQaMemo(String qaMemo) {
		this.qaMemo = qaMemo;
	}

	public String getAdminMemo() {
		return adminMemo;
	}

	public void setAdminMemo(String adminMemo) {
		this.adminMemo = adminMemo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDocumentState() {
		return documentState;
	}

	public void setDocumentState(String documentState) {
		this.documentState = documentState;
	}

	public String getOperateStartTime() {
		return operateStartTime;
	}

	public void setOperateStartTime(String operateStartTime) {
		this.operateStartTime = operateStartTime;
	}

	public String getOperateEndTime() {
		return operateEndTime;
	}

	public void setOperateEndTime(String operateEndTime) {
		this.operateEndTime = operateEndTime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getHospitalEvaluate() {
		return hospitalEvaluate;
	}

	public void setHospitalEvaluate(Integer hospitalEvaluate) {
		this.hospitalEvaluate = hospitalEvaluate;
	}

	public String getHospitalEvaluateMemo() {
		return hospitalEvaluateMemo;
	}

	public void setHospitalEvaluateMemo(String hospitalEvaluateMemo) {
		this.hospitalEvaluateMemo = hospitalEvaluateMemo;
	}

	public Integer getDoctorEvaluate() {
		return doctorEvaluate;
	}

	public void setDoctorEvaluate(Integer doctorEvaluate) {
		this.doctorEvaluate = doctorEvaluate;
	}

	public String getDoctorEvaluateMemo() {
		return doctorEvaluateMemo;
	}

	public void setDoctorEvaluateMemo(String doctorEvaluateMemo) {
		this.doctorEvaluateMemo = doctorEvaluateMemo;
	}

	public String getTransferUserId() {
		return transferUserId;
	}

	public void setTransferUserId(String transferUserId) {
		this.transferUserId = transferUserId;
	}

	public String getOldDocumentState() {
		return oldDocumentState;
	}

	public void setOldDocumentState(String oldDocumentState) {
		this.oldDocumentState = oldDocumentState;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	public String getQaUserName() {
		return qaUserName;
	}

	public void setQaUserName(String qaUserName) {
		this.qaUserName = qaUserName;
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

	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public String getOrgTel() {
		return orgTel;
	}

	public void setOrgTel(String orgTel) {
		this.orgTel = orgTel;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public String getLeaderTel() {
		return leaderTel;
	}

	public void setLeaderTel(String leaderTel) {
		this.leaderTel = leaderTel;
	}

	public String getOrgProvince() {
		return orgProvince;
	}

	public void setOrgProvince(String orgProvince) {
		this.orgProvince = orgProvince;
	}

	public String getOrgCity() {
		return orgCity;
	}

	public void setOrgCity(String orgCity) {
		this.orgCity = orgCity;
	}

	public String getOrgArea() {
		return orgArea;
	}

	public void setOrgArea(String orgArea) {
		this.orgArea = orgArea;
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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getTmpOperativeId() {
		return tmpOperativeId;
	}

	public void setTmpOperativeId(String tmpOperativeId) {
		this.tmpOperativeId = tmpOperativeId;
	}

	public String getTmpOperativeName() {
		return tmpOperativeName;
	}

	public void setTmpOperativeName(String tmpOperativeName) {
		this.tmpOperativeName = tmpOperativeName;
	}

	public String getTmpAnestheticId() {
		return tmpAnestheticId;
	}

	public void setTmpAnestheticId(String tmpAnestheticId) {
		this.tmpAnestheticId = tmpAnestheticId;
	}

	public String getTmpAnestheticName() {
		return tmpAnestheticName;
	}

	public void setTmpAnestheticName(String tmpAnestheticName) {
		this.tmpAnestheticName = tmpAnestheticName;
	}

	public String getTmpPatientName() {
		return tmpPatientName;
	}

	public void setTmpPatientName(String tmpPatientName) {
		this.tmpPatientName = tmpPatientName;
	}

	public Integer getTmpPatientAge() {
		return tmpPatientAge;
	}

	public void setTmpPatientAge(Integer tmpPatientAge) {
		this.tmpPatientAge = tmpPatientAge;
	}

	public String getTmpPatientSex() {
		return tmpPatientSex;
	}

	public void setTmpPatientSex(String tmpPatientSex) {
		this.tmpPatientSex = tmpPatientSex;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getAsa() {
		return asa;
	}

	public void setAsa(String asa) {
		this.asa = asa;
	}

	public String getXtbs() {
		return xtbs;
	}

	public void setXtbs(String xtbs) {
		this.xtbs = xtbs;
	}

	public String getShs() {
		return shs;
	}

	public void setShs(String shs) {
		this.shs = shs;
	}

	public Integer getShcs() {
		return shcs;
	}

	public void setShcs(Integer shcs) {
		this.shcs = shcs;
	}

	public String getSskssj() {
		return sskssj;
	}

	public void setSskssj(String sskssj) {
		this.sskssj = sskssj;
	}

	public String getSsjssj() {
		return ssjssj;
	}

	public void setSsjssj(String ssjssj) {
		this.ssjssj = ssjssj;
	}

	public Integer getSssc() {
		return sssc;
	}

	public void setSssc(Integer sssc) {
		this.sssc = sssc;
	}

	public String getJmjs() {
		return jmjs;
	}

	public void setJmjs(String jmjs) {
		this.jmjs = jmjs;
	}

	public String getYwsjHxxt() {
		return ywsjHxxt;
	}

	public void setYwsjHxxt(String ywsjHxxt) {
		this.ywsjHxxt = ywsjHxxt;
	}

	public String getYwsjHxxtQt() {
		return ywsjHxxtQt;
	}

	public void setYwsjHxxtQt(String ywsjHxxtQt) {
		this.ywsjHxxtQt = ywsjHxxtQt;
	}

	public String getYwsjXhxt() {
		return ywsjXhxt;
	}

	public void setYwsjXhxt(String ywsjXhxt) {
		this.ywsjXhxt = ywsjXhxt;
	}

	public String getYwsjXhxtQt() {
		return ywsjXhxtQt;
	}

	public void setYwsjXhxtQt(String ywsjXhxtQt) {
		this.ywsjXhxtQt = ywsjXhxtQt;
	}

	public String getYwsjGm() {
		return ywsjGm;
	}

	public void setYwsjGm(String ywsjGm) {
		this.ywsjGm = ywsjGm;
	}

	public String getYwsjGmAocre() {
		return ywsjGmAocre;
	}

	public void setYwsjGmAocre(String ywsjGmAocre) {
		this.ywsjGmAocre = ywsjGmAocre;
	}

	public String getYwsjQt() {
		return ywsjQt;
	}

	public void setYwsjQt(String ywsjQt) {
		this.ywsjQt = ywsjQt;
	}

	public String getShblZw() {
		return shblZw;
	}

	public void setShblZw(String shblZw) {
		this.shblZw = shblZw;
	}

	public String getShblHbtt() {
		return shblHbtt;
	}

	public void setShblHbtt(String shblHbtt) {
		this.shblHbtt = shblHbtt;
	}

	public String getShblEx() {
		return shblEx;
	}

	public void setShblEx(String shblEx) {
		this.shblEx = shblEx;
	}

	public String getShblXy() {
		return shblXy;
	}

	public void setShblXy(String shblXy) {
		this.shblXy = shblXy;
	}

	public String getShblOt() {
		return shblOt;
	}

	public void setShblOt(String shblOt) {
		this.shblOt = shblOt;
	}

	public Integer getShblOtScore() {
		return shblOtScore;
	}

	public void setShblOtScore(Integer shblOtScore) {
		this.shblOtScore = shblOtScore;
	}

	public String getShttzl() {
		return shttzl;
	}

	public void setShttzl(String shttzl) {
		this.shttzl = shttzl;
	}

	public String getPhoto_1() {
		return photo_1;
	}

	public void setPhoto_1(String photo_1) {
		this.photo_1 = photo_1;
	}

	public String getPhoto_2() {
		return photo_2;
	}

	public void setPhoto_2(String photo_2) {
		this.photo_2 = photo_2;
	}

	public String getPhoto_3() {
		return photo_3;
	}

	public void setPhoto_3(String photo_3) {
		this.photo_3 = photo_3;
	}

	public String getPhoto_4() {
		return photo_4;
	}

	public void setPhoto_4(String photo_4) {
		this.photo_4 = photo_4;
	}

	public String getPhoto_5() {
		return photo_5;
	}

	public void setPhoto_5(String photo_5) {
		this.photo_5 = photo_5;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
}
