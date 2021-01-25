package com.sc.mp.model;

import java.io.Serializable;

public class WebScOperative implements Serializable {
	private static final long serialVersionUID = -491259598471802474L;
	
	private String operativeId;
	private String operativeName;
	private Integer urgenttime;
	
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
	public Integer getUrgenttime() {
		return urgenttime;
	}
	public void setUrgenttime(Integer urgenttime) {
		this.urgenttime = urgenttime;
	}
	@Override
	public String toString() {
		return "WebScOperative [operativeId=" + operativeId + ", operativeName=" + operativeName + ", urgenttime="
				+ urgenttime + "]";
	}

}
