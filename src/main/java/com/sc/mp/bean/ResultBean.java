package com.sc.mp.bean;

import java.io.Serializable;

public class ResultBean implements Serializable {
	private static final long serialVersionUID = -8276264968757808344L;
	private static final String SUCCESS = "1";
	public static final String FAIL = "0";
	private String msg = "操作成功";
	private String code = SUCCESS;
	
	private ResultBean() {
		super();
	}
	
	public ResultBean(String msg, String code) {
		this.msg = msg;
		this.code = code;
	}
	
	public static ResultBean success() {
		return success("操作成功");
	}
	
	public static ResultBean success(String msg) {
		return new ResultBean(msg, SUCCESS);
	}
	
	public static ResultBean error(String msg) {
		ResultBean resultBean = new ResultBean();
		resultBean.setCode(FAIL);
		resultBean.setMsg(msg);
		return resultBean;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
}
