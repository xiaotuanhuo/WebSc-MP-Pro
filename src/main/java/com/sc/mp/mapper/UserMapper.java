package com.sc.mp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sc.mp.model.WebScUser;

@Mapper
public interface UserMapper {
	WebScUser selectByPrimaryKey(Integer userId);
	
	WebScUser selectUserInfo(WebScUser user);
	
	/**
	 * 登录校验
	 * @param name
	 * @param pwd
	 * @return
	 */
	WebScUser selectByLoginName(@Param("loginName") String name);
}