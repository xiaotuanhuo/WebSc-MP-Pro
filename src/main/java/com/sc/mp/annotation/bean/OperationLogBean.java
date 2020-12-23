package com.sc.mp.annotation.bean;

import java.util.Date;

/**
 * 操作日志记录实体类
 * @author pp
 *
 */
public class OperationLogBean {
	private String id; //唯一标识
    private Date createDate; //操作时间
    private String method; //方法名
    private String args; //参数
    private String userId; //操作人id
    private String userName; //操作人
    private String describe; //日志描述
    private Long runTime; //方法运行时间
    private String returnValue; //方法返回值
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getArgs() {
		return args;
	}
	public void setArgs(String args) {
		this.args = args;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public Long getRunTime() {
		return runTime;
	}
	public void setRunTime(Long runTime) {
		this.runTime = runTime;
	}
	public String getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}
	
	@Override
	public String toString() {
		return "OperationLogBean [id=" + id + ", createDate=" + createDate + ", method=" + method + ", args=" + args
				+ ", userId=" + userId + ", userName=" + userName + ", describe=" + describe + ", runTime=" + runTime
				+ ", returnValue=" + returnValue + "]";
	}
}
