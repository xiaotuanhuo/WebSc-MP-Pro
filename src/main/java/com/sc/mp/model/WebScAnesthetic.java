package com.sc.mp.model;

import java.io.Serializable;

public class WebScAnesthetic implements Serializable {

	private static final long serialVersionUID = 4063115647173007422L;
	
	private String anestheticId;
	private String anestheticName;
	private int indexNo;
	
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
	public int getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}
	@Override
	public String toString() {
		return "WebScAnesthetic [anestheticId=" + anestheticId + ", anestheticName=" + anestheticName + ", indexNo="
				+ indexNo + "]";
	}
}
