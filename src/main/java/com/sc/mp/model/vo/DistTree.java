package com.sc.mp.model.vo;

public class DistTree {

	private String id;
	private String parentId;
	private String name;
	private String institution;
	private int distType;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getParentId() {
		return parentId;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getInstitution() {
		return institution;
	}
	
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	
	public int getDistType() {
		return distType;
	}
	
	public void setDistType(int distType) {
		this.distType = distType;
	}
	
}
