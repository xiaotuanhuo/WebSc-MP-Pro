package com.sc.mp.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sc.mp.mapper.UserMapper;
import com.sc.mp.model.WebScUser;

@Service
public class UserService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Resource
	private UserMapper userMapper;
	
	public String test() {
		log.info("test......");
		WebScUser user = userMapper.selectByPrimaryKey(1);
		return user.getLoginName();
	}
	
	public WebScUser selectUserInfo(WebScUser user){
		return userMapper.selectUserInfo(user);
	}
}