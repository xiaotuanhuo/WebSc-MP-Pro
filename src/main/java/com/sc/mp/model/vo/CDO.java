package com.sc.mp.model.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 行政区划树形医疗机构
 * @author aisino
 *
 */
public class CDO {

	private String id;
	private String name;
	private String parentId;		// 所属的行政区划或者医疗机构
	private String rootOrgId;		// 医疗机构根节点id
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<CDO> children;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getParentId() {
		return parentId;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public List<CDO> getChildren() {
		return children;
	}
	
	public void setChildren(List<CDO> children) {
		this.children = children;
	}
	
	public String getRootOrgId() {
		return rootOrgId;
	}
	
	public void setRootOrgId(String rootOrgId) {
		this.rootOrgId = rootOrgId;
	}
	
}
