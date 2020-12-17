package com.sc.mp.mapper;

import com.sc.mp.model.WebScDept;

public interface DeptMapper {
	int deleteByPrimaryKey(String deptId);
	
	int insert(WebScDept record);
	
	int insertSelective(WebScDept record);
	
	WebScDept selectByPrimaryKey(String deptId);
	
	int updateByPrimaryKeySelective(WebScDept record);
	
	int updateByPrimaryKey(WebScDept record);
}