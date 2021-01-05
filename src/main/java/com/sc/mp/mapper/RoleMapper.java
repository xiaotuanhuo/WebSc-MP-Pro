package com.sc.mp.mapper;

import com.sc.mp.model.WebScRole;

public interface RoleMapper {
	int deleteByPrimaryKey(Integer roleId);
	
	int insert(WebScRole record);
	
	int insertSelective(WebScRole record);
	
	WebScRole selectByPrimaryKey(Integer roleId);
	
	int updateByPrimaryKeySelective(WebScRole record);
	
	int updateByPrimaryKey(WebScRole record);
}