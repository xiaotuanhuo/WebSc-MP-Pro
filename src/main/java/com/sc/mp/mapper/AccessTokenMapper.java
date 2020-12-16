package com.sc.mp.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sc.mp.model.WebScAccessToken;

@Mapper
public interface AccessTokenMapper {
	WebScAccessToken getWebScAccessToken();
	
	void updateWebScAccessToken(String accesstoken);
}
